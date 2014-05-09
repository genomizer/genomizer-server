package database;

import java.io.File;
import java.io.IOException;

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
	@Deprecated
	public static String GenerateFilePath(String expID, String fileType,
			String fileName) {

	    fileType = fileType.toLowerCase();

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

	public static String GenerateFilePath(String expID, int fileType,
            String fileName) {

        StringBuilder dir = new StringBuilder();

        dir.append(homeDir);
        dir.append("data");
        dir.append('/');
        dir.append(expID);
        dir.append('/');

        switch (fileType) {
        case FileTuple.RAW:
            dir.append("raw");
            break;
        case FileTuple.PROFILE:
            dir.append("profile");
            break;
        case FileTuple.REGION:
            dir.append("region");
            break;
        default:
            dir.append("unknown");
            break;
        }

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

	public static String GenerateChainFilePath(String species, String fileName) {

		String path = "";
		StringBuilder dir = new StringBuilder();

		dir.append(homeDir);
		dir.append("data");
		dir.append('/');
		dir.append("genome_releases");
		dir.append('/');
		dir.append(species);
		dir.append('/');
		dir.append("chain_files");
		dir.append('/');
		dir.append(fileName);

		File chainFolder = new File(dir.toString());

		if (!chainFolder.exists()) {
			chainFolder.mkdirs();
		}

		return dir.toString();
	}

	/**
	 * Generates a filepath for where the genome release file should be stored.
	 * @param String version, the genome version of the file,
	 * @param String specie, the specie that the genome versions belongs to.
	 * @return String filePath, where the file can be stored.
	 */
	public static String GeneratePathForGenomeFiles(String version,
													String specie){

		StringBuilder dir = new StringBuilder();

		dir.append(homeDir);
		dir.append("data");
		dir.append('/');
		dir.append("genome_releases");
		dir.append('/');
		dir.append(specie);
		dir.append('/');
		dir.append(version);
		return dir.toString();
	}

	/**
	 * Used when first adding a new Genome_release type for a specific specie.
	 * Creates all folders needed for the GenomeVersion file.
	 *
	 * @param String Specie.
	 */
	public static void GenerateGenomeReleaseFolders(String specie) {

		File file = new File(homeDir + "/data/genome_releases/" + specie);
		file.mkdirs();
	}

	/**
	 * Tests if the requested filename(not whole path) is valid on the server
	 * file system or a file is already using that name. Returns true if the
	 * name can be used, false if it cannot.
	 *
	 * @param String fileName
	 * @return boolean
	 */
	public static boolean isNameOk(String fileName) {

		File file = new File(homeDir + File.separator + fileName);
		boolean isOk = false;

		if (!file.exists()) {
			try {
				isOk = file.createNewFile() ;
			} catch (IOException e) {
				isOk = false;
			}

			if (isOk) {
				file.delete();
			}

		}

		return isOk;
	}

}