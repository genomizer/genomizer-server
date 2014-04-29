package process.classes;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public abstract class Executor {

private final String FILEPATH = "resources/";

	void executeProgram(String[] command) throws InterruptedException, IOException{

		File pathToExecutable = new File( FILEPATH + command[0] );
		command[0]=pathToExecutable.getAbsolutePath();
		executeCommand(command);
	}

	void executeScript(String[] command) throws InterruptedException, IOException{

		File pathToExecutable = new File( FILEPATH + command[1] );
		command[1]=pathToExecutable.getAbsolutePath();
		executeCommand(command);
	}

	private void executeCommand(String[] command) throws InterruptedException, IOException{
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

		System.out.printf( "Process exited with result %d and output %s%n", result, text );
	}

}
