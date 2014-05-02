import java.io.IOException;

import process.classes.Executor;
import process.classes.ProcessHandler;

//Class to make initial commit
public class Main {

	// String bowTie =
	// "bowtie -a -m 1 --best -p 10 -v 2 d_melanogaster_fb5_22 -q reads/MOF_male_wt_reads_sample.fastq -S test/male.sam";
	// private String[] sortBamParameters = new
	// String[]{"samtools-0.1.19/samtools", "sort", "test/male.bam",
	// "test/maleSorted"};
	// String[] sortSam = new String[]{"samtools-0.1.19/samtools", "sort",
	// "test/male.sam", "test/maleSortedSam"};
	// String samToBam =
	// "samtools-0.1.19/samtools view -bS -o test/male.bam test/male.sam";
	// String[] sortBam = new String[]{"samtools-0.1.19/samtools", "sort",
	// "test/male.bam", "test/maleSorted"};
	// String bamToWig =
	// "samtools pileup fileName.bam | perl -ne 'BEGIN{print "track
	// type=wiggle_0 name=fileName
	// description=fileName\n"};($c, $start, undef, $depth) = split; if ($c ne $lastC) { print "variableStep
	// chrom=$c\n"; };$lastC=$c;next unless $. % 10 ==0;print "$start\t$depth\n" unless $depth<3;'  > fileName.wig"
	// samtools view -bS -o /path/*.bam /path/*.sam"

	public static void main(String[] args) {

		String genome = "d_melanogaster_fb5_22";
		String bowTie = "bowtie -a -m 1 --best -p 10 -v 2 -q  -S " + genome;
		String inFile = "reads/MOF_male_wt_reads.fastq";
		String outFile = "/home/shinowa/Videos/step10";
		ProcessHandler p = new ProcessHandler();
		String[] para = new String[] { bowTie };
		try {
			System.out.println("nu executar vi");

			p.executeProcess("rawToProfile", para, inFile, outFile);
			System.out.println("nu har vi executat");
		} catch (IllegalArgumentException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
