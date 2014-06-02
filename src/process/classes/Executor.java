package process.classes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * Class that is abstract and contains methods that all analysis needs to use.
 *
 * v 1.0
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
	protected String executeProgram(String[] command)
			throws InterruptedException, IOException {

		File pathToExecutable = new File(FILEPATH + command[0]);
		command[0] = pathToExecutable.getAbsolutePath();
		return executeCommand(command);
	}

	/**
	 * Used to execute a script
	 *
	 * @param command
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected String executeScript(String[] command)
			throws InterruptedException, IOException {

		File pathToExecutable = new File(FILEPATH + command[1]);
		command[1] = pathToExecutable.getAbsolutePath();
		return executeCommand(command);
	}

	/**
	 * Used to parse a string and make it into a String array
	 *
	 * @param procedureParameters
	 * @return
	 */
	protected String[] parse(String procedureParameters) {
		StringTokenizer paramTokenizer = new StringTokenizer(
				procedureParameters);
		String[] parsedString = new String[paramTokenizer.countTokens()];
		int i = 0;
		while (paramTokenizer.hasMoreTokens()) {
			parsedString[i] = paramTokenizer.nextToken();
			i++;
		}
		return parsedString;
	}

	/**
	 * Used to execute commands
	 *
	 * @param command
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private String executeCommand(String[] command)
			throws InterruptedException, IOException {
		ProcessBuilder builder = new ProcessBuilder(command);

		builder.directory(new File(FILEPATH).getAbsoluteFile());
		builder.redirectErrorStream(true);
		Process process;
		process = builder.start();

		Scanner s = new Scanner(process.getInputStream());
		StringBuilder text = new StringBuilder();
		while (s.hasNextLine()) {
			text.append(s.nextLine());
			text.append("\n");
		}
		s.close();

		int result = process.waitFor();

		// System.out.printf( "Process exited with result %d and output %s%n",
		// result, text );
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
	protected String executeShellCommand(String[] command, String dir,
			String fileName) throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder(command);

		builder.directory(new File(FILEPATH).getAbsoluteFile());
		builder.redirectErrorStream(true);
		Process process;
		process = builder.start();

		Scanner s = new Scanner(process.getInputStream());
		StringBuilder text = new StringBuilder();
		File dirFile = new File(FILEPATH + dir);

		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File file = new File(dirFile.toString() + "/" + fileName);
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		while (s.hasNextLine()) {
			bw.append(s.nextLine());
			bw.append("\n");
		}
		bw.close();
		s.close();

		int result = process.waitFor();

		// System.out.printf( "Process exited with result %d and output %s%n",
		// result, text );
		return text.toString();
	}

	/**
	 * Removes a list of folders and their content.
	 *
	 * @param files
	 * @return
	 * @throws ProcessException
	 */
	protected boolean cleanUp(Stack<String> files) throws ProcessException {
		boolean isOk = true;
		while (!files.isEmpty()) {
			File file = new File(files.pop());
			if (file.isDirectory()) {
				File[] fileList = file.listFiles();
				for (int i = 0; i < fileList.length; i++) {
					System.out.println("nu tas " + fileList[i].toString()
							+ " bort");
					if (fileList[i].isFile()) {
						if (!fileList[i].delete()) {
							isOk = false;
							System.out.println("Failed");
							//throw new ProcessException("Failed to delete file "+fileList[i].toString());
						}
					}
				}
			}
			System.out.println("nu tas " + file.toString() + " bort");
			if (!file.delete()) {
				isOk = false;
				System.out.println("Failed to delete directory");
				//throw new ProcessException("Failed to delete directory "+file.toString());
			}
		}
		return isOk;
	}

	/**
	 * Moves files from dirToFiles to dest.
	 *
	 * @param dirToFiles
	 *            directory where files are.
	 * @param dest
	 *            directory where files will be moved.
	 * @throws ProcessException
	 */
	protected void moveEndFiles(String dirToFiles, String dest) throws ProcessException {
		File[] filesInDir = new File(dirToFiles).getAbsoluteFile().listFiles();
		if (filesInDir != null) {
			if(filesInDir.length == 0) {
				throw new ProcessException("No files were generated. If you are running ratio calculation, make sure the name is correct");
			} else {
				for (int i = 0; i < filesInDir.length; i++) {
					if (!filesInDir[i].isDirectory()) {
						if (filesInDir[i].renameTo(new File(dest
								+ filesInDir[i].getName())));
					}
				}
			}
		}
	}

	/**
	 * Checks if a a analysis step is executed correctly and made a file.
	 *
	 * @param dirToCheck
	 * @return
	 */
	protected boolean checkStep(String dirToCheck) {
		System.out.println(dirToCheck);
		File fileDirToCheck = new File(dirToCheck);
		File[] filesInDir = fileDirToCheck.listFiles();

		if (filesInDir == null) {
			return false;
		} else if (filesInDir.length == 0) {
			return false;
		} else if (filesInDir.length == 1 && filesInDir[0].isDirectory()) {
			return false;
		} else if (filesInDir.length >= 1) {
			for (int i = 0; i < filesInDir.length; i++) {
				if (!filesInDir[i].isDirectory()) {
					if (filesInDir[i].length() == 0) {
						return false;
					}
				}
			}

		}
		return true;
	}
}