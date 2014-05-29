package process.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StartUpCleaner {

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
						} catch (IOException e) {
						}

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
