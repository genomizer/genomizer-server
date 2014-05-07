package process.classes;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;


/**
 * Class that is abstract and contains methods that all analysis needs to use.
 *
 */
public abstract class Executor {

private final String FILEPATH = "resources/";
	
	/**
	 * Used to execute a program like bowtie
	 * 
	 * @param command
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	protected String executeProgram(String[] command) throws InterruptedException, IOException{

		File pathToExecutable = new File( FILEPATH + command[0] );
		command[0]=pathToExecutable.getAbsolutePath();
		return executeCommand(command);
	}
	
	/**
	 * Used to execute a script
	 * 
	 * @param command
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	protected String executeScript(String[] command) throws InterruptedException, IOException{

		File pathToExecutable = new File( FILEPATH + command[1] );
		command[1]=pathToExecutable.getAbsolutePath();
		return executeCommand(command);
	}
	
	/**
	 * Used to parse a string and make it into a String array
	 * 
	 * @param procedureParameters
	 * @return
	 */
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
	
	/**
	 * Used to execute commands
	 * 
	 * @param command
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
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

	/**
	 * Used to execute shell command
	 * 
	 * @param command
	 * @param dir
	 * @param fileName
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	protected void executeShellCommand(String[] command, String dir, String fileName) throws InterruptedException, IOException{
		ProcessBuilder builder = new ProcessBuilder(command);

		builder.directory( new File( FILEPATH ).getAbsoluteFile() );
		builder.redirectErrorStream(true);
		Process process =  builder.start();

		Scanner s = new Scanner(process.getInputStream());
		StringBuilder text = new StringBuilder();
		File dirFile = new File(FILEPATH+dir);

		if(!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File file = new File(dirFile.toString()+"/"+fileName);
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
	
	/**
	 * Used to make a File object which represents a folder.
	 * 
	 * @param dirName
	 * @return
	 */
	protected File cleanUpInitiator(String dirName) {

		return new File(dirName.substring(0, dirName.length()-1));

	}
	
	/**
	 * Deletes a folder and all its subfolders.
	 * 
	 * @param dir
	 * @return
	 */
	protected static boolean cleanUp(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = cleanUp(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

}
