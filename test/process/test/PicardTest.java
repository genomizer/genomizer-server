package process.test;

import command.ValidateException;
import org.junit.*;
import process.Picard;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * TODO class description goes here...
 */

public class PicardTest {

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionOnNullFilenameRemoveDuplicates()
            throws ValidateException, IOException, InterruptedException {
        Picard.runRemoveDuplicates(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionOnNullFilenameSortSam()
            throws ValidateException, IOException, InterruptedException {
        Picard.runSortSam(null, null);
    }

    @Test
    public void shouldRunSortSamCorrectly()
            throws ValidateException, IOException, InterruptedException {
        Picard picard = new Picard("SortSam",
                "resources/processTest/sam/test.sam", "/dev/null",
                ".sam", ".sam",
                new String[]{"SO=coordinate"});

        picard.validate();
        assertNotNull(picard.execute());

    }

    @Test
    public void shouldMarkDuplicatesCorrectly()
            throws ValidateException, IOException, InterruptedException {
        Picard picard = new Picard("MarkDuplicates",
                "resources/processTest/sam/test_sorted.sam", "/dev/null",
                ".sam", ".sam",
                new String[]{"REMOVE_DUPLICATES=true",
                             "METRICS_FILE=/dev/null"});

        picard.validate();
        assertNotNull(picard.execute());

    }

    @Test
    public void shouldMarkDuplicatesFromStaticCall()
            throws ValidateException, IOException, InterruptedException {
        assertNotNull(Picard.runRemoveDuplicates(
                "resources/processTest/sam/test_sorted.sam", "/dev/null")
        );
    }

    @Test
    public void shouldSortSamFromStaticCall()
            throws ValidateException, IOException, InterruptedException {
        assertNotNull(
                Picard.runSortSam("resources/processTest/sam/test.sam",
                        "/dev/null")
        );
    }

    @Test(expected = ValidateException.class)
    public void shouldNotRunUnsupportedCommand() throws ValidateException {
        Picard picard = new Picard("ViewSam",
                "resources/processTest/res.sam", "/dev/null", ".sam",".sam",
                new String[]{""});
        picard.validate();
    }

    @Test
    public void shouldProduceFileFromStaticMarkDuplicates()
            throws ValidateException, IOException, InterruptedException {
        String markDupResultFile = "resources/processTest/" +
                                   "test_sorted_without_duplicates.sam";
        Picard.runRemoveDuplicates(
                        "resources/processTest/sam/test_sorted.sam",
                        markDupResultFile);
        assertTrue((new File(markDupResultFile)).exists());
    }

    @Test
    public void shouldProduceFileFromStaticSortSam()
            throws ValidateException, IOException, InterruptedException {
        String sortResultFile = "resources/processTest/test_sorted.sam";
        Picard.runSortSam(
                "resources/processTest/sam/test.sam", sortResultFile);
        assertTrue((new File(sortResultFile)).exists());

    }

}
