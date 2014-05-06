package response;

import com.google.gson.annotations.Expose;

public class AddFileToExperimentResponse extends Response {

	@Expose
	private String URLupload;

	public AddFileToExperimentResponse(int code, String URLupload) {
		this.code = code;
		
		this.URLupload = URLupload;
		// create URL from filepath
		// http//:....:8090/html/ + filepath
		// this.filepath = fileURL;
	}

}
