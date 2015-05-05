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
 * Created by dv13mln on 2015-04-30.
 */
public class wigFixedStepToSgrTest {

    private final String outputPath = "resources/conversionTestData/output/";
    private final String expectedResultPath = "resources/conversionTestData/" +
            "expectedResults/";
    private File outputFile;
    private ConversionResultCompare cmp = new ConversionResultCompare();
    private ProfileDataConverter pdc;
    @Before
    public void setUp() {
        pdc = new ProfileDataConverter();
    }

    /**
     * Tests null argument for input file
     * @throws java.io.FileNotFoundException
     */
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfInputFileIsNull()
            throws IOException {
        pdc.wigToSgr("fixedStep", null);
    }


    /**
     * Test for file not found-exception
     * @throws FileNotFoundException
     */
    @Test (expected = FileNotFoundException.class)
    public void shouldThrowFileNotFoundIfInputPathIsntAFile()
            throws IOException {
        pdc.wigToSgr("fixedStep", "hej");
    }

    /**
     * Tests that exception is thrown when input file is of wrong type
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAcceptWrongFileTypeForInput()
            throws IOException {
        pdc.wigToSgr("fixedStep",
                "resources/conversionTestData/BED-testdata.bed");
    }

    /**
     * Tests that output exists after conversion
     * @throws FileNotFoundException
     */
    @Test
    public void shouldExistAnOutputFileAfterConversion()
            throws IOException {
        String output = pdc.wigToSgr("fixedStep",
                "resources/conversionTestData/WIG-testdata.wig");
        outputFile = new File(output);

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
        String output = pdc.wigToSgr("fixedStep",
                "resources/conversionTestData/WIG-testdata.wig");
        File expectedFile;

        try{
            outputFile = new File(output);
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
