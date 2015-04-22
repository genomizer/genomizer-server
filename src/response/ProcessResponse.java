package response;

import java.util.Date;

import com.google.gson.annotations.Expose;

/**
 * Class that represents the response when processing.
 *
 * @author
 * @version 1.0
 */
public class ProcessResponse extends Response {

	@Expose
	private String message;
	@Expose
	private Date date;


	/**
	 * Creator for the response
	 * @param code The return code for the response
	 */
	public ProcessResponse (int code) {
		this.code = code;
		message = null;
		date = new Date(System.currentTimeMillis());
	}

	/**
	 * Creator for the response which also includes a message
	 * @param code The return code for the response
	 * @param message The attached message
	 */
	public ProcessResponse (int code, String message) {
		this.code = code;
		this.message = message;
		date = new Date(System.currentTimeMillis());

	}

}
