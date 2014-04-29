package process.classes;

import java.io.File;
import java.io.IOException;

public class RawToProfileConverter extends Executor {

		private String[] bowTieParameters;
		private String[] samToBamParameters;
		private String[] sortBamParameters = new String[]{"samtools-0.1.19/samtools", "sort", "test/male.bam", "test/maleSorted"};
		private String[] bamTest =	new String[]{"samtools-0.1.19/samtools", "mpileup", "test/maleSorted.bam", "test/hej"};
		//Testar bowtie. Sparar ner resultaten i test.map.
//		String[] com = new String[]{"bowtie","-a","-m","1","--best","-p","10","-v","2","d_melanogaster_fb5_22","-q","reads/MOF_male_wt_reads_sample.fastq","-S","test/male.sam"};
//		String[] com = new String[]{"bowtie","e_coli", "reads/e_coli_1000.fq"};
		private String test = "samtools-0.1.19/samtools mpileup test/maleSorted.bam | perl -ne 'BEGIN{print "+'"'+"track type=wiggle_0 name=test/fileName.wig description=test/fileName.wig\n"+'"'+"};($c, $start, undef, $depth) = split; if ($c ne $lastC) { print" + '"' + "variableStep chrom=$c\n"+'"' +"; };$lastC=$c;next unless $. % 10 ==0;print"+ '"' + "$start\t$depth\n"+'"' + "unless $depth<3;'  > test/fileName.wig";
// samtools mpileup test/fileName.bam | perl -ne 'BEGIN{print "track type=wiggle_0 name=fileName description=fileName\n"};($c, $start, undef, $depth) = split; if ($c ne $lastC) { print "variableStep chrom=$c\n"; };$lastC=$c;next unless $. % 10 ==0;print "$start\t$depth\n" unless $depth<3;'  > test/fileName.wig
		public void procedure(String[] parameters) throws InterruptedException, IOException {
			bowTieParameters = parse(parameters[0]);
			samToBamParameters = new String[]{"samtools-0.1.19/samtools", "view", "-bS", "-o", "test/male.bam", "test/male.sam"};

			String[] bamToSamParameters = new String[]{"samtools-0.1.19/samtools", "view", "-h", "-o", "test/sam2.sam", "test/male.bam"};
			executeProgram(bowTieParameters);
			System.out.println("hej");
			for(int i=0; i<samToBamParameters.length; i++){
				System.out.println(samToBamParameters[i]+ " ");

			}
			executeProgram(samToBamParameters);
			executeProgram(sortBamParameters);
			executeProgram(bamToSamParameters);
			String temp = executeProgram(parse(test));
			System.out.println("temp är = " + temp);
			File bamTestFile = new File("resources/test/bamTest");
			if(!bamTestFile.exists()) {
				bamTestFile.createNewFile();
			}
			System.out.println('"' + "då"+'"');
		}

		public String[] getBowTieParameters() {
			return bowTieParameters;
		}



}