package process.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Class used to create profile data from .fastq format.
 *
 * @version 1.0
 */
public class RawToProfileConverter extends Executor {

	private String remoteExecution;
	private String dir;
	private String sortedDir;
	private String samToGff;
	private String gffToAllnusgr;
	private String smooth;
	private String step10;
	private File fileDir;
	private String inFolder;
	private String[] parameters;
	private String rawFile1;
	private String rawFile2;
	private String rawFile_1_Name;
	private String rawFile_2_Name;
	private String logString;
	private RawToProfileProcessChecker checker;
	private String filesToBeMoved = null;

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
	public String procedure(String[] parameters, String inFolder,
			String outFilePath) throws InterruptedException, IOException {
		checker = RawToProfileProcessChecker.rawToProfileCheckerFactory();

		File[] inFiles = new File(inFolder).listFiles();
		this.parameters = parameters;
		this.inFolder = inFolder;

		if(verifyInData(parameters, inFolder, outFilePath) == false || !CorrectInfiles(inFiles)) {
			logString =  "Indata is not in the correct format";
		} else {
			initiateConversionStrings(parameters, outFilePath);
			System.out.println("convdir=" + remoteExecution + dir + "/sorted");
			makeConversionDirectories(remoteExecution+"resources/" + dir + "/sorted");
			checker.calculateWhichProcessesToRun(parameters);
			rawFile1 = inFiles[0].getName();
			rawFile_1_Name = rawFile1.substring(0, rawFile1.length() - 6);
			if (inFiles.length == 2) {
				rawFile2 = inFiles[1].getName();
				rawFile_2_Name = rawFile2.substring(0, rawFile2.length() - 6);
			}

			initiateConversionStrings(parameters, outFilePath);

			printTrace(parameters, inFolder, outFilePath);
			if (fileDir.exists()) {
				if(checker.shouldRunBowTie()) {
					logString = runBowTie(rawFile1, rawFile_1_Name);
					System.out.println("nu körs sortering");
					sortSamFile(rawFile_1_Name);
					if (inFiles.length == 2) {
						logString = logString + "\n"
								+ runBowTie(rawFile2, rawFile_2_Name);
						// Sets parameters for sorting second sam file
						sortSamFile(rawFile_2_Name);
					}// Sets parameters for sorting first sam file
					filesToBeMoved = sortedDir;
				}
				if(checker.shouldRunSamToGff()) {
					logString = logString + "\n" + executeScript(parse(samToGff));
					filesToBeMoved = sortedDir
							+ "reads_gff/";
				}
				if(checker.shouldRunGffToAllnusgr()) {
					logString = logString + "\n"
						+ executeScript(parse(gffToAllnusgr));
					filesToBeMoved = sortedDir
							+ "reads_gff/allnucs_sgr/";
				}
				if(checker.shouldRunSmoothing()) {
					logString = logString + "\n" + executeScript(parse(smooth));
					filesToBeMoved = sortedDir
							+ "reads_gff/allnucs_sgr/smoothed/";
				}
				if(checker.shouldRunStep()) {
					logString = logString + "\n" + executeScript(parse(step10));
					filesToBeMoved = sortedDir
					+ "reads_gff/allnucs_sgr/smoothed/Step10/";
				}
				if(checker.shouldRunRatioCalc()) {
					System.out.println("RATODID = " + sortedDir
							+ "reads_gff/allnucs_sgr/smoothed/Step10/");
					doRatioCalculation(sortedDir
							+ "reads_gff/allnucs_sgr/smoothed/Step10/",
							parameters);

							filesToBeMoved = sortedDir
									+ "reads_gff/allnucs_sgr/smoothed/Step10/ratios/smoothed/";
				}


				System.out.println("logString = " +logString);
				System.out.println("filesToBeMoved = " + "resources/"+ filesToBeMoved);
				moveEndFiles("resources/" + filesToBeMoved,
						outFilePath);

				//cleanUp(cleanUpInitiator(remoteExecution + "resources/"  + dir));

			} else {
				logString = logString +" "+"Failed to create directory " + fileDir.toString();
			}
		}
		System.out.println("logString = " +logString);
		return logString;
	}

	private boolean CorrectInfiles(File[] inFiles) {
		boolean checkInFiles = true;
		if(inFiles == null) {
			System.out.println("infiles == null");
			checkInFiles = false;
		} else if (inFiles.length > 2 && inFiles.length < 1) {
			System.out.println("infiles length bad");
			checkInFiles = false;
		}
		return checkInFiles;
	}

