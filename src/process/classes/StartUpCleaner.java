package process.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class StartUpCleaner{


	public static void removeOldTempDirectories(String dir) {
		if(dir != null) {
			System.out.println("dir var inte null");
			File dirFile = new File(dir);
			if(dirFile.isDirectory()) {
				File[] listOfFiles = dirFile.listFiles();
				for (File file : listOfFiles) {
					System.out.println(file.toString());
					if(file.getName().startsWith("results_") && file.isDirectory()) {
								try {
									delete(file);
								} catch (IOException e) {
									System.out.println("IO");
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
