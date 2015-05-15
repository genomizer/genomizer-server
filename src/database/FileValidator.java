package database;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

public class FileValidator {

	private String home = FileSystemView.getFileSystemView()
			.getHomeDirectory().toString();

	public FileValidator() {}

	public static final String validFileNameChars = "[-\\w,\\s\\.åäöÅÄÖ()]";
	public static final String validFileExtChars  = "[A-ZÅÄÖa-zåäö0-9]";

	public static boolean fileNameCheck(String fileName){
		String regex = "^" + validFileNameChars + "+\\." + validFileExtChars + "+$";
		return fileName.matches(regex);
	}
}