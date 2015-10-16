package process;

import command.ValidateException;
import conversion.ProfileDataConverter;
import org.apache.commons.io.FilenameUtils;
import server.Debug;
import server.ErrorLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Class used to create profile data from .fastq format.
 * Can run a dynamic number of steps depending of which parameters that's sent
 * from the clients.
 *
 * @version 1.0
 */
public class RawToProfileConverter extends Executor {

	private static String tempFolder = "/tmp/genomizer/resources/";
	private String remoteExecution;
	private String dir;
	protected String inFolder;
	protected String rawFile_2_Name;
	
    /**
     * Constructor that initializes some data structures used by the class.
     */
    public RawToProfileConverter() {
    }


	/**
	 * This method might be deleted or edited to be the new procedure. Work in progress atm with Adam
	 * @param params Bowtie parameters
	 * @param fastqFile The raw file which is to be processed.
	 * @param wigFile The name of the output file produced
	 *@param keepSam Boolean telling whether intermediate .sam file should be
	 *               saved.
	 * @param genomeVersion Version of genome, not used at the moment.
	 * @param referenceGenome Path to reference genome.
	 * @param filepathRaw File path to experiment raw file directory
	 * @param filepathProfile File path to experiment profile file directory
	 * @return Processed files to save to database.
	 * @throws ProcessException
	 */
	public static File [] procedureRaw(String params, String fastqFile,
									  String wigFile, boolean keepSam,
									   boolean removeDups,
									  String genomeVersion,
									  String referenceGenome,
									  String filepathRaw,
									  String filepathProfile)
			throws ProcessException, InterruptedException, IOException {

		ArrayList<File> returnFiles = new ArrayList<>();

		/* Make sure that the output directory exists. */
		new File(filepathProfile).mkdirs();


		Stack<String> toBeRemoved = new Stack<>();
		Stack<Map.Entry<String,String>> filesToSaveToExperiment = new Stack<>();


		/*
			Create temporary directory for processing to save
			intermediate files.
		*/
		String tmpDirPath = tempFolder + "temp_" +
							Thread.currentThread().getId()+"/";

		createDirectory(tmpDirPath);


		/* Append end of paths with '/' if not present */
		filepathRaw = fixEndOfPath(filepathRaw);
		filepathProfile = fixEndOfPath(filepathProfile);


		fastqFile = filepathRaw+fastqFile;

		String samFile = tmpDirPath + FilenameUtils.getBaseName(fastqFile)+
						 ".sam";
		String sortedSam = FilenameUtils.removeExtension(samFile)
						   +"_sorted.sam";
		String sortedSamWithoutDups = FilenameUtils.removeExtension(samFile)
									  +"_sorted_without_duplicates.sam";

		ErrorLogger.log("SYSTEM", "Running Bowtie2");
		Bowtie2 bowtieProcess = new Bowtie2(
				referenceGenome, null, null,
				fastqFile, samFile, params.split(" "));

		try {
			bowtieProcess.validate();
		} catch (ValidateException e) {
			ErrorLogger.log(
					"SYSTEM", "Error validating Bowtie2 - " +
							"[" + bowtieProcess.toString() + "]");
			throw new ProcessException(e);
		}

		String bowtieReturnMessage = "NO_RETURN";
		try {
			bowtieReturnMessage = bowtieProcess.execute();
			ErrorLogger.log("SYSTEM", "Finished Bowtie2");
		} catch (InterruptedException | IOException e) {
			ErrorLogger.log(
					"SYSTEM", "Error executing Bowtie2, exited with -"
							+ " [" + bowtieReturnMessage + "]");
			throw e;
		}

		/* Run sort sam */
		try {
			ErrorLogger.log("SYSTEM","Running SortSam");
			Picard.runSortSam(samFile, sortedSam);
			toBeRemoved.push(samFile);
			ErrorLogger.log("SYSTEM", "Finished SortSam");
		} catch (ValidateException e) {
			ErrorLogger.log("SYSTEM",
					"Error validating picard sortSam");
			throw new ProcessException(e);
		} catch (InterruptedException | IOException e) {
			ErrorLogger.log("SYSTEM",
					"Error executing picard sortSam - " + e.getMessage());
			throw e;
		}

		/* Run remove duplicates */
		if(removeDups){
			try {
				ErrorLogger.log("SYSTEM","Running RemoveDuplicates");
				Picard.runRemoveDuplicates(sortedSam, sortedSamWithoutDups);
				ErrorLogger.log("SYSTEM", "Finished RemoveDuplicates");
				toBeRemoved.push(sortedSam);
			} catch (ValidateException e) {
				ErrorLogger.log("SYSTEM",
						"Error validating picard markDuplicates" );
				throw new ProcessException(e);
			} catch (IOException | InterruptedException e) {
				ErrorLogger.log("SYSTEM",
						"Error executing picard markDuplicates");
				ErrorLogger.log("SYSTEM", e.getMessage());
				throw e;
			}
		}else{
			sortedSamWithoutDups = sortedSam;
		}

		/* Run conversion from .sam to .wig */
		try {
			ErrorLogger.log("SYSTEM","Running .wig conversion");
			Pyicos.runConvert(sortedSamWithoutDups,
					tmpDirPath+FilenameUtils.getName(wigFile));
			ErrorLogger.log("SYSTEM", "Finished .wig conversion");

			filesToSaveToExperiment.push(new AbstractMap.SimpleEntry<>(
					tmpDirPath+FilenameUtils.getName(wigFile),
					filepathProfile+wigFile));
		} catch (ValidateException e) {
			ErrorLogger.log("SYSTEM",
					"Error validating pyicos conversion to .wig");
			ErrorLogger.log("SYSTEM", e.getMessage());
			throw new ProcessException(e);
		} catch (InterruptedException | IOException e) {
			ErrorLogger.log("SYSTEM",
					"Error executing pyicos conversion to .wig");
			throw e;
		}

		/* Run conversion from .wig to .sgr, final step */
		ErrorLogger.log("SYSTEM", "Running .sgr conversion");
		ProfileDataConverter pdc = new ProfileDataConverter();
		String sgrFile = pdc.wigToSgr("bed", tmpDirPath+FilenameUtils.getName(wigFile));
		filesToSaveToExperiment.push(new AbstractMap.SimpleEntry<>(
				sgrFile, filepathProfile+FilenameUtils.getName(sgrFile)));

		/* Save .sam file to experiment if wanted */
		if (keepSam) {
			ErrorLogger.log("SYSTEM", "Saving .sam file");
			filesToSaveToExperiment.push(new AbstractMap.SimpleEntry<>(
							sortedSamWithoutDups,
							filepathProfile+
							FilenameUtils.getName(sortedSamWithoutDups)));
		} else {
			toBeRemoved.push(sortedSamWithoutDups);
		}

		ErrorLogger.log("SYSTEM", "Moving files to experiment");
		ErrorLogger.log(
				"SYSTEM", "The path to move these files is: [" +
						  filepathProfile + "]");
		for (Map.Entry<String,String> filePair: filesToSaveToExperiment) {
			try {
				Debug.log("Moving: " + filePair.getKey());
				Files.move(
						Paths.get(filePair.getKey()),
						Paths.get(filePair.getValue()));
				returnFiles.add(new File(filePair.getValue()));

			} catch (IOException e) {
				ErrorLogger.log("SYSTEM", "Error moving file "+ "["+
										  filePair+"] to "+filepathProfile);
				throw e;
			}
		}

		/* Remove temporary files */
		for (String fileName: toBeRemoved) {
			File file = new File(fileName);
			Debug.log("Removing: " + file.getCanonicalPath());
			file.delete();
		}
		return returnFiles.toArray(new File[returnFiles.size()]);
	}

	private static String fixEndOfPath(String filepathRaw) {
		if (!filepathRaw.endsWith("/")) {
			filepathRaw += "/";
		}
		return filepathRaw;
	}


	/**
	 * Constructs a string with values to run a linux command that sorts a file
	 * with the specified parameters. puts a new sorted fil in a specified path.
	 *
	 * @param unsortedSamFileName
	 *            the name of the unsorted sam file
	 * @throws ProcessException
	 */
	protected void sortSamFile(String unsortedSamFileName)
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
	private static File createDirectory(String directoryPath) {
		File directory = new File(directoryPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		return directory;
	}


	}
