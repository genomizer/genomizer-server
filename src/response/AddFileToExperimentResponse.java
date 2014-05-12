package response;

import com.google.gson.annotations.Expose;

public class AddFileToExperimentResponse extends Response {

	@Expose
	private String filepath;

	public AddFileToExperimentResponse(int code, String filepath) {
		this.code = code;
		// create URL from filepath
		// http//:....:8090/html/ + filepath
		// this.filepath = fileURL;
	}

}
