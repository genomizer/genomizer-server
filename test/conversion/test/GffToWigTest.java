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
 * Created 2015-04-29.
 *
 * @author Albin Råstander <c12arr@cs.umu.se>
 * @author Martin Larsson <dv13mln@cs.umu.se>
 */
public class GffToWigTest {
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
        pdc.gffToWig(null);
    }


    /**
     * Test for file not found-exception
     * @throws FileNotFoundException
     */
    @Test (expected = FileNotFoundException.class)
    public void shouldThrowFileNotFoundIfInputPathIsntAFile()
            throws IOException {
        pdc.gffToWig("hej");
    }

    /**
     * Tests that exception is thrown when input file is of wrong type
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAcceptWrongFileTypeForInput()
            throws IOException {
        pdc.gffToWig("resources/conversionTestData/BED-testdata.bed");
    }

    /**
     * Tests that output exists after conversion
     * @throws FileNotFoundException
     */
    @Test
    public void shouldExsistAnOutputFileAfterConversion()
            throws IOException {
        String output;
        output = pdc.gffToWig("resources/conversionTestData/GFF-testdata.gff");

        outputFile = new File(output);

        assertTrue(outputFile.exists());

    }

    /**
     * Tests that the converted files checksum equals the expected checksum
     * @throws InterruptedException
     * @throws java.io.IOException
     */
    @Test
    public void gffToWigCheckSumTest() throws InterruptedException,
            IOException {
        String output;
        output = pdc.gffToWig("resources/conversionTestData/GFF-testdata.gff");
        File expectedFile;

        try{
            outputFile = new File(output);
            expectedFile = new File(expectedResultPath+"gff2wigResult.wig");
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
