package conversion.test;

import conversion.ProfileDataConverter;
import org.junit.After;
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

    /**
     * Tests null argument for input file
     * @throws java.io.FileNotFoundException
     */
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfInputFileIsNull()
            throws FileNotFoundException {
        ProfileDataConverter.bedToWig(null, "resources/conversionTestData/" +
                "expectedResults/sgr2wigResult.wig");
    }

    /**
     * Tests null argument for output file
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfOutputFileIsNull()
            throws FileNotFoundException{
        ProfileDataConverter.bedToWig("resources/conversionTestData/BED-testdata.bed",
                null);
    }

    /**
     * Test for file not found-exception
     * @throws FileNotFoundException
     */
    @Test (expected = FileNotFoundException.class)
    public void shouldThrowFileNotFoundIfInputPathIsntAFile()
            throws FileNotFoundException{
        ProfileDataConverter.bedToWig("hej", "hej");
    }

    /**
     * Tests for illegal argument when output already exists
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfOutputPathIsAFile()
            throws FileNotFoundException{
        ProfileDataConverter.bedToWig("resources/conversionTestData/BED-testdata.bed",
                "resources/conversionTestData/WIG-testdata.wig");
    }

    /**
     * Tests that exception is thrown when input file is of wrong type
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAcceptWrongFileTypeForInput()
            throws FileNotFoundException {
        ProfileDataConverter.bedToWig("resources/conversionTestData/SGR-testdata.sgr",
                "resources/conversionTestData/output/test.wig");
    }

    /**
     * Tests that exception is thrown when output file is of wrong type
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAcceptWrongFileTypeForOutput()
            throws FileNotFoundException {
        ProfileDataConverter.bedToWig("resources/conversionTestData/BED-testdata.bed",
                "resources/conversionTestData/output/test.sgr");
    }

    /**
     * Tests that output exists after conversion
     * @throws FileNotFoundException
     */
    @Test
    public void shouldExsistAnOutputFileAfterConversion()
            throws FileNotFoundException {
        ProfileDataConverter.bedToWig("resources/conversionTestData/BED-testdata.bed",
                "resources/conversionTestData/output/test.wig");

        outputFile = new File("resources/conversionTestData/output/test.wig");

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
        ProfileDataConverter.bedToWig("resources/conversionTestData/BED-testdata.bed",
                "resources/conversionTestData/output/test.wig");
        File expectedFile;

        try{
            outputFile = new File(outputPath+"test.wig");
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
