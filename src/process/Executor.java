package process;

import command.Command;
import database.DatabaseAccessor;
import database.containers.FileTuple;
import server.Debug;
import server.ErrorLogger;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.AccessControlException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;


/**
 * Class that is abstract and contains methods that all analysis needs to use.
 *
 * v 1.0
 */
public abstract class Executor {

	/* Assumes execution is done from project root */
	private final String FILEPATH = "";
	private DatabaseAccessor db;

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

		File pathToExecutable = new File(FILEPATH+command[0]);


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
		Debug.log("[Executor.executeScript] Path to executable: "
				+ pathToExecutable.toString());


		command[1] = pathToExecutable.getAbsolutePath();
		Debug.log("[Executor.executeScript] Executing the command: "
				+ Arrays.toString(command));
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
	protected String executeCommand(String[] command)
			throws InterruptedException, IOException {
                command = prependNiceCommand(command);
		ProcessBuilder builder = new ProcessBuilder(command);

		builder.directory(new File(FILEPATH).getAbsoluteFile());
		builder.redirectErrorStream(true);
		
		String commandString = "";
		for ( String arg : command) {
			commandString += arg + " ";
		}
		ErrorLogger.log("SYSTEM", "Command ["+commandString+"]: "+
								  builder.directory().getAbsolutePath());

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
			ErrorLogger.log("SYSTEM", "CWD: "+(System.getProperty("user.dir")));
			throw new RuntimeException(results.toString());
		}

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

		return text.toString();
	}

	/**
	 * Removes a list of folders and their content.
	 *
	 * @param files
	 * @return
	 * @throws ProcessException
	 */
	protected boolean cleanUp(Stack<String> files) {

		/* TODO should this be replaced with FileUtils.deleteDirectory?*/

		boolean isOk = true;

		try {
			db = Command.initDB();

			while (!files.isEmpty()) {

				File file = new File(files.pop());

				// If it is a directory, delete contents first
				if (file.isDirectory()) {

					// Save file references into an array
					File[] fileList = file.listFiles();

					// Delete each file with a reference in the array
					isOk = deleteFiles(fileList);
				}

				// Delete the file/directory
				if (file.delete()) {
					ErrorLogger.log("SYSTEM", "Deleting " + file.toString());
				} else {
					isOk = false;
					ErrorLogger.log("SYSTEM", "Failed to delete directory " + file);
				}
			}

		} catch (SQLException | IOException ex) {
			ErrorLogger.log("SYSTEM", "Executor::cleanUp: could not connect " +
					"to the database");
			Debug.log("Executor::cleanUp: could not connect " +
				"to the database");
			return false;
		} finally {
			if (db != null && db.isConnected()) {
				db.close();
			}
		}


		return isOk;
	}

	/**
	 * Helper method that deletes given files. Doesn't delete directory files.
	 *
	 * @param fileList List of files to delete
	 * @return False if one or more files wasn't deleted, true otherwise.
	 */
	private boolean deleteFiles(File[] fileList) throws SQLException, IOException{
		boolean isOk = true;
		if (fileList != null){
			for (File fileToDelete : fileList) {
				if (fileToDelete.isFile()) {
					if (fileToDelete.delete()) {
						FileTuple fileTuple = db.getFileTuple(fileToDelete
								.getAbsolutePath());
						db.deleteFile(fileTuple.path);
						ErrorLogger.log("SYSTEM", "Deleting "
										  + fileToDelete.toString());
					} else {
						isOk = false;
						ErrorLogger.log("SYSTEM", "Deletion of " +
										  fileToDelete + " failed.");
					}
				}
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
		ErrorLogger.log("SYSTEM", new File(orgDir).getAbsolutePath());

		if (filesInDir != null) {
			if (filesInDir.length == 0) {
				throw new ProcessException("No files were generated. " +
						"If you are running ratio calculation, " +
						"make sure the name is correct");
			} else {
				for (File endFile : filesInDir) {
					ErrorLogger.log("SYSTEM", "Moving file: ["+endFile.getAbsolutePath()+"]");

					// Path to source file
					Path sourcePath = FileSystems.getDefault().getPath
							(orgDir, endFile.getName());

					// Path to target file
					Path targetPath = FileSystems.getDefault().getPath
							(destDir, endFile.getName());

					// Move file if it is not a directory
					if (!endFile.isDirectory()) {
						try {
							Files.move(sourcePath, targetPath,
									StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							ErrorLogger.log("SYSTEM", "Could not move file "
									+ endFile.getName() + " from " +
									sourcePath.toString() + " to " +
									targetPath.toString());
						}
					}
				}
			}
		}
	}

	/**
	 * Checks if an analysis step has executed correctly and produced a file.
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
			for (File file : filesInDir) {
				if (!file.isDirectory()) {
					if (file.length() == 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

        private String[] prependNiceCommand(String[] command) {
		ArrayList<String> temp = new ArrayList<String>(Arrays.asList(command));
		temp.add(0, "19");
		temp.add(0, "-n");
		temp.add(0, "nice");
		command = temp.toArray(new String[temp.size()]);
                return command;
        }
}
