package unused;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

/**
 * Class for validating filename
 *
 * @author yhi04jeo
 */
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
	 * @param String fileName
	 * @return boolean
	 */
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
}