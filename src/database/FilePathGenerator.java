package database;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

public class FilePathGenerator {

	public static String GenerateFilePath(String expID, String fileType, String fileName) {

		StringBuilder dir = new StringBuilder();

		File file=FileSystemView.getFileSystemView().getHomeDirectory();

		dir.append(file.toString());
		dir.append('/');
		dir.append(expID);
		dir.append('/');
		dir.append(fileType);
		dir.append('/');
		dir.append(fileName);
		return dir.toString();
	}

}