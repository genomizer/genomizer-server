package response;

import com.google.gson.annotations.Expose;

/**
 * Response to a command which adds a file to the experiment
 */
public class UrlUploadResponse extends Response {

	@Expose
	String URLupload;

	/**
	 * Constructor for the response
	 * @param code The return code
	 * @param UrlUpload URL for the experiment to upload
	 */
	public UrlUploadResponse(int code, String UrlUpload) {

		this.code = code;
		this.URLupload = UrlUpload;
	}

}
