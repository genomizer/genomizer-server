package response;

import java.util.Date;

import com.google.gson.annotations.Expose;

public class ProcessResponse extends Response {

	@Expose
	String message;
	@Expose
	private Date date;

	public ProcessResponse (int code) {
		this.code = code;
		date = new Date(System.currentTimeMillis());
	}
	public ProcessResponse (int code, String message) {
		this.code = code;
		this.message = message;
		date = new Date(System.currentTimeMillis());

	}

}
