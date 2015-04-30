package conversion.test;

/**
 * Tests for conversion from .wig of subtype bed to .sgr
 * Created 2015-04-29.
 *
 * @author Albin Råstander <c12arr@cs.umu.se>
 * @author Martin Larsson <dv13mln@cs.umu.se>
 */

import conversion.Converter;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class BedToSgrTest {
    private final String outputPath = "resources/conversionTestData/output/";
    private final String expectedResultPath = "resources/conversionTestData/" +
            "expectedResults/";
    private File outputFile;
    private ConversionResultCompare cmp = new ConversionResultCompare();


    /**
     * Tests null argument for input file
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfInputFileIsNull()
            throws FileNotFoundException{
        Converter.bedToSgr(null, "resources/conversionTestData/" +
                "expectedResults/gff2sgrResult.sgr");
    }

    /**
     * Tests null argument for output file
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfOutputFileIsNull()
            throws FileNotFoundException{
        Converter.bedToSgr("resources/conversionTestData/BED-testdata.bed",
                null);
    }

    /**
     * Test for file not found-exception
     * @throws FileNotFoundException
     */
    @Test (expected = FileNotFoundException.class)
    public void shouldThrowFileNotFoundIfInputPathIsntAFile()
            throws FileNotFoundException{
        Converter.bedToSgr("hej", "hej");
    }

    /**
     * Tests for illegal argument when output already exists
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfOutputPathIsAFile()
            throws FileNotFoundException{
        Converter.bedToSgr("resources/conversionTestData/BED-testdata.bed",
                "resources/conversionTestData/BED-testdata.bed");
    }

    /**
     * Tests that exception is thrown when input file is of wrong type
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAcceptWrongFileTypeForInput()
            throws FileNotFoundException {
        Converter.gffToSgr("resources/conversionTestData/GFF-testdata.gff",
                "resources/conversionTestData/SGR-testdata-4.sgr");
    }

    /**
     * Tests that exception is thrown when output file is of wrong type
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAcceptWrongFileTypeForOutput()
            throws FileNotFoundException {
        Converter.gffToSgr("resources/conversionTestData/BED-testdata.bed",
                "resources/conversionTestData/output/test.gff");
    }

    /**
     * Tests that output exists after conversion
     * @throws FileNotFoundException
     */
    @Test
    public void shouldExsistAoutputFileAfterConversion()
            throws FileNotFoundException {
        Converter.bedToSgr("resources/conversionTestData/BED-testdata.bed",
                "resources/conversionTestData/output/test.sgr");

        outputFile = new File("resources/conversionTestData/output/test.sgr");

        assertTrue(outputFile.exists());

    }

    /**
     * Tests that the converted files checksum equals the expected checksum
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void bedToSgrCheckSumTest() throws InterruptedException,IOException {
        Converter.bedToSgr("resources/conversionTestData/BED-testdata.bed",
                "resources/conversionTestData/output/test.sgr");
        File expectedFile;

        try{
            outputFile = new File(outputPath+"test.sgr");
            expectedFile = new File(expectedResultPath+"bed2sgrResult.sgr");

            assertTrue(cmp.compareFiles(outputFile, expectedFile));
        } catch (NullPointerException | NoSuchAlgorithmException e) {
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
