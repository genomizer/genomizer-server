package process.test;

import command.ValidateException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import process.Ratio;
import process.Step;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * File:        RatioCalcTest.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-28
 */

public class RatioCalcTest {

    private static final String INFILE_1 =
            "resources/ratioCalcTestData/ratioInfile1.sgr";
    private static final String INFILE_2 =
            "resources/ratioCalcTestData/ratioInfile2.sgr";
    private static final String NO_SUCH_FILE =
            "resources/ratioCalcTestData/noSuchFile.sgr";
    private static final String OUTFILE =
            "resources/ratioCalcTestData/out/ratioOutfile.sgr";
    private static final String OUTFILE_PATH =
            "resources/ratioCalcTestData/out/";
    private static final Ratio.Mean MEAN = Ratio.Mean.SINGLE;
    private static final int READS_CUT_OFF = 0; // TODO check
    private static final String NO_CHROMOSOMES = "";
    private static final String CHROMOSOMES = "chr1,chr2";
    private static final String BAD_CHROMOSOMES = "";
    private static final String CORRECT_FILE =
            "resources/ratioCalcTestData/correct/correctOutfile.sgr";

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Before
    public void setUp() throws Exception {
        new File(OUTFILE_PATH).mkdirs();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @After
    public void tearDown() throws Exception {
        new File(OUTFILE).delete();
    }

    @Test
    public void shouldNotThrowValidationExceptionNoChroms() throws Exception {
        new Ratio(
                INFILE_1,
                INFILE_2,
                OUTFILE,
                MEAN,
                READS_CUT_OFF,
                NO_CHROMOSOMES).validate();
    }

    @Test
    public void shouldNotThrowValidationExceptionWithChroms() throws Exception {
        new Ratio(INFILE_1, INFILE_2, OUTFILE, MEAN, READS_CUT_OFF, CHROMOSOMES)
                .validate();
    }

    @Test(expected = ValidateException.class)
    public void shouldThrowValidationExceptionInfile1Null() throws Exception {
        new Ratio(null, INFILE_2, OUTFILE, MEAN, READS_CUT_OFF, CHROMOSOMES)
                .validate();
    }

    @Test(expected = ValidateException.class)
    public void shouldThrowValidationExceptionInfile2Null() throws Exception {
        new Ratio(INFILE_1, null, OUTFILE, MEAN, READS_CUT_OFF, CHROMOSOMES)
                .validate();
    }

    @Test(expected = ValidateException.class)
    public void shouldThrowValidationExceptionOutfileNull() throws Exception {
        new Ratio(INFILE_1, INFILE_2, null, MEAN, READS_CUT_OFF, CHROMOSOMES)
                .validate();
    }

    @Test(expected = ValidateException.class)
    public void shouldThrowValidationExceptionInfile1Missing()
            throws Exception {
        new Ratio(
                NO_SUCH_FILE,
                INFILE_2,
                OUTFILE,
                MEAN,
                READS_CUT_OFF,
                CHROMOSOMES)
                .validate();
    }

    @Test(expected = ValidateException.class)
    public void shouldThrowValidationExceptionInfile2Missing()
            throws Exception {
        new Ratio(
                INFILE_1,
                NO_SUCH_FILE,
                OUTFILE,
                MEAN,
                READS_CUT_OFF,
                CHROMOSOMES)
                .validate();
    }

    @Test(expected = ValidateException.class)
    public void shouldThrowValidationExceptionOutfileExists() throws Exception {
        new Ratio(
                INFILE_1,
                INFILE_2,
                INFILE_2,
                MEAN,
                READS_CUT_OFF,
                CHROMOSOMES)
                .validate();
    }

    @Test(expected = ValidateException.class)
    public void shouldThrowValidationExceptionMeanIsNull() throws Exception {
        new Ratio(INFILE_1, INFILE_2, OUTFILE, null, READS_CUT_OFF, CHROMOSOMES)
                .validate();
    }

    @Test(expected = ValidateException.class)
    public void shouldThrowValidationExceptionChromsNull() throws Exception {
        new Ratio(INFILE_1, INFILE_2, OUTFILE, MEAN, READS_CUT_OFF, null)
                .validate();
    }

    /**
     * TODO check constraints for chromosomes
     */
    @Test(expected = ValidateException.class)
    @Ignore
    public void shouldThrowValidationExceptionInvalidChroms() throws Exception {
        new Ratio(
                INFILE_1,
                INFILE_2,
                OUTFILE,
                MEAN,
                READS_CUT_OFF,
                BAD_CHROMOSOMES)
                .validate();
    }

    /**
     * TODO check constraints for cutOff
     */
    @Test(expected = ValidateException.class)
    @Ignore
    public void shouldThrowValidationExceptionInvalidCutOff() throws Exception {
        new Ratio(INFILE_1, INFILE_2, OUTFILE, MEAN, READS_CUT_OFF, CHROMOSOMES)
                .validate();
    }

    @Test
    @Ignore
    public void shouldProduceOutfile() throws Exception {
        new Ratio(
                INFILE_1,
                INFILE_2,
                OUTFILE,
                MEAN,
                READS_CUT_OFF,
                CHROMOSOMES);
        assertTrue(new File(OUTFILE).exists());
    }

    @Test
    @Ignore
    public void shouldProduceCorrectFile() throws Exception {
        new Ratio(
                INFILE_1,
                INFILE_2,
                OUTFILE,
                MEAN,
                READS_CUT_OFF,
                CHROMOSOMES);
        assertTrue(
                FileUtils.contentEquals(
                        new File(OUTFILE),
                        new File(CORRECT_FILE)));
    }
}
