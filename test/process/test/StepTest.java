package process.test;

import command.ValidateException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import process.Step;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * File:        StepTest.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-27
 */

public class StepTest {

    public static final String INFILE =
            "resources/stepTestData/stepTestInfile.sgr";
    public static final String OUTFILE_PATH =
            "resources/stepTestData/out/";
    public static final String OUTFILE =
            "resources/stepTestData/out/stepTestOutfile.sgr";
    public static final String CORRECT_FILE =
            "resources/stepTestData/correct/correctStep20.sgr";
    public static final String NO_SUCH_FILE =
            "resources/stepTestData/noSuchFile.sgr";

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
    public void shouldNotThrowValidationException() throws Exception {
        new Step(INFILE, OUTFILE, 20).validate();
    }

    @Test(expected = ValidateException.class)
    public void shouldThrowValidationExceptionInfileNull() throws Exception {
        new Step(null, OUTFILE, 20).validate();
    }

    @Test(expected = ValidateException.class)
    public void shouldThrowValidationExceptionOutfileNull() throws Exception {
        new Step(INFILE, null, 20).validate();
    }

    @Test(expected = ValidateException.class)
    public void shouldThrowValidationExceptionNoInfile() throws Exception {
        new Step(NO_SUCH_FILE, OUTFILE, 20).validate();
    }

    @Test(expected = ValidateException.class)
    public void shouldThrowValidationExceptionOutfileExists() throws Exception {
        new Step(INFILE, INFILE, 20).validate();
    }

    @Test(expected = ValidateException.class)
    public void shouldThrowValidationExceptionInvalidStepSize()
            throws Exception {
        new Step(INFILE, OUTFILE, 0).validate();
    }

    @Test
    public void shouldProduceOutfile() throws Exception {
        new Step(INFILE, OUTFILE, 20).validate().execute();
        assertTrue(new File(OUTFILE).exists());
    }

    @Test
    public void shouldProduceCorrectFile() throws Exception {
        new Step(INFILE, OUTFILE, 20).validate().execute();
        assertTrue(
                FileUtils.contentEquals(
                        new File(OUTFILE),
                        new File(CORRECT_FILE)));
    }
}
