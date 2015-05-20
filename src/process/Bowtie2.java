package process;

import command.ValidateException;
import response.HttpStatusCode;
import server.ServerSettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// A class representing a single invocation of the 'bowtie2' program.
public class Bowtie2 extends Executor {
    public enum ScoringScheme { Phred33, Phred64 };

    // Scoring scheme to use.
    private ScoringScheme scoringScheme = ScoringScheme.Phred33;

    // Input genome release (-x), obligatory.
    private String genomeRelease;

    // Input FASTQ file with #1 mates (-1), optional.
    private String inFile1;

    // Input FASTQ file with #2 mates (-2), optional.
    private String inFile2;

    // Input FASTQ file with unpaired reads (-U), optional.
    private String inFileUnpaired;

    // Output (SAM) file name.
    private String outFile;

    // Additional parameters.
    private String [] params;

    public Bowtie2(String genomeRelease, String inFile1, String inFile2,
                   String inFileUnpaired, String outFile, String [] params) {
        this.genomeRelease  = genomeRelease;
        this.inFile1        = inFile1;
        this.inFile2        = inFile2;
        this.inFileUnpaired = inFileUnpaired;
        this.outFile        = outFile;
        this.params         = params;
    }

    public void validate() throws ValidateException {
        if (genomeRelease == null) {
            validateException("Bowtie 2 requires a genome release to be provided!");
        }
        if ((inFile1 != null && inFile2 == null)
                || (inFile1 == null && inFile2 != null)) {
            validateException("Bowtie 2 requires both paired files to be provided!");
        }
        if ((inFile1 == null || inFile2 == null) && inFileUnpaired == null ) {
            validateException("Bowtie 2 requires either two paired FASTQ files"
                    + " or a single file with unpaired reads!");
        }
        checkInputFileExists(inFile1);
        checkInputFileExists(inFile2);
        checkInputFileExists(inFileUnpaired);
    }

    private void checkInputFileExists(String fileName) throws ValidateException {
        if (fileName != null && !new File(fileName).exists()) {
            validateException("Bowtie 2 input file '" + fileName + "' doesn't exist!");
        }
    }

    private void validateException(String errMsg) throws ValidateException {
        throw new ValidateException(HttpStatusCode.BAD_REQUEST, errMsg);
    }

    public String execute() throws InterruptedException, IOException {
        scoringScheme = detectScoringScheme(
                inFileUnpaired == null ? inFile1 : inFileUnpaired);
        ArrayList<String> args = new ArrayList<>();
        args.add(ServerSettings.bowtie2Location);
        args.add(scoringSchemeArg());
        args.add("-x");
        args.add(genomeRelease);

        if (inFile1 != null) {
            args.add("-1");
            args.add(inFile1);
        }

        if (inFile2 != null) {
            args.add("-2");
            args.add(inFile2);
        }

        if (inFileUnpaired != null) {
            args.add("-U");
            args.add(inFileUnpaired);
        }

        args.add("-S");
        args.add(outFile);

        for (String additionalParam : params) {
            args.add(additionalParam);
        }

        return executeCommand(args.toArray(new String []{}));
    }

    private String scoringSchemeArg() {
        switch (scoringScheme) {
            case Phred33:
                return "--phred33";
            case Phred64:
                return "--phred64";
            default:
                return "--phred33";
        }
    }

    public static ScoringScheme detectScoringScheme(String fastqFile) {
        // Runs the script originally found at
        // https://www.uppmax.uu.se/userscript/check-fastq-quality-score-format
        // Its output is then be mapped to --phred33/--phred64 as follows:
        //
        // Sanger         -> Phred+33
        // Solexa         -> Solexa+64 (What is this? Probably the same as Phred+64)
        // Illumina 1.3+  -> Phred+64
        // Illumina 1.5+  -> Phred+64
        // Illumina 1.8+  -> Phred+33
        //
        // (source: http://en.wikipedia.org/wiki/FASTQ_format#Encoding ,
        // see also https://www.biostars.org/p/63225/
        // and https://www.biostars.org/p/6656/ )
        return ScoringScheme.Phred33;
    }
}
