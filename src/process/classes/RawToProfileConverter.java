package process.classes;

import java.io.File;
import java.io.IOException;

public class RawToProfileConverter extends Executor {

		private String[] bowTieParameters;
		private String[] samToBamParameters;
		
//		String bowTie = "bowtie -a -m 1 --best -p 10 -v 2 d_melanogaster_fb5_22 -q reads/MOF_male_wt_reads_sample.fastq -S test/male.sam";
		private String sortSam = "sort -k 3,3 -k 4,4n test/*.sam";
		private String readgff = "perl sam_to_readsgff_v1.pl test/";
		private String readsgff_to_allnucsgr_v1  = "perl readsgff_to_allnucsgr_v1.pl test/reads_gff/";
		private String smooth = "perl smooth_v4.pl test/reads_gff/allnucs_sgr/ 10 1 5 0 0";
		private String step10 = "perl AllSeqRegSGRtoPositionSGR_v1.pl y 10 test/reads_gff/allnucs_sgr/smoothed/";
		private String sgr2wig = "perl sgr2wig.pl test/reads_gff/allnucs_sgr/smoothed/step10/male_v1_v1_median_smooth_winSiz-10_minProbe-5_step10.sgr test/reads_gff/allnucs_sgr/smoothed/step10/step10.wig";
		
		private String[] sortBamParameters = new String[]{"samtools-0.1.19/samtools", "sort", "test/male.bam", "test/maleSorted"};
		public void procedure(String[] parameters, String inFile, String outFile) throws InterruptedException, IOException {
			bowTieParameters = parse(parameters[0]);
			samToBamParameters = new String[]{"samtools-0.1.19/samtools", "view", "-bS", "-o", "test/male.bam", "test/male.sam"};

			executeProgram(bowTieParameters);
			System.out.println("bowtie done");
			Thread.sleep(2000);
			executeScript(parse(sortSam));
			System.out.println("sortsam done");
			Thread.sleep(2000);
			executeScript(parse(readgff));
			System.out.println("readgff done");
			Thread.sleep(2000);
			executeScript(parse(readsgff_to_allnucsgr_v1));
			System.out.println("readsgff to allnucsgr done");
			Thread.sleep(2000);
			executeScript(parse(smooth));
			System.out.println("smooth done");
			
			Thread.sleep(2000);
			executeScript(parse(step10));
			System.out.println("step10 done");
			
			Thread.sleep(2000);
			executeScript(parse(sgr2wig));
			System.out.println("sgr2wig done");

			
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