package conversion.test;

import conversion.Converter;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertTrue;

/**
 * Created by dv13mln on 2015-04-30.
 */
public class wigFixedStepToSgrTest {

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
        Converter.wigToSgr("fixedStep", null,
                "resources/conversionTestData/expectedResults/wigbedToSgr.sgr");
    }

    /**
     * Tests null argument for output file
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfOutputFileIsNull()
            throws FileNotFoundException{
        Converter.wigToSgr("fixedStep",
                "resources/conversionTestData/WIG-testdata.wig", null);
    }

    /**
     * Test for file not found-exception
     * @throws FileNotFoundException
     */
    @Test (expected = FileNotFoundException.class)
    public void shouldThrowFileNotFoundIfInputPathIsntAFile()
            throws FileNotFoundException{
        Converter.wigToSgr("fixedStep", "hej", "hej");
    }

    /**
     * Tests for illegal argument when output already exists
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfOutputPathIsAFile()
            throws FileNotFoundException{
        Converter.wigToSgr("fixedStep",
                "resources/conversionTestData/WIG-testdata.wig",
                "resources/conversionTestData/expectedResults/bed2sgrResult.sgr");
    }

    /**
     * Tests that exception is thrown when input file is of wrong type
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAcceptWrongFileTypeForInput()
            throws FileNotFoundException {
        Converter.wigToSgr("fixedStep",
                "resources/conversionTestData/BED-testdata.bed",
                "resources/conversionTestData/output/test.sgr");
    }

    /**
     * Tests that exception is thrown when output file is of wrong type
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAcceptWrongFileTypeForOutput()
            throws FileNotFoundException {
        Converter.wigToSgr("fixedStep",
                "resources/conversionTestData/WIG-testdata.wig",
                "resources/conversionTestData/output/test.bed");
    }

    /**
     * Tests that output exists after conversion
     * @throws FileNotFoundException
     */
    @Test
    public void shouldExistAnOutputFileAfterConversion()
            throws FileNotFoundException {
        Converter.wigToSgr("fixedStep",
                "resources/conversionTestData/WIG-testdata.wig",
                "resources/conversionTestData/output/test.sgr");
        outputFile = new File("resources/conversionTestData/output/test.sgr");

        assertTrue(outputFile.exists());
    }

    /**
     * Tests that the converted files checksum equals the expected checksum
     * @throws InterruptedException
     * @throws java.io.IOException
     */
    @Test
    public void wigbedToSgrCheckSumTest() throws InterruptedException,
            IOException {
        Converter.wigToSgr("fixedStep",
                "resources/conversionTestData/WIG-testdata.wig",
                "resources/conversionTestData/output/test.sgr");
        File expectedFile;

        try{
            outputFile = new File(outputPath+"test.sgr");
            expectedFile = new File("resources/conversionTestData/" +
                    "expectedResults/wigFixedStepToSgr.sgr");
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