	/**
	 * Initiates strings using the incoming parameters and executes the two
	 * scripts to do the ratio calculation.
	 *
	 * @param dirPath
	 *            the path to where the files used to run ratio calculation is
	 * @param parameters
	 *            contains the parameters for the two scripts
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void doRatioCalculation(String dirPath, String[] parameters)
			throws InterruptedException, IOException {
		String ratioCalc = "perl ratio_calculator_v2.pl " + dirPath + " "
				+ parameters[6];
		String smooth = "perl smooth_v4.pl " + dirPath + "ratios/" + " "
				+ parameters[7];
		logString = logString + executeScript(parse(ratioCalc));
		logString = logString + executeScript(parse(smooth));

	}

	/**
	 * Prints important variables, used for testing.
	 *
	 * @param parameters
	 * @param inFolder
	 * @param outFile
	 */
	private void printTrace(String[] parameters, String inFolder, String outFile) {
		System.out.println("dir " + fileDir.toString());
		System.out.println("INFOLDER = " + inFolder);
		System.out.println("OUTFILE = " + outFile);
		System.out.println("DIR = " + dir);
		System.out.println("SORTEDDIR = " + sortedDir);
		System.out.println("BOWTIE = "
				+ parse(parameters[0] + " " + inFolder + "/" + rawFile1 + " "
						+ dir + rawFile_1_Name + ".sam"));
	}

	/**
	 * Initiates strings that is used to run programs and scripts and also
	 * strings that specifies directories
	 *
	 * @param parameters
	 * @param outFile
	 */
	private void initiateConversionStrings(String[] parameters, String outFile) {
		remoteExecution = "";
		dir = "results_" + Thread.currentThread().getId() + "/";
		sortedDir = remoteExecution + dir + "sorted/";
		samToGff = "perl sam_to_readsgff_v1.pl " + sortedDir;
		gffToAllnusgr = "perl readsgff_to_allnucsgr_v1.pl " + sortedDir
				+ "reads_gff/";
		smooth = "perl smooth_v4.pl " + sortedDir + "reads_gff/allnucs_sgr/ "
				+ parameters[2]; // Step 5
		step10 = "perl AllSeqRegSGRtoPositionSGR_v1.pl " + parameters[3] + " "
				+ sortedDir + "reads_gff/allnucs_sgr/smoothed/"; // Step 6
		// sgr2wig = "perl sgr2wig.pl " + sortedDir
		// + "/reads_gff/allnucs_sgr/smoothed/Step10/*.sgr " + outFile
		// + "test.wig";
	}

	/**
	 * Constructs a string array with the values to run bowtie on the file that
	 * comes as parameter.
	 *
	 * @param fileOne
	 *            the name of the file with the file extension.
	 * @param fileOneName
	 *            the name of the file without the file extension.
	 * @return the value that bowtie returns.ckIfFolderExists(outFilePath)
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private String runBowTie(String fileOne, String fileOneName)
			throws InterruptedException, IOException {
		String[] bowTieParameters = parse("bowtie " + parameters[0] + " "
				+ parameters[1] + " " + inFolder + "/" + fileOne + " "
				+ remoteExecution + dir + fileOneName + ".sam");

		printStringArray(bowTieParameters);
		return executeProgram(bowTieParameters);
	}

	/**
	 * Prints a string array, used for testing.
	 *
	 * @param s
	 * @return
	 */
	private String printStringArray(String[] s) {
		String string = "";
		for (int i = 0; i < s.length; i++) {
			string += s[i] + " ";
		}
		System.out.println(string);
		return string;
	}

	/**
	 * Constructs a string with values to run a linux command that sorts a file
	 * with the specified parameters. puts a new sorted fil in a specified path.
	 *
	 * @param unsortedSamFileName
	 *            the name of the unsorted sam file
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void sortSamFile(String unsortedSamFileName)
			throws InterruptedException, IOException {
		String sortSam = "sort " + remoteExecution + dir + unsortedSamFileName
				+ ".sam" + " -k 3,3 -k 4,4n";
		executeShellCommand(parse(sortSam), remoteExecution + dir + "sorted/",
				unsortedSamFileName + "_sorted.sam");

	}

	/**
	 * Creates the working directory for the procedure to put its files in.
	 *
	 *
	 * @param directoryPath
	 *            the directory to create if it doesnt exist
	 */
	private void makeConversionDirectories(String directoryPath) {
		fileDir = new File(directoryPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

	}
	private boolean verifyInData(String[] parameters, String inFolder,
			String outFilePath) {

		if (parameters == null) {
			System.out.println("param == null");
			return false;
		}
		if (parameters.length < 0) {
			System.out.println("param < 0");
			return false;

		} else if (parameters.length > 8) {
			System.out.println("param > 8");
			return false;
		}

		if (inFolder == null || outFilePath == null) {
			System.out.println("inFOlder || outFolder == null");
			return false;
		}
		return checkIfFolderExists(outFilePath) && checkIfFolderExists(inFolder);


	}

	private boolean checkIfFolderExists(String folder) {
		File dir = new File(folder);
		System.out.println("dire.exists = "+dir.exists());
		return dir.exists();
	}


}