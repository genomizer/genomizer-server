package response;

import com.google.gson.annotations.Expose;

/**
 * Class that represents the login response.
 */
public class LoginResponse extends Response {

	@Expose
	public String token;

	public LoginResponse(int code, String token) {
		this.code = code;
		this.token = token;
	}

}
