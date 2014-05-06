package process.classes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;


public abstract class Executor {

private final String FILEPATH = "resources/";

	protected String executeProgram(String[] command) throws InterruptedException, IOException{

		File pathToExecutable = new File( FILEPATH + command[0] );
		command[0]=pathToExecutable.getAbsolutePath();
		return executeCommand(command);
	}

	protected String executeScript(String[] command) throws InterruptedException, IOException{

		File pathToExecutable = new File( FILEPATH + command[1] );
		command[1]=pathToExecutable.getAbsolutePath();
		return executeCommand(command);
	}

	protected String[] parse(String procedureParameters){
		StringTokenizer paramTokenizer = new StringTokenizer(procedureParameters);
		String[] temp = new String[paramTokenizer.countTokens()];
		int i=0;
		while(paramTokenizer.hasMoreTokens()){
			temp[i] = paramTokenizer.nextToken();
			i++;
		}
		return temp;
	}

	private String executeCommand(String[] command) throws InterruptedException, IOException{
		ProcessBuilder builder = new ProcessBuilder(command);

		builder.directory( new File( FILEPATH ).getAbsoluteFile() );
		builder.redirectErrorStream(true);
		Process process =  builder.start();

		Scanner s = new Scanner(process.getInputStream());
		StringBuilder text = new StringBuilder();
		while (s.hasNextLine()) {
		  text.append(s.nextLine());
		  text.append("\n");
		}
		s.close();

		int result = process.waitFor();

//		System.out.printf( "Process exited with result %d and output %s%n", result, text );
		return text.toString();
	}

	protected String executeShellCommand(String[] command) throws InterruptedException, IOException{
		ProcessBuilder builder = new ProcessBuilder(command);

		builder.directory( new File( FILEPATH ).getAbsoluteFile() );
		builder.redirectErrorStream(true);
		Process process =  builder.start();

		Scanner s = new Scanner(process.getInputStream());
		StringBuilder text = new StringBuilder();
		File file = new File(FILEPATH+"results/sorted/sorted.sam");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		while (s.hasNextLine()) {
			bw.append(s.nextLine());
			bw.append("\n");
		}
		bw.close();
		s.close();

		int result = process.waitFor();

//		System.out.printf( "Process exited with result %d and output %s%n", result, text );
		return text.toString();
	}

}
