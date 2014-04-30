package process.classes;

import java.io.File;
import java.io.IOException;

public class RawToProfileConverter extends Executor {

		private String[] bowTieParameters;
		private String[] samToBamParameters;
		private String[] sortBamParameters = new String[]{"samtools-0.1.19/samtools", "sort", "test/male.bam", "test/maleSorted"};
		public void procedure(String[] parameters, String inFile, String outFile) throws InterruptedException, IOException {
			bowTieParameters = parse(parameters[0]);
			samToBamParameters = new String[]{"samtools-0.1.19/samtools", "view", "-bS", "-o", "test/male.bam", "test/male.sam"};

			executeProgram(bowTieParameters);
			System.out.println("hej");
			for(int i=0; i<samToBamParameters.length; i++){
				System.out.println(samToBamParameters[i]+ " ");

			}
			executeProgram(samToBamParameters);
			executeProgram(sortBamParameters);
//			String temp = executeProgram(parse(test));
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