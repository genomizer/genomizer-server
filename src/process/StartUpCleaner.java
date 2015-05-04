package process;
/**
 * Class that removes temporary processing directories. Should be called when the server is started.
 */
import server.ErrorLogger;

import java.io.File;
import java.io.IOException;

public class StartUpCleaner {
	/**
	 * Static method that removes temporary processing directories in a specified directory.
	 * @param dir The directory where the temporary directories can be found
	 */
	public static void removeOldTempDirectories(String dir) {
		if (dir != null) {

			File dirFile = new File(dir);

			// Make sure the argument is a directory
			if (dirFile.isDirectory()) {
				File[] listOfFiles = dirFile.listFiles();

				for (File file : listOfFiles) {
					if (file.getName().startsWith("results_")
							&& file.isDirectory()) {
						try {
							ErrorLogger.log("SYSTEM", "Deleting temp " +
									"directory " + file.getName());
							delete(file);

						} catch (IOException e) {
							ErrorLogger.log("SYSTEM", "Could not delete file "
									+ file.getName());
						}


					}

				}
			}
		}

	}

	private static void delete(File f) throws IOException {
		// Delete contents if it is a folder
		if (f.isDirectory()) {
			for (File c : f.listFiles()) {
				delete(c);
			}
		}
		// Delete the file/folder
		f.delete();
	}
}
