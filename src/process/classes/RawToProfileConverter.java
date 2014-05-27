package process.classes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Class used to create profile data from .fastq format.
 *
 * @version 1.0
 */
public class RawToProfileConverter extends Executor {

	private String remoteExecution;
	private String dir;
	private String sortedDirForCommands;
	private String samToGff;
	private String gffToAllnusgr;
	private File fileDir;
	private String inFolder;
	private String[] parameters;
	private String rawFile1;
	private String rawFile2;
	private String rawFile_1_Name;
	private String rawFile_2_Name;
	private String logString;
	private RawToProfileProcessChecker checker;
	private ParameterValidator validator;
	private String filesToBeMoved = null;
	private Stack<String> toBeRemoved;
	private String sortedDirForFile;

	public RawToProfileConverter() {
		super();
		toBeRemoved = new Stack<String>();
		checker = RawToProfileProcessChecker.rawToProfileCheckerFactory();
		validator = new ParameterValidator();
	}

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
	 * @throws ProcessException
	 */
	public String procedure(String[] parameters, String inFolder,
			String outFilePath) throws ProcessException {
		File[] inFiles = null;
		if (inFolder != null) {
			inFolder = validateInFolder(inFolder);
			inFiles = getRawFiles(inFolder);
		} else {
			throw new ProcessException("Fatal error: This should never happen");
		}

		if (inFiles == null || inFiles.length == 0) {
			throw new ProcessException("Folder does not contain raw files");
		}

		this.parameters = parameters;
		this.inFolder = inFolder;

		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				System.out.println("param " + i + ": " + parameters[i]);
			}

