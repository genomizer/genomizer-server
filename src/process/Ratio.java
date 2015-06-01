package process;

import command.ValidateException;

import java.io.File;
import java.io.IOException;

/**
 * File:        Ratio.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-28
 */

public class Ratio extends Executor {

    private final String infile1;
    private final String infile2;
    private final String outfile;
    private final Mean mean;
    private final int readsCutOff;
    private final String chromosomes;

    public Ratio(
            String infile1,
            String infile2,
            String outfile,
            Mean mean,
            int readsCutOff,
            String chromosomes) {
        this.infile1 = infile1;
        this.infile2 = infile2;
        this.outfile = outfile;
        this.mean = mean;
        this.readsCutOff = readsCutOff;
        this.chromosomes = chromosomes;
    }

    public Ratio validate() throws ValidateException {
        if (infile1 == null) {
            throw new ValidateException(0, "Specify infile 1");
        }
        if (infile2 == null) {
            throw new ValidateException(0, "Specify infile 2");
        }
        if (outfile == null) {
            throw new ValidateException(0, "Specify outfile");
        }
        if (!new File(infile1).isFile()) {
            throw new ValidateException(0, "Infile 1 doesn't exist");
        }
        if (!new File(infile2).isFile()) {
            throw new ValidateException(0, "Infile 2 doesn't exist");
        }
        if (new File(outfile).exists()) {
            throw new ValidateException(0, "Outfile already exists");
        }
        if (mean == null) {
            throw new ValidateException(0, "Specify mean (single or double)");
        }
        return this;
    }

    public String execute() throws IOException, InterruptedException {
        return executeProgram(
                new String[]{

                });
    }

    public static String runRatio(
            String infile1,
            String infile2,
            String outfile,
            Mean mean,
            int readsCutOff,
            String chromosomes)
            throws IOException, InterruptedException, ValidateException {

        return new Ratio(
                infile1,
                infile2,
                outfile,
                mean,
                readsCutOff,
                chromosomes)
                .validate().execute();
    }

    public enum Mean {
        SINGLE,
        DOUBLE;

        public static Mean getMean(String mean) throws ValidateException {
            switch (mean) {
                case "single":
                    return SINGLE;
                case "double":
                    return DOUBLE;
                default:
                    throw new ValidateException(0,
                            "Invalid mean setting: " + mean);
            }
        }
    }
}
