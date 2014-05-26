package command;

/***
 * Class used to store exception messages that should be thrown
 * when a command class validate method fails.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class ValidateException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Empty constructor.
	 */
	public ValidateException() {
	}

	/**
	 * Constructor that takes a message.
	 *
	 * @param message to insert.
	 */
	public ValidateException(String message) {
		super(message);
	}

}
