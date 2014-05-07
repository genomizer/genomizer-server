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
	public void procedure(String[] parameters, String inFolder, String outFile)
			throws InterruptedException, IOException {
		File[] inFiles = new File("resources/"+inFolder).getAbsoluteFile().listFiles();
		String fileOne;
		String fileTwo;
		String fileOneName;
		String fileTwoName;
		
		fileOne = inFiles[0].getName();
		fileTwo = inFiles[1].getName();
		fileOneName = fileOne.substring(0, fileOne.length()-6);
		fileTwoName = fileTwo.substring(0, fileTwo.length()-6);

		// String dir = String.valueOf((Thread.currentThread().getId())) + "/";
		String dir = "results/";
		String sortedDir = dir + "sorted/";
		String bowTieOutput = "beforeSorted.sam";
		String outString;

		File fileDir = new File("resources/" + dir);
		// deleteDir(fileDir);
		System.out.println("Deletes directory");

		System.out.println("dir " + fileDir.toString());
		if (!fileDir.exists()) {
			fileDir.mkdir();
		}

		if (fileDir.exists()) {

			String sortSam; // Step 2
//			 = "sort " + dir + bowTieOutput + " -k 3,3 -k 4,4n";		
			
			String samToGff = "perl sam_to_readsgff_v1.pl " + sortedDir; // Step 3
			String gffToAllnusgr = "perl readsgff_to_allnucsgr_v1.pl "
					+ sortedDir + "reads_gff/"; // Step 4
			String smooth = "perl smooth_v4.pl " + sortedDir
					+ "reads_gff/allnucs_sgr/ 10 1 5 0 0"; // Step 5
			String step10 = "perl AllSeqRegSGRtoPositionSGR_v1.pl y 10 "
					+ sortedDir + "reads_gff/allnucs_sgr/smoothed/"; // Step 6
			String sgr2wig = "perl sgr2wig.pl " + sortedDir
					+ "/reads_gff/allnucs_sgr/smoothed/Step10/*.sgr " + outFile
					+ "test.wig"; // Step 7

			
			
			long startTime;
			long endTime;
			long allTimeStart;
			long allTimeEnd;

			//Sets parameters for first bowtie execute
			bowTieParameters = parse(parameters[0] + " " + inFolder+"/"+fileOne + " " +dir
					+ fileOneName + ".sam");

			startTime = System.currentTimeMillis();
			allTimeStart = startTime;
			outString = executeProgram(bowTieParameters);
			endTime = System.currentTimeMillis();
			System.out.println("bowtie done, Time: " + ((endTime - startTime))
					+ " milliseconds");

			//Sets parameters for second bowtie execute
			bowTieParameters = parse(parameters[0] + " " + inFolder+"/"+fileTwo + " " +dir
					+ fileTwoName + ".sam");
			
			startTime = System.currentTimeMillis();
			allTimeStart = startTime;
			outString = executeProgram(bowTieParameters);
			endTime = System.currentTimeMillis();
			System.out.println("bowtie done, Time: " + ((endTime - startTime))
					+ " milliseconds");

			String newString;
			startTime = System.currentTimeMillis();
			/*
			 * Process p = Runtime.getRuntime().exec(sortSam); BufferedReader
			 * reader = new BufferedReader(new
			 * InputStreamReader(p.getInputStream())); String line = "";
			 * System.out.println("NUSKRIVER VIH�ER"); while ((line =
			 * reader.readLine()) != null) { System.out.println("hej"+line); }
			 */
			
			//Sets parameters for sorting first sam file
			sortSam = "sort " + dir + fileOne + " -k 3,3 -k 4,4n";
			newString = executeShellCommand(parse(sortSam), dir + "sorted/",
					fileOne+"_sorted.sam");
			
			//Sets parameters for sorting second sam file
			sortSam = "sort " + dir + fileTwo + " -k 3,3 -k 4,4n";
			newString = executeShellCommand(parse(sortSam), dir + "sorted/",
					fileTwo+"_sorted.sam");
			
			System.out.println(newString);
			endTime = System.currentTimeMillis();
			System.out.println("sortsam done, Time: " + ((endTime - startTime))
					+ " milliseconds");

			startTime = System.currentTimeMillis();
			outString = outString + " " + executeScript(parse(samToGff));
			endTime = System.currentTimeMillis();
			System.out.println("readgff done, Time: " + ((endTime - startTime))
					+ " milliseconds");

			startTime = System.currentTimeMillis();
			outString = outString + " " + executeScript(parse(gffToAllnusgr));
			endTime = System.currentTimeMillis();
			System.out.println("readsgff to allnucsgr done, Time: "
					+ ((endTime - startTime)) + " milliseconds");

			startTime = System.currentTimeMillis();
			outString = outString + " " + executeScript(parse(smooth));
			endTime = System.currentTimeMillis();
			System.out.println("smooth done, Time: " + ((endTime - startTime))
					+ " milliseconds");

			
			startTime = System.currentTimeMillis();
			outString = outString + " " + executeScript(parse(step10));
			endTime = System.currentTimeMillis();
			System.out.println("step10 done, Time: " + ((endTime - startTime))
					+ " milliseconds");

			//Parameters for first sgr2wig execution
			sgr2wig = "perl sgr2wig.pl " + sortedDir
					+ "/reads_gff/allnucs_sgr/smoothed/Step10/*.sgr " + outFile+"/"+fileOneName
					+ ".wig"; // Step 7
			
			startTime = System.currentTimeMillis();
			outString = outString + " " + executeScript(parse(sgr2wig));
			endTime = System.currentTimeMillis();
			allTimeEnd = endTime;
			System.out.println("sgr2wig done, Time: " + ((endTime - startTime))
					+ " milliseconds");
			
			//TODO kan inte skicka in *.sgr när det finns mer än en fil till detta script
			//Parameters for second sgr2wig execution
			sgr2wig = "perl sgr2wig.pl " + sortedDir
					+ "/reads_gff/allnucs_sgr/smoothed/Step10/*.sgr " + outFile+"/"+fileTwoName
					+ ".wig"; // Step 7
			
			startTime = System.currentTimeMillis();
			outString = outString + " " + executeScript(parse(sgr2wig));
			endTime = System.currentTimeMillis();
			allTimeEnd = endTime;
			System.out.println("sgr2wig done, Time: " + ((endTime - startTime))
					+ " milliseconds");
			
			if(allTimeEnd-allTimeStart > 60000) {
				System.out.println("All done, took "+getTime(allTimeEnd-allTimeStart));
			}
			System.out.println(outString);
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
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