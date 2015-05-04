package response;

import com.google.gson.annotations.Expose;

/**
 * Class that represents the error response.
 *
 * @author
 * @version 1.0
 */
public class ErrorResponse extends Response {
	
	@Expose
	private String message;

	/**
	 * Creator for the error response
	 * @param code The return code of the response
	 * @param message The error message
	 */
	public ErrorResponse(int code, String message) {

		this.code = code;
		this.message = message;
	}

	/**
	 * Getter for the error message
	 * @return The error message as a String
	 */
	public String getMessage() {

		return message;
	}
}
