package process;

import command.ValidateException;
import server.Debug;
import server.ServerSettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// Like process.Smooth, but uses Java code
// from 'smoothing.SmoothingAndStep' for smoothing.
public class SmoothJava extends Smooth {

    private String inFile;
    private String outFile;
    private int    stepSize;

    public SmoothJava (int    windowSize,
                       int    meanType,
                       int    minPos,
                       int    calcTotalMean,
                       int    printPos,
                       String inFile,
                       String outFile,
                       int stepSize) {
        super(inFile, windowSize, meanType, minPos, calcTotalMean, printPos);
        this.inFile = inFile;
        this.outFile = outFile;
        this.stepSize = stepSize;
    }

    @Override
    public void validate() throws ValidateException {
        /* Validate parameters */
        this.parameters.validateParameters();

        /* Validate environment */
        if (!new File(ServerSettings.smoothingJarLocation).exists())
            throw new ValidateException(404, "Smoothing script '" +
                    ServerSettings.smoothingJarLocation + "' is missing!");
    }

    @Override
    public String execute() throws IOException, InterruptedException {
        ArrayList<String> args = new ArrayList<>();

        /* Time to build our command */
        args.add("java");
        args.add("-jar");
        args.add(ServerSettings.smoothingJarLocation);
        args.add(String.valueOf(parameters.getWindowSize()));
        args.add(String.valueOf(parameters.getMeanType()));
        args.add(String.valueOf(parameters.getMinPos()));
        args.add(String.valueOf(parameters.getCalcTotalMean()));
        args.add(String.valueOf(parameters.getPrintPos()));
        args.add(inFile);
        args.add(outFile);
        args.add(String.valueOf(stepSize));

        return executeCommand(args.toArray(new String[args.size()]));
    }

    public static void runSmoothing(String path, int windowSize, int meanType,
                                    int minPos, int calcTotalMean, int printPos,
                                    String outputPath, int stepSize)
            throws ValidateException, InterruptedException, IOException {

        SmoothJava smooth = new SmoothJava(windowSize, meanType, minPos,
                calcTotalMean, printPos, path, outputPath, stepSize);
        Debug.log("Started smoothing on " + path);

        smooth.validate();
        Debug.log("Validated smoothing on " + path);

        smooth.execute();
        Debug.log("Executed smoothing on " + path);

        Debug.log("Output filename is " + outputPath);
    }
}