package process;

import command.ValidateException;
import response.HttpStatusCode;
import server.ErrorLogger;
import server.ServerSettings;
import util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// A class representing a single invocation of the 'pyicos' program.
public class Pyicos extends Executor {
    // Which command to run? Currently only 'strcorr' and 'convert' are supported.
    private String command;

    // Input file.
    private String inFile;

    // Output file.
    private String outFile;

    // Input file format.
    private String inFormat;

    // Output file format.
    private String outFormat;

    // Additional parameters.
    private String [] params;

    // Supported input formats.
    private static final HashMap<String,Set<String>> supportedInputFormats;

    // Supported output formats.
    private static final HashMap<String,Set<String>> supportedOutputFormats;

    static {
        supportedInputFormats = new HashMap<>();
        supportedOutputFormats = new HashMap<>();

        HashSet<String> convertInputFormats = new HashSet<>();
        convertInputFormats.add("sam");
        supportedInputFormats.put("convert", convertInputFormats);
        HashSet<String> strcorrInputFormats = new HashSet<>();
        strcorrInputFormats.add("sam");
        supportedInputFormats.put("strcorr", strcorrInputFormats);

        HashSet<String> convertOutputFormats = new HashSet<>();
        convertOutputFormats.add("bed_wig");
        supportedOutputFormats.put("convert", convertOutputFormats);
        HashSet<String> strcorrOutputFormats = new HashSet<>();
        strcorrOutputFormats.add("bed_pk");
        supportedOutputFormats.put("strcorr", strcorrOutputFormats);
    }

    public Pyicos(String command, String inFile, String outFile,
                  String inFormat, String outFormat, String [] params) {
        this.command   = command;
        this.inFile    = inFile;
        this.outFile   = outFile;
        this.inFormat  = inFormat;
        this.outFormat = outFormat;
        this.params    = params;
    }

    public void validate() throws ValidateException {
        if (!(command.equals("convert") || command.equals("strcorr"))) {
            validateException("Unsupported 'pyicos' subcommand!");
        }
 /*       if (!new File(inFile).exists()) {
            validateException("The input file '" +
                              (new File(inFile).getAbsolutePath()) + "' doesn't exist!");
        }
   */     if (!supportedInputFormats.get(command).contains(inFormat)) {
            validateException("Input format '" + inFormat
                    + "' not supported for 'pyicos' command '" + command + "'!");
        }
        if (!supportedOutputFormats.get(command).contains(outFormat)) {
            validateException("Output format '" + outFormat
                    + "' not supported for 'pyicos' command '" + command + "'!");
        }
    }

    private void validateException(String errMsg) throws ValidateException {
        throw new ValidateException(HttpStatusCode.BAD_REQUEST, errMsg);
    }

    // Execute the command represented by this Pyicos object.
    // Returns output produced by the program.
    public String execute() throws InterruptedException, IOException {
        ArrayList<String> args = new ArrayList<>();
        args.add("python");
        args.add(ServerSettings.pyicosLocation);
        args.add(command);
        args.add(inFile); args.add(outFile);
        args.add("-f"); args.add(inFormat);
        args.add("-F"); args.add(outFormat);
        for (String additionalParam : params) {
            args.add(additionalParam);
        }

        return executeCommand(args.toArray(new String[]{}));
    }

    public void cleanupTempFiles() {
        try {
            String outDir = new File(outFile).getParentFile().getCanonicalPath();
            // TODO: path to resources/ is hardcoded in Executor.
            Files.delete(new File("resources/pyicos.log").toPath());
            Files.delete(new File(outDir + "/correlation_test.pk.png").toPath());
        }
        catch (IOException ex) {
            //ignore
        }
    }

    // Estimate the extension length with strength correlation.
    public static int runStrcorr(String samFile)
            throws ValidateException, InterruptedException, IOException {
        // Run 'strcorr'.
        Pyicos strcorr = new Pyicos("strcorr", samFile, Util.replaceExtension(samFile, ".pk"),
                "sam", "bed_pk", new String[] {});
        strcorr.validate();
	ErrorLogger.log("SYSTEM", "Validated strcorr");
        String haystack = strcorr.execute();
	ErrorLogger.log("SYSTEM", "Executed strcorr");
        strcorr.cleanupTempFiles();


        // Get the extension length from its output.
        String needle = "Correlation test RESULT: You should extend this dataset to ";
        if (haystack.indexOf(needle) < 0) {
 		ErrorLogger.log("SYSTEM", "Pyicos error, correlation result invalid");
		return 303;
	   	
        }
        Pattern p = Pattern.compile(needle + "([0-9]+(\\.[0-9]+)?)");
        Matcher m = p.matcher(haystack);
        m.find();
        String found = m.group(1);

        return Math.round(Float.valueOf(found));
    }

    // Convert a sam file to a wig file. Returns the name of the newly-produced wig file.
    public static String runConvert(String samFile, String wigFile)
            throws ValidateException, InterruptedException, IOException {
        // Get the extension length from strcorr.
        int extensionLength = runStrcorr(samFile);
	ErrorLogger.log("SYSTEM", "Done with strcorr");

        // Run 'convert'.
        Pyicos convert = new Pyicos("convert", samFile, wigFile,
                "sam", "bed_wig", new String[] {"-O", "-x", String.valueOf(extensionLength)});
        convert.validate();
	ErrorLogger.log("SYSTEM", "Validated conversion");
        convert.execute();
	ErrorLogger.log("SYSTEM", "Executed conversion");
        convert.cleanupTempFiles();

        return wigFile;
    }
}
