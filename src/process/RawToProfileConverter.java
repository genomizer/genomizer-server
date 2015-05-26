package process;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import command.ValidateException;
import server.ErrorLogger;
import server.ServerSettings;

/**
 * Class used to create profile data from .fastq format.
 * Can run a dynamic number of steps depending of which parameters that's sent
 * from the clients.
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

	/**
	 * Constructor that initializes some data structures used by the class.
	 */
	public RawToProfileConverter() {
		toBeRemoved = new Stack<String>();
		checker = new RawToProfileProcessChecker();
		validator = new ParameterValidator();
	}

	/**
	 * 1. runs the bowtie program to get a .sam file.
	 * 2. runs a linux shell command to sort the sam file.
	 * 3. runs a perl script that creates a .ggf file from the sam file.
	 * 4. runs a perl script that creates a .sgr file from the .gff file.
	 * 6. runs a converted method of the smoothing and stepping scripts.
	 * 7. runs a perl script that converts the .sgr file to .wig file.
	 * 8. runs a perl script that does ratio calculation on the files, also runs
	 * smoothing on these files.
	 *
	 * All these steps have to be run in order but the clients can specify how
	 * many steps they want to run by sending parameters for the steps
	 * they want to run.
	 *
	 *
	 *
	 * @param parameters
	 *            String array with execution parameters
	 * @param inFolder
	 *            The filepath to the file to create a wig from
	 * @param outFilePath
	 *            Filepath to where the .wig file should be placed.
	 * @throws ProcessException
	 */
	public String procedure(String[] parameters, String inFolder,
			String outFilePath) throws ProcessException {

		/* Will initiate files, directories et c. needed to run procedure,
		 * can cast exceptions */
		File[] inFiles = initiateProcedure(parameters, inFolder, outFilePath);

		ErrorLogger.log("SYSTEM", Arrays.toString(parameters));

		// printTrace(parameters, inFolder, outFilePath);
		if (fileDir.exists()) {
			//Runs bowtie on files. and sorts them.
			if (checker.shouldRunBowTie()) {
				ErrorLogger.log("SYSTEM", "Running Bowtie");
				logString = runBowTie(rawFile1, rawFile_1_Name);
				ErrorLogger.log("SYSTEM", "Bowtie error - " +logString);
				ErrorLogger.log("SYSTEM", "Finished Bowtie");

				checkBowTieFile(
						"resources/" + dir + rawFile_1_Name
						+ ".sam", rawFile_1_Name);

				//ErrorLogger.log("SYSTEM","Running SortSam");
				//sortSamFile(rawFile_1_Name);

				/*if (inFiles.length == 2) {
					logString = logString + "\n"
							+ runBowTie(rawFile2, rawFile_2_Name);

					checkBowTieFile(remoteExecution + "resources/" + dir
								+ rawFile_2_Name + ".sam", rawFile_2_Name);

					sortSamFile(rawFile_2_Name);
				}// Sets parameters for sorting first sam file
				*/

				toBeRemoved.push(remoteExecution + "resources/" + dir);
				filesToBeMoved = dir;
				toBeRemoved.push(filesToBeMoved);
			}

			if(checker.shouldRunSortSam()) {
				ErrorLogger.log("SYSTEM","Running SortSam");
				try {
					Picard.runSortSam(
							dir + rawFile_1_Name + ".sam",
							dir + rawFile_1_Name + "_sorted.sam");
				} catch (ValidateException e) {
					ErrorLogger.log("SYSTEM",
							"Error validating picard sortSam");
				} catch (InterruptedException | IOException e) {
					ErrorLogger.log("SYSTEM", "Error executing picard sortSam");
					ErrorLogger.log("SYSTEM", e.getMessage());
				}
			}

			if(checker.shouldRunRemoveDuplicates()) {
				ErrorLogger.log("SYSTEM","Running RemoveDuplicates");
				try {
					Picard.runRemoveDuplicates(
                            dir+rawFile_1_Name+"_sorted.sam",
                            dir+rawFile_1_Name + "_sorted_without_duplicates.sam"
                            );
				} catch (ValidateException e) {
					ErrorLogger.log("SYSTEM",
							"Error validating picard markDuplicates" );
				} catch (IOException | InterruptedException e) {
					ErrorLogger.log("SYSTEM",
							"Error executing picard markDuplicates");
					ErrorLogger.log("SYSTEM", e.getMessage());
				}
			}

			if(checker.shouldRunConvert()) {
				ErrorLogger.log("SYSTEM","Running .wig conversion");
				try {
					Pyicos.runConvert(
							dir + rawFile_1_Name +
							"_sorted_without_duplicates.sam");
					filesToBeMoved = dir;
					toBeRemoved.push(filesToBeMoved);
				} catch (ValidateException e) {
					ErrorLogger.log("SYSTEM",
							"Error validating pyicos conversion to .wig");
					ErrorLogger.log("SYSTEM", e.getMessage());
				} catch (InterruptedException | IOException e) {
					ErrorLogger.log("SYSTEM",
							"Error executing pyicos conversion to .wig");
				}

			}


			// Runs SamToGff script on files
			if (checker.shouldRunSamToGff()) {
				runSamToGff();
			}

			// Runs GffToAllnucsgr on files.
			if (checker.shouldRunGffToAllnusgr()) {
				runGffToAllnusgr();
			}

			// Runs smoothing on files.
			if (checker.shouldRunSmoothing()) {
				ErrorLogger.log("SYSTEM","Running Smoothing");

				// Second parameter should be false when ratio
				// calculation should not run.
				runSmoothing(parameters, false);

				filesToBeMoved = sortedDirForFile
						+ "reads_gff/allnucs_sgr/smoothed/";
				toBeRemoved.push(filesToBeMoved);
			}

			// Runs Ratio calculation on files.
			if (checker.shouldRunRatioCalc()) {
				ErrorLogger.log("SYSTEM", "Running ratio calculation");

				doRatioCalculation(sortedDirForCommands
						+ "reads_gff/allnucs_sgr/smoothed/", parameters);

				runSmoothing(parameters, true);

				toBeRemoved.push(sortedDirForFile
						+ "reads_gff/allnucs_sgr/smoothed/ratios/");
				filesToBeMoved = sortedDirForFile
						+ "reads_gff/allnucs_sgr/smoothed/ratios/smoothed/";
				toBeRemoved.push(filesToBeMoved);
			}

			try {
				ErrorLogger.log("SYSTEM", "Files to be moved are: ["+filesToBeMoved+"]");
				ErrorLogger.log("SYSTEM", "The path to move these files is: ["+outFilePath+"]");
				moveEndFiles("resources/"+filesToBeMoved, outFilePath);
			} catch (ProcessException e) {
				cleanUp(toBeRemoved);
				throw e;
			}
//			cleanUp(toBeRemoved);

		} else {

			logString = logString + " " + "Failed to create directory "
					+ fileDir.toString();
		}

		return logString;
	}

	private File[] initiateProcedure(
			String[] parameters,
			String inFolder,
			String outFilePath) throws ProcessException {
    /*If you want to run several processes simultaneously, this would need to be changed*/
		StartUpCleaner.removeOldTempDirectories("resources/");
		File[] inFiles = null;

		// Error handling
		if (inFolder == null) {
			throw new ProcessException("Fatal error: This should never happen");
		}
		inFolder = validateInFolder(inFolder);

		if (!inFolder.startsWith("/")) {
			inFolder = System.getProperty("user.dir") + "/" + inFolder;
		}

		ErrorLogger.log("SYSTEM", "Executing on infolder ["+inFolder+"]");
		inFiles = getRawFiles(inFolder);

		// Check if there are any raw files
		if (inFiles == null || inFiles.length == 0) {
			throw new ProcessException("Folder does not contain raw files");
		}

		this.parameters = parameters;
		this.inFolder = inFolder;

		// Checks all parameters that they are correct before proceeding
		if (!verifyInData(parameters, inFolder, outFilePath)
				|| !correctInfiles(inFiles)) {
			throw new ProcessException("Wrong format of input data");
		}
		// Runs the procedure.

		initiateConversionStrings(parameters, outFilePath);
		makeConversionDirectories(remoteExecution + "resources/" + dir
				+ "/sorted");
		checker.calculateWhichProcessesToRun(parameters);
		if(!validateParameters(parameters)) {
			throw new ProcessException("Parameters are incorrect");
		}
		/* Updates attribute raw files. */
		parseRawFiles(inFiles);
		initiateConversionStrings(parameters, outFilePath);
		return inFiles;
	}

	/**
	 * Parses raw files from input files
	 * @param inFiles
	 */
	private void parseRawFiles(File[] inFiles) {
		rawFile1 = inFiles[0].getName();
		rawFile_1_Name = rawFile1.substring(0, rawFile1.length() - 6);
		if (inFiles.length == 2) {
            rawFile2 = inFiles[1].getName();
            rawFile_2_Name = rawFile2.substring(0, rawFile2.length() - 6);
        }
	}

	/**
	 * Runs Gff to Allnusgr, calls and executes script.
	 *
	 * @throws ProcessException
	 */
	private void runGffToAllnusgr() throws ProcessException {
		ErrorLogger.log("SYSTEM", "Running gffToAllnucsgr");
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

	/**
	 * Runs Sam to GFF conversion, calls and executes script.
	 * @throws ProcessException
	 */
	private void runSamToGff() throws ProcessException {
		ErrorLogger.log("SYSTEM", "Running SamToGff");
		try {
            logString = logString + "\n"
                    + executeScript(parse(samToGff));

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

	/**
	 * Gets all files from the folder, throws a ProcessException if there is no
	 * files in the directory.
	 *
	 * @param inFolder
	 * @return
	 * @throws ProcessException
	 */
	private File[] getRawFiles(String inFolder) throws ProcessException {

		File[] rawDirFiles = new File(inFolder).listFiles();
		File[] rawFilesIsFile = null;
		if (rawDirFiles == null || (rawDirFiles.length == 0)) {
			throw new ProcessException(
					"No files found in directory "
					+ inFolder);
		}

		ArrayList<File> files = new ArrayList<File>();

		for (File rawDirFile: rawDirFiles) {
			if (rawDirFile.isFile()) {
				files.add(rawDirFile);
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
		return rawFilesIsFile;
	}

	/**
	 * Validates parameters.
	 *
	 * @param parameters
	 * @return
	 * @throws ProcessException
	 */
	private boolean validateParameters(String[] parameters)
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
			} else {
				return folderString;
			}
		}
		return null;
	}

	/**
	 * Runs the smoothing and step procedures. Checks if its smoothing after
	 * ratio calculation or (parameters[5])[1]) just normal smoothing. Changes
	 * some parameters depending on ratio calculation or not. Runs smoothing
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
		File dirToFiles;

		if (isRatioCalc) {
			parameterArray = parse(parameters[7]);
			stepSize = 1;
			filesToSmooth = new File(sortedDirForFile
					+ "reads_gff/allnucs_sgr/smoothed/ratios").listFiles();
			dirToFiles = new File(sortedDirForFile
					+ "reads_gff/allnucs_sgr/smoothed/ratios/smoothed");

		} else {
			filesToSmooth = new File(sortedDirForFile + "reads_gff/allnucs_sgr")
					.listFiles();
			dirToFiles = new File(sortedDirForFile + "reads_gff/allnucs_sgr/smoothed");
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


		if (!dirToFiles.exists()) {
			dirToFiles.mkdirs();
		}

		if (filesToSmooth != null) {
			for (File fileToSmooth : filesToSmooth) {
				if (fileToSmooth.isFile()
						&& isSgr(fileToSmooth.getName())) {
					String inFile = fileToSmooth.getAbsoluteFile()
							.toString();
					String outFile = fileToSmooth.getName();
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
					outFile = dirToFiles.toString() + "/" + outFile;


					// TODO: Don't hardcode path to smoothing.jar.
					ProcessBuilder pb = new ProcessBuilder("java", "-jar",
							"resources/smoothing.jar",
							parameterArray[0],
							parameterArray[1], parameterArray[2],
							parameterArray[3], parameterArray[4],
							inFile, outFile, String.valueOf(stepSize));
					try {
						Process p = pb.start();
						p.waitFor();
					} catch (IOException | InterruptedException ex) {
						throw new ProcessException(ex.getMessage());
					}

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

	private boolean correctInfiles(File[] inFiles) throws ProcessException {

		if (inFiles == null) {
			throw new ProcessException("Filepath to raw file is null");
		} else if (inFiles.length > 2 && inFiles.length < 1) {
			throw new ProcessException("Wrong quantity of raw file in Filepath");
		}

		return true;
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
		samToGff = "expect sam_to_readsgff_v1.sh " + sortedDirForCommands;
		gffToAllnusgr = "expect readsgff_to_allnucsgr_v1.sh "
				+ sortedDirForCommands + "reads_gff/";
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

		String[] bowTieParameters = parse(ServerSettings.bowtieLocation +
				" " + bowTieParams + " " + parameters[1] + " " +
				inFolder + "/" + fileOne + " " + dir + fileOneName + ".sam");

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
	 * machine only has 1 or 2 processors bowtie will run on at least one
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
		for (String param : bowTieParams) {
			bowTieString += param + " ";
		}
		return bowTieString;
	}

	/**
	 * Prints a string array, used for testing.
	 *
	 * @param s
	 * @return
	 */


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
	 *            the directory to create if it doesn't exist
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
		/* TODO Log these errors correctly */
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

		if (!checkIfFolderExists(inFolder)) {
			System.out.println("Input folder does not exist");
			return false;
		}
		if(!checkIfFolderExists(outFilePath)) {
			new File(outFilePath).mkdir();
			//return all.getCode().stavMix(KungensKurva.
			// getAllTheThings()).try(code.digest());
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

		return (new File(folder)).isDirectory();
	}

	private String runPicard(String command, String arguments) {
		/* TODO Refactor to use this? */
		return null;
	}

	/**
	 * Makes external call to Picard that removes duplicates in input .sam file.
	 *
	 * @param inputFile An input .sam file
	 * @param outputFile .sam file without duplicates
	 * @param metrics File to save data on removal of duplicates in
	 * @return Process finish status
	 * @throws ProcessException cast if execution of picard MarkDuplicates
	 * was erroneous.
	 * @throws IllegalArgumentException If input or output file was
	 * not .sam format
	 */
	private String runRemoveDuplicates(String inputFile, String outputFile,
									   String metrics) throws ProcessException {
		/* Check if input is .sam format */
		if(!inputFile.endsWith(".sam")) {
			throw new IllegalArgumentException("Could not run Picard on file: "
											   +inputFile +
											   ", as it was not in .sam format");
		}

		/* Check if output is .sam format */
		if(!outputFile.endsWith(".sam")) {
			throw new IllegalArgumentException("Could not run Picard to file: "
											   +outputFile +
											   ", as it was not in .sam format");
		}

		/* TODO Should metric file be returned/stored/used? */
		/* Set command parameters  */
		/* 	java -jar picard.jar MarkDuplicates INPUT=inputFile
			OUTPUT=outputFile REMOVE_DUPLICATES=true
			This needs to be called from the same directory as picard.jar
			or include that in the path
		*/
		String [] picardParameters = parse("java -jar " +
										   ServerSettings.picardLocation +
										   "/picard.jar MarkDuplicates " +
										   " INPUT=" + inputFile +
										   " OUTPUT=" + outputFile +
										   " REMOVE_DUPLICATES=true"+"" +
										   " METRICS_FILE=metrics.txt");
		try {
			return executeProgram(picardParameters);
		} catch (InterruptedException e) {
			throw new ProcessException(
					"Process interrupted while running picard on file: "
					+ inputFile);
		} catch (IOException e) {
			throw new ProcessException("Could not run picard on file: "
									   + inputFile + ", please check your input and permissions");
		}

	}

	private String runSortSam(String inputFile, String outputFile)
			throws ProcessException {
		/* Check if input is .sam format */
		if (!inputFile.endsWith(".sam")) {
			throw new IllegalArgumentException(
					"Could not run Picard on file: "
					+ inputFile +
					", as it was not in .sam format");
		}

		/* Check if output is .sam format */
		if (!outputFile.endsWith(".sam")) {
			throw new IllegalArgumentException(
					"Could not run Picard to file: "
					+ outputFile +
					", as it was not in .sam format");
		}

		String[] picardParameters = parse(
				"java -jar " +
				ServerSettings.picardLocation +
				"/picard.jar SortSam " +
				" I=" + inputFile +
				" O=" + outputFile +
				" SO=coordinate");
		try {
			return executeProgram(picardParameters);
		} catch (InterruptedException e) {
			throw new ProcessException(
					"Process interrupted while running picard on file: "
					+ inputFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ProcessException(
					"Could not run picard on file: " +
					inputFile +
					", please check your input and " +
					"permissions");
		}
	}


	}
