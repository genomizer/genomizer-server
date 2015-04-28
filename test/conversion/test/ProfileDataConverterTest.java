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

/* TODO None of these tests check the actual conversion but only tests if
 * it is possible to run scripts. Should be extended with tests of actual
  * results but real data is needed as reference. */
@Ignore
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
		ch.executeProfileDataConversion("sgrToBed", "conversionTestData/GSM604730_CB_DmS2DRSC_Trx_b.dm3_rep2_TRUNCATEDFORTEST.sgr","conversionTestData/output/bed1.bed", "Unknown");
	}
*/
	@Test
	public void bedToSgrtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("bedToSgr", "conversionTestData/Sg4_TRX-CP_Ave.resRto_Rel5_TRUNCATEDFORTEST.bed","conversionTestData/output/sgr1.sgr", null);
	}

	@Test
	public void gff3ToSgrtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("gff3ToSgr", "conversionTestData/TP_SG_A_ZeroLvl_Release5_TRUNCATEDFORTEST.gff", "conversionTestData/output/sgr2.sgr", null);
	}

/*	@Test
	public void wigToSgrtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("wigToSgr", "conversionTestData/GSM604730_CB_DmS2DRSC_Trx_b.dm3_rep2_TRUNCATEDFORTEST.wig", "conversionTestData/output/sgr3.sgr", null);
	}
*/
	/*@Test
	public void wigToBedtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("wigToBed", "conversionTestData/GSM604730_CB_DmS2DRSC_Trx_b.dm3_rep2_TRUNCATEDFORTEST.wig", "conversionTestData/output/bed2.bed", "Unknown");
	}*/

/*	@Test
	public void gff3ToBedtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("gff3ToBed", "conversionTestData/TP_SG_A_ZeroLvl_Release5_TRUNCATEDFORTEST.gff", "conversionTestData/output/bed3.bed", "Unknown");
	}*/

	@Test
	public void gff3ToWigtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("gff3ToWig", "conversionTestData/TP_SG_A_ZeroLvl_Release5_TRUNCATEDFORTEST.gff", "conversionTestData/output/gff2wigtest.wig", "Unknown");
	}
	@Test
	public void sgrToWigTestFileExists() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("sgrToWig", "conversionTestData/Sg4_TRX-CP_Ave.resRto_Rel5_TRUNCATEDFORTEST.sgr", "conversionTestData/output/sgr2wigtest.wig", "Unknown");
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
		ch.executeProfileDataConversion("sgrToWig", "conversionTestData/GSM604730_CB_DmS2DRSC_Trx_b.dm3_rep2_TRUNCATEDFORTEST.sgr", "conversionTestData/output/sgr2wigtest.wig", "Unknown");
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
		ch.executeProfileDataConversion("bedToWig", "conversionTestData/Sg4_TRX-CP_Ave.resRto_Rel5_TRUNCATEDFORTEST.bed", "conversionTestData/output/bed2wigtest.wig", "Unknown");
	}
	@Test(expected=IllegalArgumentException.class)
	public void nulltest1() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("sgrToBed", "conversionTestData/GSM604730_CB_DmS2DRSC_Trx_b.dm3_rep2_TRUNCATEDFORTEST.sgr",null, "Unknown");
	}
	@Test(expected=IllegalArgumentException.class)
	public void nulltest2() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("sgrToBed", null,"conversionTestData/output/bed1.bed", "Unknown");
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
		ch.executeProfileDataConversion("gff3ToBed", "conversionTestData/TP_SG_A_ZeroLvl_Release5_TRUNCATEDFORTEST.gff", "conversionTestData/output", "Unknown");
	}

*/
}