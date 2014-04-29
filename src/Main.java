import java.io.IOException;

import process.classes.Executor;
import process.classes.ProcessCommand;

//Class to make initial commit
public class Main {


	public static void main(String [ ] args) {
		String path = "test/male.sam";
		String bowTie = "bowtie -a -m 1 --best -p 10 -v 2 d_melanogaster_fb5_22 -q reads/MOF_male_wt_reads_sample.fastq -S " +path;
//		String[] sortSam = new String[]{"samtools-0.1.19/samtools", "sort", "test/male.sam", "test/maleSortedSam"};
		String samToBam = "samtools-0.1.19/samtools view -bS -o test/male.bam test/male.sam";

		String[] sortBam = new String[]{"samtools-0.1.19/samtools", "sort", "test/male.bam", "test/maleSorted"};
		//samtools view -bS -o /path/*.bam /path/*.sam"
		ProcessCommand p = new ProcessCommand("rawToProfile", new String[]{bowTie, samToBam});
		try {
			p.execute();
		} catch (IllegalArgumentException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//e.specificParamProcedure(bowTie);
		//e.specificParamProcedure(samToBam);
//		e.specificParamProcedure(sortBam);
//		e.specificParamProcedure(sortSam);



	}

}
