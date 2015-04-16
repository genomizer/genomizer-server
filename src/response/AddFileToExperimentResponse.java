package response;

import com.google.gson.annotations.Expose;

/**
 * Response to a command which adds a file to the experiment
 */
public class AddFileToExperimentResponse extends Response {

	@Expose
	private String URLupload;

	public AddFileToExperimentResponse(int code, String URLupload) {
		this.code = code;
		this.URLupload = URLupload;
	}

}
