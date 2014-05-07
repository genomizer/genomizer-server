package process.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class used to create .wig file from .fastq format.
 * 
 * @version 1.0
 */
public class RawToProfileConverter extends Executor {

	private String[] bowTieParameters; // Step 1
	private final String SMOOTH = "perl smooth_v4.pl ";
	private final String GFF_TO_ALLNUCSGR = "perl readsgff_to_allnucsgr_v1.pl ";
	private final String SGR_2_WIG = "perl sgr2wig.pl ";
	private final String SAM_TO_GFF = "perl sam_to_readsgff_v1.pl ";
	private final String STEP10 = "perl AllSeqRegSGRtoPositionSGR_v1.pl y 10 ";
	private final String RATIO_CALC = "perl ratio_calculator_v2 ";
	/**
	 * Takes a string array that contains bowtie parameters and then follows a
	 * procedure.
	 * 
	 * 1. runs the bowtie program to get a .sam file. 2. runs a linux shell
	 * command to sort the sam file. 3. runs a perl script that creates a .ggf
	 * file from the sam file. 4. runs a perl script that creates a .sgr file
	 * from the .gff file. 5. runs a perl script to smooth the .sgr file. 6.
	 * runs a perl script to convert the .sgr file to a .sgr with fixed size. 7.
	 * runs a perl script that converts the .sgr file to .wig file.
	 * 
	 * 
	 * 
	 * @param parameters
	 *            String array with execution parameters
	 * @param inFile
	 *            The filepath to the file to create a wig from
	 * @param outFile
	 *            Filepath to where the .wig file should be placed.
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void procedure(String[] parameters, String inFolder, String outFile, boolean ratioCalc)
			throws InterruptedException, IOException {
		File[] inFiles = new File("resources/" + inFolder).getAbsoluteFile()
				.listFiles();
		String fileOne;
		String fileTwo;
		String fileOneName;
		String fileTwoName;

		fileOne = inFiles[0].getName();
		fileTwo = inFiles[1].getName();
		fileOneName = fileOne.substring(0, fileOne.length() - 6);
		fileTwoName = fileTwo.substring(0, fileTwo.length() - 6);

		String dir = "results_" + Thread.currentThread().getId() + "/";
		String sortedDir = dir + "sorted/";

		String outString = null;

		File fileDir = new File("resources/" + dir);
		// deleteDir(fileDir);
		System.out.println("Deletes directory");

		System.out.println("dir " + fileDir.toString());
		if (!fileDir.exists()) {
			fileDir.mkdir();
		}

		if (fileDir.exists()) {

			String sortSam; // Step 2
			
			// Bowtie on 2 files
			outString = runBowTie(parameters, inFolder, fileOne, fileTwo,
					fileOneName, fileTwoName, dir, outString);

			//Sort sam file one
			sortSam = "sort " + dir + fileOneName + ".sam" + " -k 3,3 -k 4,4n";
			doSortSam(fileOneName, dir, sortSam);

			//Sort sam file two
			sortSam = "sort " + dir + fileTwoName + ".sam" + " -k 3,3 -k 4,4n";
			doSortSam(fileTwoName, dir, sortSam);
																
			
			outString = outString + " " + executeScript(parse(SAM_TO_GFF+sortedDir));

			outString = outString + " " + executeScript(parse(GFF_TO_ALLNUCSGR+sortedDir + "reads_gff/"));

			outString = outString + " " + executeScript(parse(SMOOTH+sortedDir
					+ "reads_gff/allnucs_sgr/ "+"10 1 5 0 0"));

			outString = outString + " " + executeScript(parse(STEP10+sortedDir + "reads_gff/allnucs_sgr/smoothed/"));

			File[] sgrFiles = new File("resources/" + sortedDir
					+ "/reads_gff/allnucs_sgr/smoothed/Step10")
					.getAbsoluteFile().listFiles();

//			if(ratioCalc) {
//				ratioCalc("resources/" + sortedDir
//						+ "/reads_gff/allnucs_sgr/smoothed/Step10/");
//			} else {
			
			fileOne = sgrFiles[0].getName();
			fileTwo = sgrFiles[1].getName();
			fileOneName = fileOne.substring(0, fileOne.length() - 6);
			fileTwoName = fileTwo.substring(0, fileTwo.length() - 6);

			outString = doSgr2Wig(outFile, fileOne, fileTwo, fileOneName,
					fileTwoName, sortedDir, outString);
//			}
			
			System.out.println("cleanup gav tillbaka = "
					+ cleanUp(cleanUpInitiator("resources/" + dir)));

			System.out.println(outString);
		}
	}

//	private void ratioCalc(String dir) throws InterruptedException, IOException {
//		executeScript(parse(RATIO_CALC+dir
//				+ "reads_gff/allnucs_sgr/smoothed/step10/ "+"4 0"));
//		
//		executeScript(SMOOTH+"reads_gff/allnucs_sgr/smoothed/step10/");
//	}

	private String doSgr2Wig(String outFile, String fileOne, String fileTwo,
			String fileOneName, String fileTwoName, String sortedDir,
			String outString) throws InterruptedException, IOException {
		
		outString = outString + " " + executeScript(parse(SGR_2_WIG+sortedDir
				+ "/reads_gff/allnucs_sgr/smoothed/Step10/" + fileOne + " "
				+ outFile + fileOneName + ".wig"));

		outString = outString + " " + executeScript(parse(SGR_2_WIG+sortedDir
				+ "/reads_gff/allnucs_sgr/smoothed/Step10/" + fileTwo + " "
				+ outFile + "/" + fileTwoName + ".wig"));

		return outString;
	}

	private String runBowTie(String[] parameters, String inFolder,
			String fileOne, String fileTwo, String fileOneName,
			String fileTwoName, String dir, String outString)
			throws InterruptedException, IOException {
		bowTieParameters = parse(parameters[0] + " " + inFolder + "/"
				+ fileOne + " " + dir + fileOneName + ".sam");
		outString = outString + " " + doBowtie(bowTieParameters);

		// Bowtie on fileTwo
		bowTieParameters = parse(parameters[0] + " " + inFolder + "/"
				+ fileTwo + " " + dir + fileTwoName + ".sam");
		outString = outString + " " + doBowtie(bowTieParameters);
		return outString;
	}

	private void doSortSam(String fileOneName, String dir, String sortSam)
			throws InterruptedException, IOException {
		executeShellCommand(parse(sortSam), dir + "sorted/", fileOneName
				+ "_sorted.sam");
	}

	private String doBowtie(String[] strings) throws InterruptedException,
			IOException {
		return executeProgram(bowTieParameters);
	}

	public String getTime(long temp) {
		String out = "";

		String minute;
		String second;

		second = String.valueOf((temp % 60000) / 1000);
		minute = String.valueOf(temp / 60000);

		if (second.length() == 1) {
			out = minute + ":0" + second;
		} else {
			out = minute + ":" + second;
		}
		System.out.println(out);
		return out;
	}

	public String[] getBowTieParameters() {
		return bowTieParameters;
	}
}