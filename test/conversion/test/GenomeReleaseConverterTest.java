package conversion.test;

import org.junit.Test;

import conversion.GenomeReleaseConverter;

public class GenomeReleaseConverterTest {


	// TODO: This seems to always succeed no matter whether the conversion happened or not.
	@Test
	public void test() {
		GenomeReleaseConverter handler = new GenomeReleaseConverter();
		handler.procedure("test.bed", "temp.bed", "dm2ToDm3.over.chain.gz", "unlifted.bed");
	}


}
