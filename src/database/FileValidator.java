package database;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

public class FileValidator {

	private String home = FileSystemView.getFileSystemView()
			.getHomeDirectory().toString();

	public FileValidator() {

	}

	/**
	 * Tests if the requested filename(not whole path) is valid on the server
	 * file system or a file is already using that name. Returns true if the
	 * name can be used, false if it cannot.
	 *
	 * @param  fileName
	 * @return boolean
	 */
	@Deprecated
	public boolean isNameOk(String fileName) {

		File file = new File(home + File.separator + fileName);
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

	public static final String validFileNameChars = "[-\\w,\\s\\.åäöÅÄÖ()]";
	public static final String validFileExtChars  = "[A-ZÅÄÖa-zåäö0-9]";

	public static boolean fileNameCheck(String fileName){
		String regex = "^" + validFileNameChars + "+\\." + validFileExtChars + "+$";
		return fileName.matches(regex);
	}
}