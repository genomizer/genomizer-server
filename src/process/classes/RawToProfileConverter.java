package process.classes;

import java.io.File;
import java.io.IOException;

/**
 * Class used to create .wig file from .fastq format.
 * 
 * @version 1.0
 */
public class RawToProfileConverter extends Executor {

		private String[] bowTieParameters;
		private String sortSam = "sort -k 3,3 -k 4,4n test/*.sam";
		private String readgff = "perl sam_to_readsgff_v1.pl test/";
		private String readsgfftoallnuscgr  = "perl readsgff_to_allnucsgr_v1.pl test/reads_gff/";
		private String smooth = "perl smooth_v4.pl test/reads_gff/allnucs_sgr/ 10 1 5 0 0";
		private String step10 = "perl AllSeqRegSGRtoPositionSGR_v1.pl y 10 test/reads_gff/allnucs_sgr/smoothed/";
		private String sgr2wig = "perl sgr2wig.pl male_v1_v1_median_smooth_winSiz-10_minProbe-5_step10.sgr step10.wig";
		
		
		/**
		 * Takes a string array with execution parameters for each part of 
		 * the procedure which creates a .wig file from a .fastq file.
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
			executeScript(parse(readgff));
			endTime = System.currentTimeMillis();
			System.out.println("readgff done, Time: "+((endTime - startTime)) + " milliseconds");
			
			Thread.sleep(2000);
			
			startTime = System.currentTimeMillis();
			executeScript(parse(readsgfftoallnuscgr));
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