package conversion.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import conversion.ConversionHandler;

import static org.junit.Assert.assertFalse;


 public class ProfileDataConverterTest {

	private final String outputPath = "resources/conversionTestData/output/";
	private final String expectedResultPath = "resources/conversionTestData/expectedResults/";



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

/*
	@Test
	public void sgrToBedtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("sgrToBed", "conversionTestData/SGR-testdata.sgr","conversionTestData/output/bed1.bed", "Unknown");
	}
*/
	@Test
	public void bedToSgrtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("bedToSgr", "conversionTestData/BED-testdata.bed","conversionTestData/output/sgr1.sgr", null);

		File outputFile;

		try{
			outputFile = new File(outputPath+"sgr1.sgr");
			assertTrue(outputFile.exists());
			outputFile.delete();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void bedToSgrCheckSumTest() throws InterruptedException ,IOException {

		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("bedToSgr", "conversionTestData/BED-testdata.bed","conversionTestData/output/sgr1.sgr", null);
		File testFile;
		File expectedFile;
		try{
			testFile = new File(outputPath+"sgr1.sgr");
			expectedFile = new File(expectedResultPath+"bed2sgrResult.sgr");

			assertTrue(compareFiles(testFile, expectedFile));

			testFile.delete();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void gff3ToSgrtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("gff3ToSgr", "conversionTestData/GFF-testdata.gff", "conversionTestData/output/sgr2.sgr", null);

		File outputFile;

		try{
			outputFile = new File(outputPath+"sgr2.sgr");
			assertTrue(outputFile.exists());
			outputFile.delete();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void gffToSgrCheckSumTest() throws InterruptedException ,IOException {

		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("gff3ToSgr", "conversionTestData/GFF-testdata.gff", "conversionTestData/output/sgr2.sgr", null);
		File testFile;
		File expectedFile;
		try{
			testFile = new File(outputPath+"sgr2.sgr");
			expectedFile = new File(expectedResultPath+"gff2sgrResult.sgr");

			assertTrue(compareFiles(testFile, expectedFile));

			testFile.delete();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

/*	@Test
	public void wigToSgrtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("wigToSgr", "conversionTestData/WIG-testdata.wig", "conversionTestData/output/sgr3.sgr", null);
	}
*/
	/*@Test
	public void wigToBedtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("wigToBed", "conversionTestData/WIG-testdata.wig", "conversionTestData/output/bed2.bed", "Unknown");
	}*/

/*	@Test
	public void gff3ToBedtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("gff3ToBed", "conversionTestData/GFF-testdata.gff", "conversionTestData/output/bed3.bed", "Unknown");
	}*/

	@Test
	public void gff3ToWigtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("gff3ToWig", "conversionTestData/GFF-testdata.gff", "conversionTestData/output/gff2wigtest.wig", null);
		File outputFile;

		try{
			outputFile = new File(outputPath+"gff2wigtest.wig");
			assertTrue(outputFile.exists());
			outputFile.delete();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void gff3ToWigCheckSumTest() throws InterruptedException ,IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("gff3ToWig", "conversionTestData/GFF-testdata.gff", "conversionTestData/output/gff2wigtest.wig", null);
		File testFile;
		File expectedFile;
		try{
			testFile = new File(outputPath+"gff2wigtest.wig");
			expectedFile = new File(expectedResultPath+"gff2wigResult.wig");

			assertTrue(compareFiles(testFile, expectedFile));

			testFile.delete();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void sgrToWigTestFileExists() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("sgrToWig", "conversionTestData/SGR-testdata-3.sgr", "conversionTestData/output/sgr2wigtest.wig", null);
		File outputFile;

		try{
			outputFile = new File(outputPath+"sgr2wigtest.wig");
			assertTrue(outputFile.exists());
			outputFile.delete();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void sgrToWigCheckSumTest() throws InterruptedException ,IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("sgrToWig", "conversionTestData/SGR-testdata.sgr", "conversionTestData/output/sgr2wigtest.wig", null);
		File testFile;
		File expectedFile;
		try{
			testFile = new File(outputPath+"sgr2wigtest.wig");
			expectedFile = new File(expectedResultPath+"sgr2wigResult.wig");

			assertTrue(compareFiles(testFile, expectedFile));

			testFile.delete();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void bedToWigtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("bedToWig", "conversionTestData/BED-testdata.bed", "conversionTestData/output/bed2wigtest.wig", null);
		File outputFile;

		try{
			outputFile = new File(outputPath+"bed2wigtest.wig");
			assertTrue(outputFile.exists());
			outputFile.delete();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void bedToWigCheckSumTest() throws InterruptedException ,IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("bedToWig", "conversionTestData/BED-testdata.bed", "conversionTestData/output/bed2wigtest.wig", null);
		File testFile;
		File expectedFile;
		try{
			testFile = new File(outputPath+"bed2wigtest.wig");
			expectedFile = new File(expectedResultPath+"bed2wigResult.wig");

			assertTrue(compareFiles(testFile, expectedFile));

			testFile.delete();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void nulltest1() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("sgrToBed", "conversionTestData/SGR-testdata.sgr",null, null);
	}
	@Test(expected=IllegalArgumentException.class)
	public void nulltest2() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("sgrToBed", null,"conversionTestData/output/bed1.bed", null);
	}
	@Test(expected=IllegalArgumentException.class)
	public void nulltest3() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion(null, null,null,null);
	}
/*
	@Test(expected=IllegalArgumentException.class)
	public void badPathTest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("gff3ToBed", "conversionTestData/GFF-testdata.gff", "conversionTestData/output", "Unknown");
	}

*/
}