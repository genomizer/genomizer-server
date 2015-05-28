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
	 * @param UrlUpload URL for the experiment to upload
	 */
	public UrlUploadResponse(String UrlUpload) {
		this.code = HttpStatusCode.OK;
		this.URLupload = UrlUpload;
	}

}
