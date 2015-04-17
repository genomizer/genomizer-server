package authentication;

/**
 * This class creates an object that describes details of an attempted login
 *
 * @author
 *
 */
public class LoginAttempt {

	private boolean successful;
	private String uuid;
	private String error_message;

	/**
	 * Method sets the LoginAttempt specs
	 *
	 * @param successful boolean if the attempt is successful
	 * @param uuid of the attempted login
	 * @param error_message of the login attempt
	 */
	public LoginAttempt(boolean successful, String uuid, String error_message) {
		this.successful = successful;
		this.uuid = uuid;
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
		return uuid;
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
