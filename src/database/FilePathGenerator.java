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

	/**
	 *
	 * @param fileID
	 * @param species
	 * @param expID
	 * @return the homo root
	 */
	@Deprecated
	public static String GenerateFilePathForCoversion(String fileID,String species, String expID){
		return "/homo/";
	}
}