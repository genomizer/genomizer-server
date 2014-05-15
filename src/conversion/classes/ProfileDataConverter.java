package conversion.classes;

import java.io.IOException;

import process.classes.Executor;

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

	// public String gff3ToWig (String infilePath, String secondColumn){
	// return sgrToWigConversion(gff3ToSgrConversion(infilePath));
	// }
	// public String sgrToWig (String infilePath){
	// return sgrToWigConversion(infilePath);
	// }
	// public String bedToWig (String infilePath, String secondColumn){
	// return sgrToWigConversion(bedToSgrConversion(infilePath));
	// }

	// Actual conversion
	private String sgrToBedConversion(String infilePath, String outFile,
			String secondColumn) throws InterruptedException, IOException {
		executeScript(parse("perl MakeBEDfromSGR_v1.pl " + infilePath + " "
				+ outFile + " " + secondColumn));

		return infilePath.replace(".sgr", ".bed");
	}

	private String bedToSgrConversion(String infilePath, String outFile)
			throws InterruptedException, IOException {

		executeScript(parse("perl MakeSGRfromBED_v1.pl " + infilePath + " "
				+ outFile));

		return infilePath.replace(".bed", ".sgr");
	}

	private String wigToSgrConversion(String infilePath, String outFile)
			throws InterruptedException, IOException {
		executeScript(parse("perl AllFixedStepWig2sgr_v1.pl " + infilePath
				+ " " + outFile));

		return infilePath.replace(".wig", ".sgr");
	}

	private String gff3ToSgrConversion(String infilePath, String outFile)
			throws InterruptedException, IOException {
		executeScript(parse("perl MakeSGRfromGFF_v1.pl " + infilePath + " "
				+ outFile));
		return infilePath.replace(".gff", ".sgr");
	}
	// private String sgrToWigConversion (String infilePath){
	// return infilePath.replace(".sgr", ".wig");
	// }

}
