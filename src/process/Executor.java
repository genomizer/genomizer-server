package process;

import server.ErrorLogger;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.AccessControlException;
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
	 * @throws RuntimeException
	 */
	protected String executeProgram(String[] command)
			throws InterruptedException, IOException, RuntimeException {

		File pathToExecutable = new File(FILEPATH + command[0]);

		/* Checks if program can be executed, does not execute, throws excp. */
		isExecutable(pathToExecutable);

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
	 * @throws RuntimeException
	 */
	protected String executeScript(String[] command)
			throws InterruptedException, IOException, RuntimeException {

		File pathToExecutable = new File(FILEPATH + command[1]);

		/* Checks if script can be executed, does not execute, throws excp.*/
		isExecutable(pathToExecutable);

		command[1] = pathToExecutable.getAbsolutePath();
		return executeCommand(command);

	}

	/**
	 * Checks if given executable exists and that execution rights are correct.
	 * Does NOT execute the given program.
	 * @param executable Executable file
	 * @throws IOException If the file does not exists
	 * @throws AccessControlException If execute permissions are not present
	 */
	private void isExecutable(File executable)
			throws IOException, AccessControlException{
		if(!executable.exists()) {
			throw new FileNotFoundException(
					String.format("Runnable [%s] does not exist", executable));
		}

		if(!executable.canExecute()) {
			throw new AccessControlException(
					"No permission to execute "+executable);
		}

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


		Scanner processOutput = new Scanner(process.getInputStream());
		StringBuilder results = new StringBuilder();
		while (processOutput.hasNextLine()) {
			results.append(processOutput.nextLine());
			results.append("\n");
		}
		processOutput.close();


		process.waitFor();

		/* Check if command finished successfully */
		if(process.exitValue() != 0) {
			throw new RuntimeException(results.toString());
		}

		// System.out.printf( "Process exited with result %d and output %s%n",
		// result, text );
		return results.toString();
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

		// Create a system process
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.directory(new File(FILEPATH).getAbsoluteFile());
		builder.redirectErrorStream(true);
		Process process = builder.start();

		// Construct a reader to read data from process
		Scanner s = new Scanner(process.getInputStream());
		StringBuilder text = new StringBuilder();
		File dirFile = new File(FILEPATH + dir);

		// Create directory if it does not exist
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		// Construct a writer to output data to file
		File file = new File(dirFile.toString() + "/" + fileName);
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		// Write data to file
		while (s.hasNextLine()) {
			bw.append(s.nextLine()+"\n");
		}

		bw.close();
		s.close();

		// Wait for the process to finish
		process.waitFor();

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

			// If it is a directory, delete contents first
			if (file.isDirectory()) {

				// Save file references into an array
				File[] fileList = file.listFiles();

				// Delete each file with a reference in the array
				for (int i = 0; i < fileList.length; i++) {
					if (fileList[i].isFile()) {

						if (fileList[i].delete()) {
							ErrorLogger.log("SYSTEM", "Deleting "
									+ fileList[i].toString());
							//throw new ProcessException("Failed to delete file "+fileList[i].toString());
						} else {
							isOk = false;
							ErrorLogger.log("SYSTEM", "Deletion of " +
									fileList[i] +	" failed.");
						}
					}
				}
			}

			// Delete the file/directory
			if (file.delete()) {
				ErrorLogger.log("SYSTEM", "Deleting " + file.toString());
				//throw new ProcessException("Failed to delete directory "+file.toString());
			} else {
				isOk = false;
				ErrorLogger.log("SYSTEM", "Failed to delete directory " + file);
			}
		}

		return isOk;
	}

	/**
	 * Moves files from orgDir to destDir.
	 *
	 * @param orgDir
	 *            directory where files are.
	 * @param destDir
	 *            directory where files will be moved.
	 * @throws ProcessException
	 */

	protected void moveEndFiles(String orgDir, String destDir)
				throws ProcessException {

		// Save references to files in original directory into an array
		File[] filesInDir = new File(orgDir).getAbsoluteFile().listFiles();

		if (filesInDir != null) {
			if (filesInDir.length == 0) {
				throw new ProcessException("No files were generated. " +
						"If you are running ratio calculation, " +
						"make sure the name is correct");
			} else {
				for (int i = 0; i < filesInDir.length; i++) {


					// Path to source file
					Path sourcePath = FileSystems.getDefault().getPath
							(orgDir, filesInDir[i].getName());

					// Path to target file
					Path targetPath = FileSystems.getDefault().getPath
							(destDir, filesInDir[i].getName());

					// Move file if it is not a directory
					if (!filesInDir[i].isDirectory()) {
						try {
							Files.move(sourcePath, targetPath,
									StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							ErrorLogger.log("SYSTEM", "Could not move file "
									+ filesInDir[i].getName() + " from " +
									sourcePath.toString() + " to " +
									targetPath.toString());
						}
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
