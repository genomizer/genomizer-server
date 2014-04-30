package response;

import com.google.gson.annotations.Expose;

public class AddFileToExperimentResponse extends Response {

	@Expose
	private String filepath;

	public AddFileToExperimentResponse(int code, String filepath) {
		this.code = code;
		this.filepath = filepath;
	}

}
