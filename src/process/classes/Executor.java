package process.classes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

import server.Debug;
import server.ErrorLogger;

/**
 * Class that is abstract and contains methods that all analysis needs to use.
 *
 * v 1.0
 */
public abstract class Executor {

	static final String DIRECTORY = "resources/";

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

		File pathToExecutable = new File(DIRECTORY + command[0]);
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

		File pathToExecutable = new File(DIRECTORY + command[1]);
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

		// Build a system process
		ProcessBuilder builder = new ProcessBuilder(command);

		builder.directory(new File(DIRECTORY).getAbsoluteFile());
		builder.redirectErrorStream(true);
		Process process = builder.start();

		Scanner s = new Scanner(process.getInputStream());
		StringBuilder text = new StringBuilder();

		// Get process output
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

		// Build a system process
		ProcessBuilder builder = new ProcessBuilder(command);

		builder.directory(new File(DIRECTORY).getAbsoluteFile());
		builder.redirectErrorStream(true);
		Process process = builder.start();

		Scanner s = new Scanner(process.getInputStream());
		StringBuilder text = new StringBuilder();
		File dirFile = new File(DIRECTORY + dir);

		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		File file = new File(dirFile.toString() + "/" + fileName);
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		// Get process output
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

			// Delete files in the directory
			if (file.isDirectory()) {
				File[] fileList = file.listFiles();

				for (int i = 0; i < fileList.length; i++) {
					Debug.log("File " + fileList[i].toString() + "is being deleted");

					if (fileList[i].isFile()) {
						if (!fileList[i].delete()) {
							isOk = false;
							Debug.log("Failed to delete file "+fileList[i].toString());
							ErrorLogger.log("SYSTEM", "Failed to delete file "+fileList[i].toString());
							//throw new ProcessException("Failed to delete file "+fileList[i].toString());
						}
					}
				}
			}

			// Delete the file/directory
			Debug.log("File " + file.toString() + "is being deleted");
			if (!file.delete()) {
				isOk = false;
				Debug.log("Failed to delete file "+file.toString());
				ErrorLogger.log("SYSTEM", "Failed to delete file "+file.toString());
				//throw new ProcessException("Failed to delete directory "+file.toString());
			}
		}
		return isOk;
	}

	/**
	 * Moves files from dirToFiles to dest.
	 *
	 * @param orgDir
	 *            directory where files are.
	 * @param destDir
	 *            directory where files will be moved.
	 * @throws ProcessException
	 */

	protected void moveEndFiles(String orgDir, String destDir) throws ProcessException {

		File[] filesInDir = new File(orgDir).getAbsoluteFile().listFiles();

		if (filesInDir != null) {

			if (filesInDir.length == 0) {
				throw new ProcessException(
						"No files were generated. If you are running " +
						"ratio calculation, make sure the name is correct");
			} else {
				for (int i = 0; i < filesInDir.length; i++) {
					if (!filesInDir[i].isDirectory()) {
						if (filesInDir[i].renameTo(new File(destDir
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
		//System.out.println(dirToCheck);
		File fileDirToCheck = new File(dirToCheck);
		File[] filesInDir = fileDirToCheck.listFiles();

		if (filesInDir == null || filesInDir.length == 0) {
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
