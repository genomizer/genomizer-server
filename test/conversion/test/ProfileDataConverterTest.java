package conversion.test;

import java.io.IOException;

import org.junit.Test;

import conversion.ConversionHandler;


public class ProfileDataConverterTest {


	@Test
	public void sgrToBedtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("sgrToBed", "/home/c11/c11dhn/edu/PVT/scripts/neeuwmap/GSM604730_CB_DmS2DRSC_Trx_b.dm3_rep2.sgr","/home/c11/c11dhn/edu/PVT/DEEEP/bed1.bed", "Unknown");
	}

	@Test
	public void bedToSgrtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("bedToSgr", "/home/c11/c11dhn/edu/PVT/scripts/neeuwmap/Sg4_TRX-CP_Ave.resRto_Rel5.bed","/home/c11/c11dhn/edu/PVT/DEEEP/sgr1.sgr", null);
	}

	@Test
	public void gff3ToSgrtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("gff3ToSgr", "/home/c11/c11dhn/edu/PVT/scripts/neeuwmap/TP_SG_A_ZeroLvl_Release5.gff", "/home/c11/c11dhn/edu/PVT/DEEEP/sgr2.sgr", null);
	}

	@Test
	public void wigToSgrtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("wigToSgr", "/home/c11/c11dhn/edu/PVT/scripts/neeuwmap/GSM604730_CB_DmS2DRSC_Trx_b.dm3_rep2.wig", "/home/c11/c11dhn/edu/PVT/DEEEP/sgr3.sgr", null);
	}
	@Test
	public void wigToBedtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("wigToBed", "/home/c11/c11dhn/edu/PVT/scripts/neeuwmap/GSM604730_CB_DmS2DRSC_Trx_b.dm3_rep2.wig", "/home/c11/c11dhn/edu/PVT/DEEEP/bed2.bed", "Unknown");
	}
	@Test
	public void gff3ToBedtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("gff3ToBed", "/home/c11/c11dhn/edu/PVT/scripts/neeuwmap/TP_SG_A_ZeroLvl_Release5.gff", "/home/c11/c11dhn/edu/PVT/DEEEP/bed3.bed", "Unknown");
	}
	@Test
	public void gff3ToWigtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("gff3ToWig", "/home/c11/c11dhn/edu/PVT/scripts/neeuwmap/TP_SG_A_ZeroLvl_Release5.gff", "/home/c11/c11dhn/edu/PVT/DEEEP/gff2wigtest.wig", "Unknown");
	}
	@Test
	public void sgrToWigtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("sgrToWig", "/home/c11/c11dhn/edu/PVT/scripts/neeuwmap/Sg4_TRX-CP_Ave.resRto_Rel5.sgr", "/home/c11/c11dhn/edu/PVT/DEEEP/sgr2wigtest.wig", "Unknown");
	}
	@Test
	public void bedToWigtest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("bedToWig", "/home/c11/c11dhn/edu/PVT/scripts/neeuwmap/Sg4_TRX-CP_Ave.resRto_Rel5.bed", "/home/c11/c11dhn/edu/PVT/DEEEP/bed2wigtest.wig", "Unknown");
	}
	@Test(expected=IllegalArgumentException.class)
	public void nulltest1() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("sgrToBed", "/home/c11/c11dhn/edu/PVT/scripts/neeuwmap/GSM604730_CB_DmS2DRSC_Trx_b.dm3_rep2.sgr",null, "Unknown");
	}
	@Test(expected=IllegalArgumentException.class)
	public void nulltest2() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("sgrToBed", null,"/home/c11/c11dhn/edu/PVT/DEEEP/bed1.bed", "Unknown");
	}
	@Test(expected=IllegalArgumentException.class)
	public void nulltest3() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion(null, null,null,null);
	}
	@Test(expected=IllegalArgumentException.class)
	public void badPathTest() throws InterruptedException, IOException {
		ConversionHandler ch = new ConversionHandler();
		ch.executeProfileDataConversion("gff3ToBed", "/home/c11/c11dhn/edu/PVT/scripts/neeuwmap/TP_SG_A_ZeroLvl_Release5.gff", "/home/c11/c11dhn/edu/PVT/", "Unknown");
	}
}