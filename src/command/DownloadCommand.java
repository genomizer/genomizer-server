package command;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

/**
 * Class used to represent a command of the type Download.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class DownloadCommand extends Command {

	//@SerializedName("attr1")
	@Expose
	private String attr1;

	//@SerializedName("attr2")
	@Expose
	private String attr2;

	//@SerializedName("attr3")
	@Expose
	private String attr3;

	//@SerializedName("attr4")
	@Expose
	private String attr4;

	/**
	 * Empty constructor.
	 */
	public DownloadCommand() {

	}

	/**
	 * Used to validate the correctness of the
	 * class when built.
	 */
	@Override
	public boolean validate() {

		// Check if file exists
//		if(!fileExists(fileID)) {
//			return false;
//		} else {
			return true;
//		}
	}

	/**
	 * Runs the actual code that is used to execute the
	 * download.
	 */
	@Override
	public Response execute() {

		Response rsp = rsp;
		ArrayList<String> attributes = new ArrayList<String>();

		if(!fileExists(fileID)) {
			attributes = getFileAttributes(fileID);
			rsp = new DownloadResponse(200, attributes);
		} else {
			rsp = new FNFResponse();
		}

//		String fileName = getFileName(fileID);
//		String size = getFileSize(fileID);
//		String type = getFileType(fileID);
//		String URL = getFileURL(fileID);

		return rsp;
	}


	/* Hashcodes(auto-generated) used temporary for testing purposes.
	 * Might be removed later.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attr1 == null) ? 0 : attr1.hashCode());
		result = prime * result + ((attr2 == null) ? 0 : attr2.hashCode());
		result = prime * result + ((attr3 == null) ? 0 : attr3.hashCode());
		result = prime * result + ((attr4 == null) ? 0 : attr4.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DownloadCommand other = (DownloadCommand) obj;
		if (attr1 == null) {
			if (other.attr1 != null)
				return false;
		} else if (!attr1.equals(other.attr1))
			return false;
		if (attr2 == null) {
			if (other.attr2 != null)
				return false;
		} else if (!attr2.equals(other.attr2))
			return false;
		if (attr3 == null) {
			if (other.attr3 != null)
				return false;
		} else if (!attr3.equals(other.attr3))
			return false;
		if (attr4 == null) {
			if (other.attr4 != null)
				return false;
		} else if (!attr4.equals(other.attr4))
			return false;
		return true;
	}

}
