package conversion.test;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import conversion.GenomeReleaseConverter;

import java.io.*;


@Ignore
public class GenomeReleaseConverterTest {


	@Test
	public void shouldRunLiftoverWithoutFailing()
			throws IOException, InterruptedException {
		GenomeReleaseConverter handler = new GenomeReleaseConverter();
		handler.procedure("genomeConversionTestData/test.bed",
				"genomeConversionTestData/conversionResults/temp.bed",
				"genomeConversionTestData/dm2ToDm3.over.chain.gz",
				"genomeConversionTestData/conversionResults/unlifted.bed");
	}

	@Test
	public void shouldCreateResultBedFile()
			throws IOException, InterruptedException {
		GenomeReleaseConverter handler = new GenomeReleaseConverter();
		handler.procedure("genomeConversionTestData/test.bed",
				"genomeConversionTestData/conversionResults/temp.bed",
				"genomeConversionTestData/dm2ToDm3.over.chain.gz",
				"genomeConversionTestData/conversionResults/unlifted.bed");
		File resFile = new File("/resources/genomeConversionTestData/" +
								"conversionResults/temp.bed");
		assertTrue(resFile.exists());
	}

	@Test
	public void shouldCreateUnliftedBedFile()
		throws IOException, InterruptedException {
		GenomeReleaseConverter handler = new GenomeReleaseConverter();
		handler.procedure("genomeConversionTestData/test.bed",
				"genomeConversionTestData/conversionResults/temp.bed",
				"genomeConversionTestData/dm2ToDm3.over.chain.gz",
				"genomeConversionTestData/conversionResults/unlifted.bed");
		File resFile = new File("/resources/genomeConversionTestData/" +
								"conversionResults/unlifted.bed");
		assertTrue(resFile.exists());
	}


}
