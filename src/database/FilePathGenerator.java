package database;

import java.io.File;

public class FilePathGenerator {

	private static String homeDir = "/var/www/";

	/**
	 * Used when uploading and downloading files. Returns a Dir string
	 * where the file is or where is should be saved.
	 * @param expID - the ID for the experiment.
	 * @param fileType - the type of the file.
	 * @param fileName - the name of the file.
	 * @return the string for the file.
	 */
	public static String GenerateFilePath(String expID, String fileType,
			String fileName) {

		StringBuilder dir = new StringBuilder();

		dir.append(homeDir);
		dir.append("data");
		dir.append('/');
		dir.append(expID);
		dir.append('/');
		dir.append(fileType);
		dir.append('/');
		dir.append(fileName);
		return dir.toString();
	}

	/**
	 * Used when first adding a new experiment. Creates a folder
	 * for the experiment and subfolders for files
	 *
	 * @param expID
	 *            - the ID for the experiment.
	 */
	public static void GenerateExperimentFolders(String expID) {

		File file = new File(homeDir + "/data/" + expID + "/raw/");

		file.mkdirs();

		file = new File(homeDir + "/data/" + expID + "/profile/");

		file.mkdirs();

		file = new File(homeDir + "/data/" + expID + "/region/");

		file.mkdirs();
	}
}