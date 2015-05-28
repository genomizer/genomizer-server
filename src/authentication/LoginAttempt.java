package authentication;

import server.Debug;

/**
 * This class creates an object that describes details of an attempted login
 *
 * @author
 *
 */
public class LoginAttempt {

	private boolean successful;
	private String username;
	private String error_message;

	/**
	 * Method which sets the LoginAttempt specifications
	 *
	 * @param successful whether the attempt was successful or not.
	 * @param username username of the attempted login.
	 * @param error_message error message of the login attempt if any.
	 */
	public LoginAttempt(boolean successful, String username, String error_message) {
		Debug.log((successful ? "Successful" : "Unsuccessful") 
				+ " login attempt, username: " + username);
		this.successful = successful;
		this.username = username;
		this.error_message = error_message;
	}

	/**
	 * Getter for attribute successful
	 *
	 * @return boolean successful
	 */
	public boolean wasSuccessful() {
		return successful;
	}

	/**
	 * Getter for attribute uuid
	 *
	 * @return string uuid
	 */
	public String getUUID() {
		return username;
	}

	/**
	 * Getter for attribute error_message
	 *
	 * @return string error_message
	 */
	public String getErrorMessage() {
		return error_message;
	}

}
