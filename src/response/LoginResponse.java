package response;

import com.google.gson.annotations.Expose;

public class LoginResponse extends Response {

	@Expose
	private String token;

	public LoginResponse(int code, String token) {
		this.code = code;
		this.token = token;
	}

}
