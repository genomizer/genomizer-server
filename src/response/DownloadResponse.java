package response;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

/**
 * Class that represents the download response.
 *
 * @author
 * @version 1.0
 */
public class DownloadResponse extends Response {

	@Expose
	private String experimentID;
	@Expose
	private String fileName;
	@Expose
	private String size;
	@Expose
	private String type;
	@Expose
	private String URL;

	/**
	 * Creator for the download response
	 * @param code The return code for the response
	 * @param attributes An ArrayList containing the attributes for the file
	 *                   to download.
	 */
	//Currently unused
	public DownloadResponse(int code, ArrayList<String> attributes) {
		this.code = code;
		experimentID = attributes.get(0);
		fileName = attributes.get(1);
		size = attributes.get(2);
		type = attributes.get(3);
		URL = attributes.get(4);
	}

	/**
	 * Getter for the experiment id
	 * @return Experiment id as a String
	 */
	public String getExperimentID() {
		return experimentID;
	}

	/**
	 * Getter for the filename
	 * @return The filename as a String
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Getter for the file size
	 * @return The file size as a String
	 */
	public String getSize() {
		return size;
	}

	/**
	 * Getter for the type of the file
	 * @return The type as a String
	 */
	public String getType() {
		return type;
	}

	/**
	 * Getter for the URL of the file
	 * @return The URL as a String
	 */
	public String getURL() {
		return URL;
	}
}
