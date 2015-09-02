package process;

import command.ValidateException;
import server.Debug;
import server.ErrorLogger;
import server.ServerSettings;
import smoothing.SmoothingAndStep;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// Like process.Smooth, but uses Java code
// from 'smoothing.SmoothingAndStep' for smoothing.
public class SmoothJava extends Executor {

    private String inFile;
    private String outFile;
    private int    stepSize;
    protected final SmoothingParameters parameters;

    public SmoothJava (int    windowSize,
                       int    meanType,
                       int    minPos,
                       int    calcTotalMean,
                       int    printPos,
                       String inFile,
                       String outFile,
                       int stepSize) {
        this.inFile = inFile;
        this.outFile = outFile;
        this.stepSize = stepSize;
        
        /* We insert all parameters we've received. */
        this.parameters = new SmoothingParameters();
        parameters.setPath(inFile);
        parameters.setWindowSize(windowSize);
        parameters.setMeanType(meanType);
        parameters.setMinPos(minPos);
        parameters.setCalcTotalMean(calcTotalMean);
        parameters.setPrintPos(printPos);
    }

    public void validate() throws ValidateException {
        /* Validate parameters */
        this.parameters.validateParameters();

        /* Validate environment */
        if (!new File(ServerSettings.smoothingJarLocation).exists())
            throw new ValidateException(404, "Smoothing script '" +
                    ServerSettings.smoothingJarLocation + "' is missing!");
    }

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

        SmoothingAndStep sas = new SmoothingAndStep();
        int[] params = {windowSize, meanType, minPos, calcTotalMean, printPos};
        try {
            sas.smoothing(params, path, outputPath, stepSize);
        } catch (ProcessException e) {
            ErrorLogger.log("SMOOTHING", "ProcessException in runSmoothing. \n" + e.getMessage());
        }
    }
    
    /**
     * Simple wrapper around smoothing parameters.
     */
    protected class SmoothingParameters {
        private String path         = null;
        private int windowSize      = 10;
        private int meanType        = 1;
        private int minPos          = 0;
        private int calcTotalMean   = 0;
        private int printPos        = 0;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getWindowSize() {
            return windowSize;
        }

        public void setWindowSize(int windowSize) {
            this.windowSize = windowSize;
        }

        public int getMeanType() {
            return meanType;
        }

        public void setMeanType(int meanType) {
            this.meanType = meanType;
        }

        public int getMinPos() {
            return minPos;
        }

        public void setMinPos(int minPos) {
            this.minPos = minPos;
        }

        public int getCalcTotalMean() {
            return calcTotalMean;
        }

        public void setCalcTotalMean(int calcTotalMean) {
            this.calcTotalMean = calcTotalMean;
        }

        public int getPrintPos() {
            return printPos;
        }

        public void setPrintPos(int printPos) {
            this.printPos = printPos;
        }

        public void validateParameters() throws ValidateException {
            if (path == null)
                throw new ValidateException(400,
                        "No path was supplied to script!");
            if (!(meanType == 0 || meanType == 1))
                throw new ValidateException(400,
                        "Invalid option " + meanType +
                                " for meanType. Allowed is (1) or (0).");
            if (windowSize < 0)
                throw new ValidateException(400,
                        "Window size can not be negative.");
            if (minPos < 0)
                throw new ValidateException(400,
                        "Minimum pos can not be negative.");
            if (!(calcTotalMean == 0 || calcTotalMean == 1))
                throw new ValidateException(400,
                        "Invalid option " + calcTotalMean +
                                " for calcTotalMean. Allowed is (1) or (0).");
            if (!(printPos == 0 || printPos == 1))
                throw new ValidateException(400,
                        "Invalid option " + printPos +
                                " for printPos. Allowed is (1) or (0).");
            Debug.log("Validated with parameters: " + " " +
                      "Path: " + path + " " +
                      "MeanType: " + meanType + " " +
                      "WindowSize: " + windowSize + " " +
                      "MinPos: " + minPos + " " +
                      "CalcTotalMean: " + calcTotalMean + " " +
                      "PrintPos: " + printPos);
        }

    }
}