			printStringArray(parameters);
		}

		if (verifyInData(parameters, inFolder, outFilePath) == false
				|| !CorrectInfiles(inFiles)) {
			throw new ProcessException("Wrong format of input data");
		} else {
			initiateConversionStrings(parameters, outFilePath);
			makeConversionDirectories(remoteExecution + "resources/" + dir
					+ "/sorted");
			checker.calculateWhichProcessesToRun(parameters);
			ValidateParameters(parameters);
			rawFile1 = inFiles[0].getName();
			rawFile_1_Name = rawFile1.substring(0, rawFile1.length() - 6);
			if (inFiles.length == 2) {
				rawFile2 = inFiles[1].getName();
				rawFile_2_Name = rawFile2.substring(0, rawFile2.length() - 6);
			}

			initiateConversionStrings(parameters, outFilePath);

			// printTrace(parameters, inFolder, outFilePath);
			if (fileDir.exists()) {

				if (checker.shouldRunBowTie()) {
					System.out.println("Running Bowtie");
					logString = runBowTie(rawFile1, rawFile_1_Name);
					System.out.println(logString);

					checkBowTieFile("resources/" + dir + rawFile_1_Name

					+ ".sam", rawFile_1_Name);

					System.out.println("Running SortSam");
					sortSamFile(rawFile_1_Name);

					if (inFiles.length == 2) {
						logString = logString + "\n"
								+ runBowTie(rawFile2, rawFile_2_Name);

						checkBowTieFile(remoteExecution + "resources/" + dir
								+ rawFile_2_Name + ".sam", rawFile_2_Name);

						sortSamFile(rawFile_2_Name);
					}// Sets parameters for sorting first sam file

					toBeRemoved.push(remoteExecution + "resources/" + dir);
					filesToBeMoved = sortedDirForFile;
					toBeRemoved.push(filesToBeMoved);
				}

				if (checker.shouldRunSamToGff()) {
					System.out.println("Running SamToGff");
					try {
						logString = logString + "\n"
								+ executeScript(parse(samToGff));

						// if (!checkStep(sortedDirForCommands)) {
						// cleanUp(toBeRemoved);
						// throw new ProcessException("SamToGff failed");
						// }

					} catch (InterruptedException e) {
						throw new ProcessException(
								"Process interrupted while creating GFF file");
					} catch (IOException e) {
						throw new ProcessException(
								"Could not run gff conversion, please check your input and permissions");
					}
					filesToBeMoved = sortedDirForFile + "reads_gff/";
					toBeRemoved.push(filesToBeMoved);
				}

				if (checker.shouldRunGffToAllnusgr()) {
					System.out.println("Running gffToAllnucsgr");
					try {
						logString = logString + "\n"
								+ executeScript(parse(gffToAllnusgr));
					} catch (InterruptedException e) {
						throw new ProcessException(
								"Process interrupted while converting to SGR format");
					} catch (IOException e) {
						throw new ProcessException(
								"Could not run SGR conversion, please check your input and permissions");
					}
					filesToBeMoved = sortedDirForFile
							+ "reads_gff/allnucs_sgr/";
					toBeRemoved.push(filesToBeMoved);
				}

				if (checker.shouldRunSmoothing()) {
					System.out.println("Running Smoothing");

					// Second parameter should be false when ratio
					// calculation should not run.

					runSmoothing(parameters, false);

					filesToBeMoved = sortedDirForFile
							+ "reads_gff/allnucs_sgr/smoothed/";
					toBeRemoved.push(filesToBeMoved);
				}

				if (checker.shouldRunRatioCalc()) {
					System.out.println("Running ratio calculation");

					doRatioCalculation(sortedDirForCommands
							+ "reads_gff/allnucs_sgr/smoothed/", parameters);

					runSmoothing(parameters, true);

					toBeRemoved.push(sortedDirForFile
							+ "reads_gff/allnucs_sgr/smoothed/ratios/");
					filesToBeMoved = sortedDirForFile
							+ "reads_gff/allnucs_sgr/smoothed/ratios/smoothed/";
					toBeRemoved.push(filesToBeMoved);
				}

				moveEndFiles(filesToBeMoved, outFilePath);

				cleanUp(toBeRemoved);

			} else {

				logString = logString + " " + "Failed to create directory "
						+ fileDir.toString();
			}
		}
		// System.out.println("logString = " + logString);
		return logString;
	}

	private File[] getRawFiles(String inFolder) throws ProcessException {

		File[] rawDirFiles = new File(inFolder).listFiles();
		File[] rawFilesIsFile = null;
		if (rawDirFiles != null && !(rawDirFiles.length == 0)) {
			ArrayList<File> files = new ArrayList<File>();

			for (int i = 0; i < rawDirFiles.length; i++) {
				if (rawDirFiles[i].isFile()) {
					files.add(rawDirFiles[i]);
				}
			}

			rawFilesIsFile = new File[files.size()];
			for (int i = 0; i < rawFilesIsFile.length; i++) {
				rawFilesIsFile[i] = files.get(i);
			}
			if (rawFilesIsFile.length == 0) {
				throw new ProcessException("No files found in directory "
						+ inFolder);
			}
		} else {
			throw new ProcessException("No files found in directory "
					+ inFolder);
		}
		return rawFilesIsFile;
	}

	private boolean ValidateParameters(String[] parameters)
			throws ProcessException {
		boolean isOk = true;
		if (checker.shouldRunSmoothing()
				&& !validator.validateSmoothing(parameters[4])) {
			isOk = false;
		}
		if (checker.shouldRunStep() && !validator.validateStep(parameters[5])) {
			isOk = false;
		}
		if (checker.shouldRunRatioCalc()
				&& !validator.validateRatioCalculation(parameters[6],
						parameters[7])) {
			isOk = false;
		}
		// if(checker.shouldRunRatioCalc() &&
		// !validator.validateSmoothing(parameters[7])) {
		// isOk = false;
		// }

		return isOk;
	}

	/**
	 * Checks that bowtie created the files it should create and that the size
	 * is bigger then zero.
	 *
	 * @param dir
	 *            The directory where the files bowtie creates should be placed
	 * @param fileName
	 *            The name of the file bowtie should create.
	 * @throws ProcessException
	 */
	private void checkBowTieFile(String dir, String fileName)
			throws ProcessException {
		File bowTie = new File(dir);
		if (!bowTie.exists() || bowTie.length() == 0) {
			throw new ProcessException("Bowtie failed to run on file : "
					+ fileName + bowTie.exists() + bowTie.length());
		}
	}

	/**
	 * Validates that the infolder has the correct format, inFolder should not
	 * have a "/" at the end of the string to correctly create the File objects
	 * with it.
	 *
	 * @param folderString
	 *            The folder to remove a "/" from the end if there is one.
	 *
	 * @return The correct format of the inFolder string.
	 */
	private String validateInFolder(String folderString) {
		if (folderString != null) {
			if (folderString.endsWith("/")) {
				return folderString.substring(0, folderString.length() - 1);
			} else
				return folderString;
		}
		return null;
	}

	/**
	 * Runs the smoothing and step procedures. Checks if its smoothing after
	 * ratio calculation or (parameters[5])[1])just normal smoothing. Changes
	 * some parameters depending on ratio calculation or not. runs smoothing
	 * with the incoming parameters and runs stepping if it should.
	 *
	 * @param parameters
	 *            Contains all parameters needed to run smoothing snd step
	 * @param isRatioCalc
	 *            If ratio calculation or not.
	 * @throws ProcessException
	 */
	private void runSmoothing(String[] parameters, boolean isRatioCalc)
			throws ProcessException {

		String[] parameterArray;
		int stepSize = 0;

		File[] filesToSmooth;
		File file;
		if (isRatioCalc) {
			parameterArray = parse(parameters[7]);
			stepSize = 1;
			filesToSmooth = new File(sortedDirForFile
					+ "reads_gff/allnucs_sgr/smoothed/ratios").listFiles();
			file = new File(sortedDirForFile
					+ "reads_gff/allnucs_sgr/smoothed/ratios/smoothed");

		} else {
			filesToSmooth = new File(sortedDirForFile + "reads_gff/allnucs_sgr")
					.listFiles();
			file = new File(sortedDirForFile + "reads_gff/allnucs_sgr/smoothed");
			if (parameters[6].equals("")) {
				stepSize = 1;
			} else {
				try {
					stepSize = Integer.parseInt(parse(parameters[5])[1]);
				} catch (NumberFormatException e) {
					throw new ProcessException(
							"Stepsize parameter is not an integer value");
				}
			}

			parameterArray = parse(parameters[4]);
		}

		int[] intParams = new int[parameterArray.length];
		for (int i = 0; i < parameterArray.length; i++) {
			try {
				intParams[i] = Integer.parseInt(parameterArray[i]);
			} catch (NumberFormatException e) {
				throw new ProcessException(
						"Smoothing parameters are wrong format");
			}
		}

		if (!file.exists()) {
			file.mkdirs();
		}

		SmoothingAndStep smooth = new SmoothingAndStep();
		if (filesToSmooth != null) {
			for (int i = 0; i < filesToSmooth.length; i++) {
				if (filesToSmooth[i].isFile()
						&& isSgr(filesToSmooth[i].getName())) {
					String inFile = filesToSmooth[i].getAbsoluteFile()
							.toString();
					String outFile = filesToSmooth[i].getName();
					SmoothingParameterChecker smoothChecker = SmoothingParameterChecker
							.SmoothingParameterCheckerFactory(parameters[4]);
					// +"_"+getSmoothType+"_winSiz-" + "_minProbe-" +
					// getMinProbe
					outFile = outFile.substring(0, outFile.length() - 4) + "_"
							+ smoothChecker.getSmoothType() + "_winSiz-"
							+ smoothChecker.getWindowSize() + "_minProbe-"
							+ smoothChecker.getMinProbe();
					if (stepSize != 1) {
						outFile = outFile + "_step" + stepSize + ".sgr";
					} else {
						outFile = outFile + ".sgr";
					}
					outFile = file.toString() + "/" + outFile;

					smooth.smoothing(intParams, inFile, outFile, stepSize);
				}
			}
		}
	}

	/**
	 * Checks if a strings ends with .sgr
	 *
	 * @param name
	 * @return
	 */
	private boolean isSgr(String name) {
		return name.endsWith(".sgr");
	}

	/**
	 * Checks that the files in the inFolder is valid.
	 *
	 * @param inFiles
	 * @return
	 * @throws ProcessException
	 */

	private boolean CorrectInfiles(File[] inFiles) throws ProcessException {
		ProcessException e = null;
		boolean checkInFiles = true;
		if (inFiles == null) {
			e = new ProcessException("Filepath to raw file is null");
			checkInFiles = false;
		} else if (inFiles.length > 2 && inFiles.length < 1) {
			e = new ProcessException("Wrong quantity of raw file in Filepath");
			checkInFiles = false;
		}
		if (e != null) {
			throw e;
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
	 * @throws ProcessException
	 */
	private void doRatioCalculation(String dirPath, String[] parameters)
			throws ProcessException {
		String ratioCalc = "perl ratio_calculator_v2.pl " + dirPath + " "
				+ parameters[6];

		try {
			logString = logString + executeScript(parse(ratioCalc));
			System.out.println("RATIO LOGSTRING = " + logString);
		} catch (InterruptedException e) {
			throw new ProcessException(
					"Process interrupted while running ratio calculation on files in folder "
							+ dirPath);
		} catch (IOException e) {
			throw new ProcessException(
					"Could not read or write to files while running ratio calculation in folder: "
							+ dirPath);
		}
	}

	/**
	 * Prints important variables, used for testing.
	 *
	 * @param parametersSystem
	 *            .out.println(nrOfProc);
	 * @param inFolder
	 * @param outFile
	 */
	private void printTrace(String[] parameters, String inFolder, String outFile) {
		System.out.println("dir " + fileDir.toString());
		System.out.println("INFOLDER = " + inFolder);
		System.out.println("OUTFILE = " + outFile);
		System.out.println("DIR = " + dir);
		System.out.println("SORTEDDIR = " + sortedDirForCommands);
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
		sortedDirForCommands = remoteExecution + dir + "sorted/";
		sortedDirForFile = remoteExecution + "resources/" + dir + "sorted/";
		samToGff = "perl sam_to_readsgff_v1.pl " + sortedDirForCommands;
		gffToAllnusgr = "perl readsgff_to_allnucsgr_v1.pl "
				+ sortedDirForCommands + "reads_gff/";
		// step10 = "perl AllSeqRegSGRtoPositionSGR_v1.pl " + parameters[3] +
		// " " * @throws ProcessException

		// + sortedDir + "reads_gff/allnucs_sgr/smoothed/"; // Step 6
		// smooth = "perl smooth_v4.pl " + sortedDir + "reads_gff/allnucs_sgr/ "
		// + parameters[2]; // Step 5
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
	 * @throws ProcessException
	 */
	private String runBowTie(String fileOne, String fileOneName)
			throws ProcessException {
		String bowTieParams = checkBowTieProcessors(parameters[0]);

		// used to run remotely
		// String[] bowTieParameters = parse("bowtie " + bowTieParams + " "
		// + parameters[1] + " " + inFolder + "/" + fileOne + " " +
		// remoteExecution+"resources/"+dir
		// + fileOneName + ".sam");

		// Ordinary
		String[] bowTieParameters = parse("bowtie " + bowTieParams + " "
				+ parameters[1] + " " + inFolder + "/" + fileOne + " " + dir
				+ fileOneName + ".sam");

		try {
			return executeProgram(bowTieParameters);
		} catch (InterruptedException e) {
			throw new ProcessException(
					"Process interrupted while running bowtie on file: "
							+ fileOneName);
		} catch (IOException e) {
			throw new ProcessException("Could not run bowTie on file: "
					+ fileOneName + ", please check your input and permissions");
		}
	}

	/**
	 * Makes it so that bowtie always runs on all processors except two. if the
	 * machine only has 1 or 2 processors bowtie will run on atleast one
	 * processor.
	 *
	 * @param params
	 * @return
	 */
	private String checkBowTieProcessors(String params) {
		String[] bowTieParams = parse(params);

		for (int i = 0; i < bowTieParams.length; i++) {
			if (bowTieParams[i].equals("-p")) {

				int nrOfProc = Runtime.getRuntime().availableProcessors() - 2;

				if (nrOfProc < 1) {
					nrOfProc = 1;
				}
				bowTieParams[i + 1] = Integer.toString(nrOfProc);
			}
		}

		String bowTieString = "";
		for (int i = 0; i < bowTieParams.length; i++) {
			bowTieString += bowTieParams[i] + " ";
		}
		return bowTieString;
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
	 * @throws ProcessException
	 */
	private void sortSamFile(String unsortedSamFileName)
			throws ProcessException {
		String sortSam = "sort " + dir + unsortedSamFileName + ".sam"
				+ " -k 3,3 -k 4,4n";
		try {
			executeShellCommand(parse(sortSam), remoteExecution + dir
					+ "sorted/", unsortedSamFileName + "_sorted.sam");
		} catch (IOException e) {
			throw new ProcessException("Could not read file: "
					+ remoteExecution + dir + "sorted/" + unsortedSamFileName
					+ ".sam");
		} catch (InterruptedException e) {
			throw new ProcessException("Process interrupted by external "
					+ "source while sorting sam file");
		}
	}

	/**
	 * Creates the working directory for the procedure to put its files in.
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

	/**
	 * Validates that all parameters procedure takes is valid.
	 *
	 * @param parameters
	 * @param inFolder
	 * @param outFilePath
	 * @return
	 */
	private boolean verifyInData(String[] parameters, String inFolder,
			String outFilePath) {

		if (parameters == null) {
			System.out.println("Parameters are null");
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

		if (!checkIfFolderExists(outFilePath) || !checkIfFolderExists(inFolder)) {
			System.out.println("Folders does not exist");
			return false;
		}

		return true;
	}

	/**
	 * Checks if a folder exists.
	 *
	 * @param folder
	 * @return
	 */
	private boolean checkIfFolderExists(String folder) {
		File dir = new File(folder);
		return dir.exists();
	}
}
