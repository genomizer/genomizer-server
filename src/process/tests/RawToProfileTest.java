package process.tests;

import static org.junit.Assert.*;

import java.io.IOException;


import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import process.classes.Executor;
import process.classes.ProcessException;
import process.classes.RawToProfileConverter;

public class RawToProfileTest {
	RawToProfileConverter rtp = null;
	String path = "test/male.sam";
	String bowTie = "bowtie -a -m 1 --best -p 10 -v 2 ";
	//"d_melanogaster_fb5_22 -q reads/MOF_male_wt_reads_sample.fastq -S " +path;
	String[] parameters = new String[]{bowTie};

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
		String expected = "Indata is not in the correct format";
		String outFilePath = null;
		rtp.procedure(parameters, inFolder, outFilePath);
	}

	@Test (expected = ProcessException.class)
	public void ShouldNotRunWhenParametersIsNull() throws ProcessException {
		String[] parameters = null;
		String inFolder = "resources";
		String expected = "Indata is not in the correct format";
		String outFilePath = "resources";
		rtp.procedure(parameters, inFolder, outFilePath);
	}

	@Test (expected = ProcessException.class)
	public void ShouldNotRunWithFiveParameters() throws ProcessException {
		String inFolder = "HEJ";
		String outFilePath = "";
		String expected = "Indata is not in the correct format";
		String[] param = new String[]{"one","two","three","four","five"};
		rtp.procedure(param,  inFolder, outFilePath);
	}


	@Test (expected = ProcessException.class)
	public void ShouldNotRunWhenNotFindingFiles() throws ProcessException  {
		String inFolder = "HEJ";
		String outFilePath = "";
		String expected = "Indata is not in the correct format";
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
}