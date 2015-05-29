package process.test;


import org.junit.After;
import org.junit.Before;
import org.junit.*;

import process.ProcessException;
import process.RawToProfileConverter;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

public class RawToProfileTest {
	RawToProfileConverter rtp = null;
	String path = "test/male.sam";
	String bowTie = "-a";
	//"d_melanogaster_fb5_22 -q reads/MOF_male_wt_reads_sample.fastq -S " +path;
	String genome = "resources/bowtie2/example/index/lambda_virus";
	String[] parameters = new String[]{bowTie, genome, "", "","","","",""};

	@Before
	public void setup() {
		rtp = new RawToProfileConverter();
	}

	@After
	public void tearDown() {
		rtp = null;
	}

	@Test (expected = ProcessException.class)
	public void ShouldNotCrash() throws ProcessException {
		String inFolder = "HEJ";
		// String expected = "Indata is not in the correct format";
		String outFilePath = null;
		rtp.procedure(parameters, inFolder, outFilePath);
	}

	@Test (expected = ProcessException.class)
	public void ShouldNotRunWhenParametersIsNull() throws ProcessException {
		String[] parameters = null;
		String inFolder = "resources";
		// String expected = "Indata is not in the correct format";
		String outFilePath = "resources";
		rtp.procedure(parameters, inFolder, outFilePath);
	}

	@Test (expected = ProcessException.class)
	public void ShouldNotRunWithFiveParameters() throws ProcessException {
		String inFolder = "HEJ";
		String outFilePath = "";
		// String expected = "Indata is not in the correct format";
		String[] param = new String[]{"one","two","three","four","five"};
		rtp.procedure(param,  inFolder, outFilePath);
	}


	@Test (expected = ProcessException.class)
	public void ShouldNotRunWhenNotFindingFiles() throws ProcessException  {
		String inFolder = "HEJ";
		String outFilePath = "";
		// String expected = "Indata is not in the correct format";
		String[] param = new String[]{"one","two","three","four"};
		rtp.procedure(param, inFolder, outFilePath);
	}

//	@Test
//	public void ShouldNot

//	@Test
//	public void shouldParseAStringToAnArrayOfStrings() {
//		String[] input = new String[]{"hello this is a test"};
//		String[] output = rtp.getBowTieParameters();
//		assertArrayEquals(new String[]{"hello","this","is","a","test"}, output);
//	}

//	@Test(expected = IOException.class)
//	public void ExceptedIOException() {
//		rtp.execute(new String[]{"HEJ"});
//	}

//	@Test
//	public void StandardShouldGetString() {
//		rtp.standardParamProcedure(new String[]{""});
//	}
//
//	@Test
//	public void SpecificShouldGetString() {
//		rtp.specificParamProcedure(new String[]{""});
//	}

	@Ignore
	@Test
	public void shouldProduceNewSamFile() throws ProcessException{
		String inFolder = "resources/processTest/fastq";
		String outFilePath = "resources/processTest/results";

		rtp.procedure(parameters, inFolder, outFilePath);
	}

	@Test
	public void shouldRunStaticCall() throws ProcessException, IOException {
		String inFile = "fastq/test.fastq";
		String outFile = "results/test.wig";
		RawToProfileConverter.procedureRaw(
				bowTie, inFile, outFile, true,"GENOMEVERSION",genome,
				new AbstractMap.SimpleEntry<String,String>(
						"resources/processTest/",""));
	}

}
