package process;
/**
 * Class that removes temporary processing directories. Should be called when the server is started.
 */
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
			if (dirFile.isDirectory()) {
				File[] listOfFiles = dirFile.listFiles();
				for (File file : listOfFiles) {
					if (file.getName().startsWith("results_")
							&& file.isDirectory()) {
						try {
							System.out.println("Deleting temp directory " + file.getName());
							delete(file);

						} catch (IOException e) {}


					}

				}
			}
		}

	}

	private static void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles()) {
				delete(c);
			}
			f.delete();
		} else {
			f.delete();
		}

	}
}
