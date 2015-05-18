package response;

import com.google.gson.annotations.Expose;

/**
 * Class that represents the login response.
 *
 * @author
 * @version 1.0
 */
public class LoginResponse extends Response {

	@Expose
	private String token;

	@Expose
	private String role;


	/**
	 * Creator for the response.
	 * @param code The return code for the response.
	 * @param token The login token to be returned.
	 */
	public LoginResponse(int code, String token) {

		this.code = code;
		this.token = token;
		this.role = "role";
	}

	/**
	 * Getter for the login token
	 * @return The login token as a String
	 */
	public String getToken() {

		return token;
	}
}
