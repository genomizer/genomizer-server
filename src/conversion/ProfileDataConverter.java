package conversion;

import java.io.IOException;

import process.Executor;

public class ProfileDataConverter extends Executor {

	public String wigToSgr(String infilePath, String outFile)
			throws InterruptedException, IOException {
		return wigToSgrConversion(infilePath, outFile);
	}

	public String gff3ToSgr(String infilePath, String outFile)
			throws InterruptedException, IOException {
		return gff3ToSgrConversion(infilePath, outFile);
	}

	public String bedToSgr(String infilePath, String outFile)
			throws InterruptedException, IOException {
		return bedToSgrConversion(infilePath, outFile);
	}

	public String sgrToBed(String infilePath, String outFile,
			String secondColumn) throws InterruptedException, IOException {
		return sgrToBedConversion(infilePath, outFile, secondColumn);
	}

	public String sgrToWig(String infilePath, String outFile)
			throws InterruptedException, IOException {
		return sgrToWigConversion(infilePath, outFile);
	}

	public String wigToBed(String infilePath, String outFile,
			String secondColumn) throws InterruptedException, IOException {
		return sgrToBedConversion(
				wigToSgrConversion(infilePath,
						infilePath.replace(".wig", ".sgr")), outFile,
				secondColumn);
	}

	public String gff3ToBed(String infilePath, String outFile,
			String secondColumn) throws InterruptedException, IOException {
		return sgrToBedConversion(
				gff3ToSgrConversion(infilePath,
						infilePath.replace(".gff", ".sgr")), outFile,
				secondColumn);
	}

	public String gff3ToWig(String infilePath, String outFile)
			throws InterruptedException, IOException {
		return sgrToWigConversion(
				gff3ToSgrConversion(infilePath,
						infilePath.replace(".gff", ".sgr")), outFile);
	}

	public String bedToWig(String infilePath, String outFile)
			throws InterruptedException, IOException {
		return sgrToWigConversion(
				bedToSgrConversion(infilePath,
						infilePath.replace(".bed", ".sgr")), outFile);
	}

	// Actual conversion
	private String sgrToBedConversion(String infilePath, String outFile,
			String secondColumn) throws InterruptedException, IOException {
		executeScript(parse("perl MakeBEDfromSGR.pl " + infilePath + " "
				+ outFile + " " + secondColumn));

		return infilePath.replace(".sgr", ".bed");
	}

	private String bedToSgrConversion(String infilePath, String outFile)
			throws InterruptedException, IOException {

		executeScript(parse("perl MakeSGRfromBED.pl " + infilePath + " "
				+ outFile));

		return infilePath.replace(".bed", ".sgr");
	}

	private String wigToSgrConversion(String infilePath, String outFile)
			throws InterruptedException, IOException {
		executeScript(parse("perl AllFixedStepWig2sgr.pl " + infilePath
				+ " " + outFile));

		return infilePath.replace(".wig", ".sgr");
	}

	private String gff3ToSgrConversion(String infilePath, String outFile)
			throws InterruptedException, IOException {
		executeScript(parse("perl MakeSGRfromGFF.pl " + infilePath + " "
				+ outFile));
		return infilePath.replace(".gff", ".sgr");
	}

	private String sgrToWigConversion(String infilePath, String outFile)
			throws InterruptedException, IOException {
		executeScript(parse("perl sgr2wig.pl " + infilePath + " " + outFile));
		return infilePath.replace(".sgr", ".wig");
	}

}
