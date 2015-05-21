package process.test;

import command.ValidateException;
import org.junit.*;
import process.Picard;
import server.ServerSettings;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

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
                "/Home/staff/dali/edu/5DV151/project/genomizer-server/resources/processTest/res.sam",
                "/Home/staff/dali/edu/5DV151/project/genomizer-server/resources/processTest/sorted.sam", ".sam", ".sam",
                new String[]{"SO=coordinate"});

        picard.validate();
        assertNotNull(picard.execute());

    }

    @Test
    public void shouldMarkDuplicatesCorrectly()
            throws ValidateException, IOException, InterruptedException {
        Picard picard = new Picard("MarkDuplicates",
                "/Home/staff/dali/edu/5DV151/project/genomizer-server/resources/processTest/sorted.sam",
                "/Home/staff/dali/edu/5DV151/project/genomizer-server/resources/processTest/marked.sam", ".sam", ".sam",
                new String[]{"REMOVE_DUPLICATES=true", "METRICS_FILE=/dev/null"});

        picard.validate();
        assertNotNull(picard.execute());

    }

    @Test
    public void shouldMarkDuplicatesFromStaticCall()
            throws ValidateException, IOException, InterruptedException {
        assertNotNull(Picard.runRemoveDuplicates(
                "/Home/staff/dali/edu/5DV151/project/genomizer-server/resources/processTest/sorted.sam",
                "/Home/staff/dali/edu/5DV151/project/genomizer-server/resources/processTest/marked.sam")
        );
    }

    @Test
    public void shouldSortSamFromStaticCall()
            throws ValidateException, IOException, InterruptedException {
        assertNotNull(
                Picard.runSortSam(
                        "/Home/staff/dali/edu/5DV151/project/genomizer-server" +
                        "/resources/processTest/res.sam",
                        "/Home/staff/dali/edu/5DV151/project/genomizer-server/resources/processTest/sorted.sam")
        );
    }

    @Test(expected = ValidateException.class)
    public void shouldNotRunUnsupportedCommand() throws ValidateException {
        Picard picard = new Picard("ViewSam", "/Home/staff/dali/edu/5DV151/project/genomizer-server/resources/processTest/res.sam",
                "/Home/staff/dali/edu/5DV151/project/genomizer-server/resources/processTest/marked.sam",
                ".sam",".sam",
                new String[]{""});
        picard.validate();
    }

}
