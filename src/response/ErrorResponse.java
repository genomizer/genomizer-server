package response;

import com.google.gson.annotations.Expose;

public class ErrorResponse extends Response {
	
	@Expose
	private String message;
	
	public ErrorResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	
	
}
