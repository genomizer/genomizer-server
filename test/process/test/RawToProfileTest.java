package process.test;


import org.junit.After;
import org.junit.Before;
import org.junit.*;

import process.ProcessException;
import process.RawToProfileConverter;

import java.io.File;
import java.io.IOException;

public class RawToProfileTest {
	RawToProfileConverter rtp = null;
	String bowtieParams = "";
	String genomeBowtie2 = "resources/bowtie2/example/index/lambda_virus";

	@Before
	public void setup() {
		rtp = new RawToProfileConverter();
	}

	@After
	public void tearDown() {
		new File("resources/processTest/results/test_sorted_without_duplicates.sam").delete();
		new File("resources/processTest/results/test.sgr").delete();
		rtp = null;
	}

	@Test
	public void shouldRunStaticCall() 
			throws ProcessException, IOException, InterruptedException {
		String inFile = "reads_1.fq";
		String outFile = "test.sgr";
		RawToProfileConverter.procedureRaw(
				bowtieParams, inFile, outFile, true,true,true,"GENOMEVERSION",genomeBowtie2,
						"resources/bowtie2/example/reads/",
				"resources/processTest/results/");
	}

}
