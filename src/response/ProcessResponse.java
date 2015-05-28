package response;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * Class that represents the response when processing.
 *
 * @author Business Logic
 * @version 1.0
 */
public class ProcessResponse extends Response {

	@Expose
	String message;
	@Expose
	Date date;


	/**
	 * Creator for the response where the message is null.
	 * @param code The return code for the response.
	 */
	public ProcessResponse (int code) {
		this.code = code;
		message = null;
		date = new Date(System.currentTimeMillis());
	}

	/**
	 * Creator for the response which also includes a message.
	 * @param code The return code for the response.
	 * @param message The attached message.
	 */
	public ProcessResponse (int code, String message) {

		this.code = code;
		this.message = message;
		date = new Date(System.currentTimeMillis());
	}

	public String getMessage() {
		return message;
	}

	public Date getDate() {
		return date;
	}
}
