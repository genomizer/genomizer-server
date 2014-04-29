package process.classes;

import java.io.IOException;

public class RawToProfileConverter extends Executor {

		private String[] bowTieParameters;
		private String[] samToBamParameters;
		//Testar bowtie. Sparar ner resultaten i test.map.
//		String[] com = new String[]{"bowtie","-a","-m","1","--best","-p","10","-v","2","d_melanogaster_fb5_22","-q","reads/MOF_male_wt_reads_sample.fastq","-S","test/male.sam"};
//		String[] com = new String[]{"bowtie","e_coli", "reads/e_coli_1000.fq"};

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
			executeProgram(bamToSamParameters);
			System.out.println("då");
		}

		public String[] getBowTieParameters() {
			return bowTieParameters;
		}



}