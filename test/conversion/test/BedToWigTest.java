package conversion.test;

import conversion.ProfileDataConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertTrue;

/**
 * Tests for conversion from .bed to .wig
 * Created 2015-04-29.
 *
 * @author Albin Råstander <c12arr>
 * @author Martin Larsson <dv13mln>
 */
public class BedToWigTest {
    private final String outputPath = "resources/conversionTestData/output/";
    private final String expectedResultPath = "resources/conversionTestData/" +
            "expectedResults/";
    private File outputFile;
    private ConversionResultCompare cmp = new ConversionResultCompare();
    private ProfileDataConverter pdc;

    @Before
    public void setUp() {
        pdc = new ProfileDataConverter("resources/conversionTestData/output/");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException() {
        pdc = new ProfileDataConverter("resources/nonexistent");
    }


    /**
     * Tests null argument for input file
     * @throws java.io.FileNotFoundException
     */
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfInputFileIsNull()
            throws FileNotFoundException {
        pdc.bedToWig(null);
    }


    /**
     * Test for file not found-exception
     * @throws FileNotFoundException
     */
    @Test (expected = FileNotFoundException.class)
    public void shouldThrowFileNotFoundIfInputPathIsntAFile()
            throws FileNotFoundException{
        pdc.bedToWig("hej");
    }

    /**
     * Tests that exception is thrown when input file is of wrong type
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAcceptWrongFileTypeForInput()
            throws FileNotFoundException {
        pdc.bedToWig("resources/conversionTestData/SGR-testdata.sgr");
    }


    /**
     * Tests that output exists after conversion
     * @throws FileNotFoundException
     */
    @Test
    public void shouldExsistAnOutputFileAfterConversion()
            throws FileNotFoundException {
        String output;
        output = pdc.bedToWig("resources/conversionTestData/BED-testdata.bed");

        outputFile = new File(output);

        assertTrue(outputFile.exists());

    }

    /**
     * Tests that the converted files checksum equals the expected checksum
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void bedToWigCheckSumTest() throws InterruptedException,
            IOException {

        String output;
        output = pdc.bedToWig("resources/conversionTestData/BED-testdata.bed");
        File expectedFile;

        try{
            outputFile = new File(output);
            expectedFile = new File(expectedResultPath+"bed2wigResult.wig");
            assertTrue(cmp.compareFiles(outputFile, expectedFile));
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete output after each test
     */
    @After
    public void deleteOutPutFile() {
        if(outputFile != null) {
            if(outputFile.exists()) {
                outputFile.delete();
            }
        }
    }
}
