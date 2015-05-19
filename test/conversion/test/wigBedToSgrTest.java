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
 * Tests for conversion from .wig of subtype bed to .sgr
 * Created 2015-04-30.
 *
 * @author Albin Råstander <c12arr@cs.umu.se>
 * @author Martin Larsson <dv13mln@cs.umu.se>
 */
public class wigBedToSgrTest {
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
        pdc.wigToSgr("bed", null);
    }

    /**
     * Test for file not found-exception
     * @throws FileNotFoundException
     */
    @Test (expected = FileNotFoundException.class)
    public void shouldThrowFileNotFoundIfInputPathIsntAFile()
            throws IOException {
        pdc.wigToSgr("bed", "hej");
    }

    /**
     * Tests that exception is thrown when input file is of wrong type
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAcceptWrongFileTypeForInput()
            throws IOException {
        pdc.wigToSgr("bed",
                "resources/conversionTestData/BED-testdata.bed");
    }


    /**
     * Tests that output exists after conversion
     * @throws FileNotFoundException
     */
    @Test
    public void shouldExistAnOutputFileAfterConversion()
            throws IOException {
        String output =pdc.wigToSgr("bed",
                "resources/conversionTestData/WIG-from-SGR-testdata.wig");
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
        String output = pdc.wigToSgr("bed",
                "resources/conversionTestData/WIG-from-SGR-testdata.wig");
        File expectedFile;

        try{
            outputFile = new File(output);
            expectedFile = new File("resources/conversionTestData/" +
                    "expectedResults/wigbedToSgr.sgr");
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
