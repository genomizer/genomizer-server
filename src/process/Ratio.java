package process;

import command.ValidateException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * File:        Ratio.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-28
 */

public class Ratio extends Executor {

    private final String infile1;
    private final String infile2;
    private final String outfileName;
    private final Mean mean;
    private final int readsCutOff;
    private final String chromosomes;

    public Ratio(
            String infile1,
            String infile2,
            String outfileName,
            Mean mean,
            int readsCutOff,
            String chromosomes) {
        this.infile1 = infile1;
        this.infile2 = infile2;
        this.outfileName = outfileName;
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
        if (outfileName == null) {
            throw new ValidateException(0, "Specify outfileName");
        }
        if (!new File(infile1).isFile()) {
            throw new ValidateException(0, "Infile 1 doesn't exist");
        }
        if (!new File(infile2).isFile()) {
            throw new ValidateException(0, "Infile 2 doesn't exist");
        }
        if (new File(outfileName).exists()) {
            throw new ValidateException(0, "Outfile already exists");
        }
        if (mean == null) {
            throw new ValidateException(0, "Specify mean (single or double)");
        }
        if (chromosomes == null) {
            throw new ValidateException(0, "Specify chromosomes");
        }
        return this;
    }

    public String execute() throws IOException, InterruptedException {

        System.out.println("outfileName = " + outfileName);
        File profileFilesDir = new File(infile1).getParentFile();
        System.out.println("profileFilesDir = " + profileFilesDir.getAbsolutePath());
        File workingDir = new File(
                "resources/ratioCalcTestData/workingDir-ratio-" +
                infile1.replaceAll("[_/]", ""));
        File resultsDir = new File(workingDir.getAbsolutePath() + "/ratios");
        File ratioWrapper = new File("resources/ratio/ratio_calc_wrapper.sh");

        String output;
        try {
            output = executeCommand(
                    new String[]{
                            "bash",
                            ratioWrapper.getAbsolutePath(),
                            workingDir.getAbsolutePath() + "/",
                            infile1,
                            infile2,
                            outfileName,
                            mean.meanParam,
                            "" + readsCutOff,
                            chromosomes
                    });
        } catch (RuntimeException rte) {
            rte.printStackTrace();
            throw rte;
        }

        for (File outFile : resultsDir.listFiles()) {
            System.out.println("outFile = " + outFile.getAbsolutePath());
            if (outFile.getName().contains("smooth")) {
                File movedFile = new File(outfileName);
                System.out.println("Moving " + outFile.getAbsolutePath() + " to " + movedFile.getAbsolutePath());
                outFile.renameTo(movedFile);
            }
        }

        System.out.println("deleting working dir: " + workingDir.getAbsolutePath());
        FileUtils.deleteDirectory(workingDir);

        return output;
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
        SINGLE("single"),
        DOUBLE("single");

        public final String meanParam;

        Mean(String meanParam) {
            this.meanParam = meanParam;
        }

        public static Mean getMean(String mean) throws ValidateException {
            switch (mean) {
                case "single":
                    return SINGLE;
                case "double":
                    return DOUBLE;
                default:
                    throw new ValidateException(
                            0,
                            "Invalid mean setting: " + mean);
            }
        }
    }
}
