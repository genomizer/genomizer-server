package process.classes;

import java.io.File;
import java.io.IOException;

/**
 * Class used to create .wig file from .fastq format.
 * 
 * @version 1.0
 */
public class RawToProfileConverter extends Executor {

		private String[] bowTieParameters; 		//Step 1
		private String sortSam = "sort -k 3,3 -k 4,4n test/*.sam"; //Step 2
		private String samToGff = "perl sam_to_readsgff_v1.pl test/"; //Step 3
		private String gffToAllnusgr  = "perl readsgff_to_allnucsgr_v1.pl test/reads_gff/"; //Step 4
		private String smooth = "perl smooth_v4.pl test/reads_gff/allnucs_sgr/ 10 1 5 0 0";  //Step 5
		private String step10 = "perl AllSeqRegSGRtoPositionSGR_v1.pl y 10 test/reads_gff/allnucs_sgr/smoothed/"; // Step 6
		private String sgr2wig = "perl sgr2wig.pl male_v1_v1_median_smooth_winSiz-10_minProbe-5_step10.sgr step10.wig"; //Step 7
		
		
		/**
		 * Takes a string array that contains bowtie parameters and then
		 * follows a procedure.
		 *  
		 * 1. runs the bowtie program to get a .sam file.
		 * 2. runs a linux shell command to sort the sam file.
		 * 3. runs a perl script that creates a .ggf file from the sam file.
		 * 4. runs a perl script that creates a .sgr file from the .gff file.
		 * 5. runs a perl script to smooth the .sgr file.
		 * 6. runs a perl script to convert the .sgr file to a .sgr with fixed
		 * size.
		 * 7. runs a perl script that converts the .sgr file to .wig file.
		 * 
		 * 
		 * 
		 * @param parameters
		 * @param inFile
		 * @param outFile
		 * @throws InterruptedException
		 * @throws IOException
		 */
		public void procedure(String[] parameters, String inFile, String outFile) throws InterruptedException, IOException {
			bowTieParameters = parse(parameters[0]);
			
			long startTime;
			long endTime;
			
			startTime = System.currentTimeMillis();
			executeProgram(bowTieParameters);
			endTime = System.currentTimeMillis();
			System.out.println("bowtie done, Time: "+((endTime - startTime)) + " milliseconds");
			
			Thread.sleep(2000);
			
			startTime = System.currentTimeMillis();
			executeScript(parse(sortSam));
			endTime = System.currentTimeMillis();
			System.out.println("sortsam done, Time: "+((endTime - startTime)) + " milliseconds");
			
			Thread.sleep(2000);
			
			startTime = System.currentTimeMillis();
			executeScript(parse(samToGff));
			endTime = System.currentTimeMillis();
			System.out.println("readgff done, Time: "+((endTime - startTime)) + " milliseconds");
			
			Thread.sleep(2000);
			
			startTime = System.currentTimeMillis();
			executeScript(parse(gffToAllnusgr));
			endTime = System.currentTimeMillis();
			System.out.println("readsgff to allnucsgr done, Time: "+((endTime - startTime)) + " milliseconds");
			
			Thread.sleep(2000);
			
			startTime = System.currentTimeMillis();
			executeScript(parse(smooth));
			endTime = System.currentTimeMillis();
			System.out.println("smooth done, Time: "+((endTime - startTime)) + " milliseconds");
			
			Thread.sleep(2000);
			
			startTime = System.currentTimeMillis();
			executeScript(parse(step10));
			endTime = System.currentTimeMillis();
			System.out.println("step10 done, Time: "+((endTime - startTime)) + " milliseconds");
			
			Thread.sleep(2000);
			
			startTime = System.currentTimeMillis();
			executeScript(parse(sgr2wig));
			endTime = System.currentTimeMillis();
			System.out.println("sgr2wig done, Time: "+((endTime - startTime)) + " milliseconds");

			
//			for(int i=0; i<samToBamParameters.length; i++){
//				System.out.println(samToBamParameters[i]+ " ");
//
//			}
//			
			
//			
//			executeScript(parse(readgff));
//			System.out.println("Efter readgff");

//			executeProgram(samToBamParameters);
//			executeProgram(sortBamParameters);
//			String temp = executeProgram(parse(test));
			File bamTestFile = new File("resources/test/bamTest");
			if(!bamTestFile.exists()) {
				bamTestFile.createNewFile();
			}
			System.out.println('"' + "d�"+'"');
		}

		public String[] getBowTieParameters() {
			return bowTieParameters;
		}
}