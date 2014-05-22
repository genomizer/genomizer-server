package authentication;


public class LoginAttempt {

	private boolean successful;
	private String uuid;
	private String error_message;

	public LoginAttempt(boolean successful, String uuid, String error_message) {
		this.successful = successful;
		this.uuid = uuid;
		this.error_message = error_message;
	}

	public boolean wasSuccessful() {
		return successful;
	}

	public String getUUID() {
		return uuid;
	}

	public String getErrorMessage() {
		return error_message;
	}

}
