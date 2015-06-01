package process;
/**
 * File:        Bowtie.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-06-01
 */

import command.ValidateException;
import response.HttpStatusCode;
import server.ServerSettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO class description goes here...
 */
public class Bowtie extends Executor{

    public enum ScoringScheme { Phred33, Phred64 };

    // Path to the script for FASTQ format detection.
    private static final String fastqFormatDetectPath = "resources/fastqFormatDetect.pl";

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

    public Bowtie(String genomeRelease, String inFile1, String inFile2,
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
            validateException("Bowtie requires a genome release to be " +
                              "provided!");
        }

        if (inFile1 != null || inFile2 != null) {
            validateException("Processing paired files with Bowtie " +
                              "not supported yet");
        }

        if ((inFile1 != null && inFile2 == null)
            || (inFile1 == null && inFile2 != null)) {
            validateException("Bowtie requires both paired files to be " +
                              "provided!");
        }
        if ((inFile1 == null || inFile2 == null) && inFileUnpaired == null ) {
            validateException("Bowtie requires either two paired FASTQ files"
                              + " or a single file with unpaired reads!");
        }


        checkInputFileExists(inFile1);
        checkInputFileExists(inFile2);
        checkInputFileExists(inFileUnpaired);
    }

    private void checkInputFileExists(String fileName) throws ValidateException {
        if (fileName != null && !new File(fileName).exists()) {
            validateException("Bowtie input file '" + fileName +
                              "' doesn't exist!");
        }
    }

    private void validateException(String errMsg) throws ValidateException {
        throw new ValidateException(HttpStatusCode.BAD_REQUEST, errMsg);
    }

    // Execute the command represented by this object.
    // Returns output produced by the program.
    public String execute() throws InterruptedException, IOException {
        ArrayList<String> args = new ArrayList<>();
        args.add(ServerSettings.bowtieLocation);
        args.add(genomeRelease);
        args.add(inFileUnpaired);
        args.add(outFile);

        for (String additionalParam : params) {
            args.add(additionalParam);
        }

        return executeCommand(args.toArray(new String []{}));
    }


}
