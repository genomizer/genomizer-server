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

	private String remoteExecution;
	private String dir;
	private String sortedDir;
	private String sortSam;
	private String samToGff;
	private String gffToAllnusgr;
	private String smooth;
	private String step10;
	private String sgr2wig;
	private File fileDir;
	private String inFolder;
	private String[] parameters;
	private String outFilePath;
	private String rawFile1;
	private String rawFile2;
	private String rawFile_1_Name;
	private String rawFile_2_Name;
	private String logString;
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
	public String procedure(String[] parameters, String inFolder, String outFilePath)
			throws InterruptedException, IOException {
		File[] inFiles = new File("/"+inFolder).getAbsoluteFile().listFiles();
		this.parameters = parameters;
		this.outFilePath = outFilePath;
		this.inFolder = inFolder;

		if(inFiles.length == 0) {
			return "No files found at path " + inFolder;
		} else if(inFiles.length > 2) {
			return "Experiment contains more than two raw files";
		}
		rawFile1 = inFiles[0].getName();
		rawFile_1_Name = rawFile1.substring(0, rawFile1.length()-6);

		if(inFiles.length == 2) {
			rawFile2 = inFiles[1].getName();
			rawFile_2_Name = rawFile2.substring(0, rawFile2.length()-6);
		}
		initiateConversionStrings(parameters, outFilePath);
		System.out.println("Deletes directory");

		makeConversionDirectories(remoteExecution + dir+"/sorted");

		printTrace(parameters, inFolder, outFilePath);

		if (fileDir.exists()) {
			logString = runBowTie(rawFile1, rawFile_1_Name);
			sortSamFile(rawFile_1_Name);
			if(inFiles.length == 2) {
				logString = logString + "\n"+ runBowTie(rawFile2, rawFile_2_Name);
				//Sets parameters for sorting second sam file
				sortSamFile(rawFile_2_Name);
			}//Sets parameters for sorting first sam file
			logString = logString + "\n" + executeScript(parse(samToGff));
			logString = logString + "\n" + executeScript(parse(gffToAllnusgr));
			logString = logString + "\n" + executeScript(parse(smooth));
			logString = logString + "\n" + executeScript(parse(step10));
			if(parameters.length == 4) {
				moveEndFiles(sortedDir + "reads_gff/allnucs_sgr/smoothed/Step10", outFilePath);
			} else {
				doRatioCalculation();
			}
			/*
			//Parameters for first sgr2wig execution
			sgr2wig = "perl sgr2wig.pl " + sortedDir
					+ "/reads_gff/allnucs_sgr/smoothed/Step10/"+fileOne +" "+ outFile+fileOneName
					+ ".wig"; // Step 7
			outString = outString + " " + executeScript(parse(sgr2wig));*/
			//cleanUp(cleanUpInitiator(remoteExecution+dir));
			return logString;
		} else {
			return "Failed to create directory " + fileDir.toString();
		}
	}
	private void doRatioCalculation() {
		// TODO Auto-generated method stub

	}
	private void printTrace(String[] parameters, String inFolder, String outFile) {
		System.out.println("dir " + fileDir.toString());
		System.out.println("INFOLDER = " + inFolder);
		System.out.println("OUTFILE = " + outFile);
		System.out.println("DIR = " + dir);
		System.out.println("SORTEDDIR = " + sortedDir);
		System.out.println("BOWTIE = " + parse(parameters[0] + " " + inFolder+"/"+rawFile1 + " " +dir
					+ rawFile_1_Name + ".sam"));
	}
	private String runBowTie(String fileOne, String fileOneName) throws InterruptedException, IOException {
		String[] bowTieParameters = parse("bowtie " + parameters[0] + " " + parameters[1] + " " + inFolder+"/"+fileOne + " " + remoteExecution + dir
				+ fileOneName + ".sam");

		printStringArray(bowTieParameters);
		return executeProgram(bowTieParameters);

	}

	private String printStringArray(String[] s) {
		String string = "";
		for(int i = 0; i < s.length; i++) {
			string += s[i]+" ";
		}
		System.out.println(string);
		return string;
	}

	private void sortSamFile(String unsortedSamFileName) throws InterruptedException, IOException {
		sortSam = "sort " + remoteExecution+dir + unsortedSamFileName +".sam"+ " -k 3,3 -k 4,4n";
	executeShellCommand(parse(sortSam), remoteExecution+dir + "sorted/",
			unsortedSamFileName + "_sorted.sam");

	}
	private void makeConversionDirectories(String directoryPath) {
		fileDir = new File(directoryPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

	}
	private void initiateConversionStrings(String[] parameters, String outFile) {
		remoteExecution = "/scratch/resources/";
		dir = "results_"+Thread.currentThread().getId()+"/";
		sortedDir =  remoteExecution +dir + "sorted/";
		samToGff = "perl sam_to_readsgff_v1.pl " + sortedDir; // Step 3
		gffToAllnusgr = "perl readsgff_to_allnucsgr_v1.pl "
				+ sortedDir + "reads_gff/"; // Step 4
		smooth = "perl smooth_v4.pl " + sortedDir
				+ "reads_gff/allnucs_sgr/ " + parameters[2] ; // Step 5
		step10 = "perl AllSeqRegSGRtoPositionSGR_v1.pl" + parameters[3] + " "
				+ sortedDir + "reads_gff/allnucs_sgr/smoothed/"; // Step 6
		sgr2wig = "perl sgr2wig.pl " + sortedDir
				+ "/reads_gff/allnucs_sgr/smoothed/Step10/*.sgr " + outFile
				+ "test.wig";
	}

	private void moveEndFiles(String dirToFiles, String dest) {
		//sortedDir+"reads_gff/allnucs_sgr/smoothed/Step10/"
		File[] filesInDir = new File("/"+dirToFiles).getAbsoluteFile().listFiles();
		for(int i=0;i<filesInDir.length;i++) {
			if(!filesInDir[i].isDirectory()){
				if(filesInDir[i].renameTo(new File(dest + filesInDir[i].getName())));
			}

		}


	}



}