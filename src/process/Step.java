package process;
/**
 * File:        Step.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-27
 */

import command.ValidateException;

import java.io.File;
import java.io.IOException;

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

        return executeCommand(
                new String[]{
                        "perl",
                        "resources/step.pl",
                        "" + stepSize,
                        infile,
                        outfile
                });
    }

    public static String runStep(String infile, String outfile, int stepSize)
            throws IOException, InterruptedException, ValidateException {

        return new Step(infile, outfile, stepSize).validate().execute();
    }
}
