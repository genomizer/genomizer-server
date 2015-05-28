package response;

import com.google.gson.annotations.Expose;

/**
 * Class that represents the login response.
 *
 * @author Business Logic
 * @version 1.0
 */
public class LoginResponse extends Response {

	@Expose
	private String token;

	@Expose
	private String role;


	/**
	 * Creator for the response.	
	 * @param token The login token to be returned.
	 * @param userType the user rights
	 */
	public LoginResponse(String token, String userType) {
		this.code = HttpStatusCode.OK;
		this.token = token;
		this.role = userType;
	}

	/**
	 * Getter for the login token
	 * @return The login token as a String
	 */
	public String getToken() {

		return token;
	}
}
