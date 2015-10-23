package process;
/**
 * File:        Step.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-27
 */

import command.ValidateException;
import server.ServerSettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Step extends Executor {

    private final String infile;
    private final String outfile;
    private final int stepSize;

    public Step(String infile, String outfile, int stepSize) {
        this.infile = infile;
        this.outfile = outfile;
        this.stepSize = stepSize;
    }

    public Step validate() throws ValidateException {

        if (infile == null) {
            throw new ValidateException(0, "Specify infile.");
        }
        if (outfile == null) {
            throw new ValidateException(0, "Specify outfile.");
        }
        if (!new File(infile).isFile()) {
            throw new ValidateException(0, "Infile doesn't exist.");
        }
        if (new File(outfile).isFile()) {
            throw new ValidateException(0, "Outfile already exists.");
        }
        if (stepSize < 1) {
            throw new ValidateException(
                    0,
                    "Step size must be a positive integer");
        }
        return this;
    }

    public String execute() throws IOException, InterruptedException {
        ArrayList<String> args = new ArrayList<>();
        args.add("java");
        args.add("-jar");
        args.add(ServerSettings.smoothingJarLocation);

        args.add("3");
        args.add("0");
        args.add("1");
        args.add("0");
        args.add("0");
        args.add(infile);
        args.add(outfile);
        args.add(String.valueOf(stepSize));
        return executeCommand(args.toArray(new String[args.size()]));
    }

    public static String runStep(String infile, String outfile, int stepSize)
            throws IOException, InterruptedException, ValidateException {

        return new Step(infile, outfile, stepSize).validate().execute();
    }

    @Override
    public String toString() {
        return "Step{" +
               "infile='" + infile + '\'' +
               ", outfile='" + outfile + '\'' +
               ", stepSize=" + stepSize +
               '}';
    }
}
