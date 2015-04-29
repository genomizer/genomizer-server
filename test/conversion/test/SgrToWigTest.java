package conversion.test;

import conversion.Converter;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * Created by c12arr on 2015-04-29.
 */
public class SgrToWigTest {
    private final String outputPath = "resources/conversionTestData/output/";
    private final String expectedResultPath = "resources/conversionTestData/expectedResults/";
    private File outputFile;


    /*
    *	Private method that uses checksum to compare two files.
    *	returns true if equal false otherwise.
     */
    private boolean compareFiles(File fileA, File fileB) throws IOException, NoSuchAlgorithmException {
        return (Arrays.equals(fileChecksum(fileA), fileChecksum(fileB)));
    }

    /*
    * 	Private method that calculates a checksum for a file, used to compare
    * 	files.
    *
    * 	returns the checksum in a byte array.
    * */
    private byte[] fileChecksum(File inputFile) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA1");

        FileInputStream fileStream = new FileInputStream(inputFile);

        byte[] dataBytes = new byte[1024];
        int nrOfLines;

        while((nrOfLines = fileStream.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nrOfLines);

        }

        return md.digest();
    }

    /**
     * Tests null argument for input file
     * @throws java.io.FileNotFoundException
     */
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfInputFileIsNull() throws FileNotFoundException {
        Converter.sgrToWig(null, "resources/conversionTestData/expectedResults/sgr2wigResult.wig");
    }

    /**
     * Tests null argument for output file
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfOutputFileIsNull() throws FileNotFoundException{
        Converter.sgrToWig("resources/conversionTestData/SGR-testdata.sgr", null);
    }

    /**
     * Test for file not found-exception
     * @throws FileNotFoundException
     */
    @Test (expected = FileNotFoundException.class)
    public void shouldThrowFileNotFoundIfInputPathIsntAFile() throws FileNotFoundException{
        Converter.sgrToWig("hej", "hej");
    }

    /**
     * Tests for illegal argument when output already exists
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfOutputPathIsAFile() throws FileNotFoundException{
        Converter.sgrToWig("resources/conversionTestData/SGR-testdata.sgr", "resources/conversionTestData/WIG-testdata.wig");
    }

    /**
     * Tests that exception is thrown when input file is of wrong type
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAcceptWrongFileTypeForInput() throws FileNotFoundException {
        Converter.sgrToWig("resources/conversionTestData/BED-testdata.bed", "resources/conversionTestData/output/test.wig");
    }

    /**
     * Tests that exception is thrown when output file is of wrong type
     * @throws FileNotFoundException
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAcceptWrongFileTypeForOutput() throws FileNotFoundException {
        Converter.sgrToWig("resources/conversionTestData/SGR-testdata.sgr", "resources/conversionTestData/output/test.bed");
    }


    /**
     * Tests that output exists after conversion
     * @throws FileNotFoundException
     */
    @Ignore
    public void shouldExsistAoutputFileAfterConversion() throws FileNotFoundException {
        Converter.sgrToWig("resources/conversionTestData/SGR-testdata.sgr", "resources/conversionTestData/output/test.wig");

        outputFile = new File("resources/conversionTestData/output/test.wig");

        assertTrue(outputFile.exists());

    }

    /**
     * Tests that the converted files checksum equals the expected checksum
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void sgrToWigCheckSumTest() throws InterruptedException ,IOException {
        Converter.sgrToWig("resources/conversionTestData/SGR-testdata.sgr", "resources/conversionTestData/output/test.wig");
        File expectedFile;

        try{
            outputFile = new File(outputPath+"test.wig");
            expectedFile = new File(expectedResultPath+"sgr2wigResult.wig");

            assertTrue(compareFiles(outputFile, expectedFile));
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
                //outputFile.delete();
            }
        }
    }
}
