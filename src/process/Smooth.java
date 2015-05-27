package process;
/**
 *
 *
 */

import command.ValidateException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class Smooth extends Executor {
    private SmoothingParameters parameters;
    private String smoothingScriptCmd   = "expect smooth_v4.sh";
    private String smoothingScriptSh    = "smooth_v4.sh";
    private String smoothingScriptPerl  = "smooth_v4.pl";

    public Smooth(String path,
                  int    windowSize,
                  int    meanType,
                  int    minPos,
                  int    calcTotalMean,
                  int    printPos) {

        /* We insert all parameters we've received. */
        this.parameters = new SmoothingParameters();
        parameters.setPath(path);
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
        if (!new File(smoothingScriptSh).exists())
            throw new ValidateException(404, "Shell script " +
                    smoothingScriptSh + " is missing!");
        if (!new File(smoothingScriptPerl).exists())
            throw new ValidateException(404, "Shell script " +
                    smoothingScriptPerl + " is missing!");

    }

    public String execute() throws IOException, InterruptedException {
        ArrayList<String> args = new ArrayList<String>();

        /* Time to build our command */
        args.add(smoothingScriptCmd);
        args.add(String.valueOf(parameters.getWindowSize()));
        args.add(String.valueOf(parameters.getMeanType()));
        args.add(String.valueOf(parameters.getMinPos()));
        args.add(String.valueOf(parameters.getCalcTotalMean()));
        args.add(String.valueOf(parameters.getPrintPos()));

        return executeCommand(args.toArray(new String []{}));
    }

    public static void runSmoothing(String path, int windowSize, int meanType,
                                    int minPos, int calcTotalMean, int printPos)
            throws ValidateException,
                   IOException,
                   InterruptedException {

        Smooth smooth = new Smooth(path, windowSize, meanType, minPos,
                calcTotalMean, printPos);
        smooth.validate();
        smooth.execute();
    }

    private class SmoothingParameters {
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
                throw new ValidateException(404,
                        "No path was supplied to script!");
            if (!(meanType == 0 || meanType == 1))
                throw new ValidateException(404,
                        "Invalid option " + meanType +
                                " for meanType. Allowed is (1) or (0).");
            if (windowSize < 0)
                throw new ValidateException(404,
                        "Window size can not be negative.");
            if (minPos < 0)
                throw new ValidateException(404,
                        "Minimum pos can not be negative.");
            if (!(calcTotalMean == 0 || calcTotalMean == 1))
                throw new ValidateException(404,
                        "Invalid option " + calcTotalMean +
                                " for calcTotalMean. Allowed is (1) or (0).");
            if (!(printPos == 0 || printPos == 1))
                throw new ValidateException(404,
                        "Invalid option " + printPos +
                                " for printPos. Allowed is (1) or (0).");
        }

    }
}
