package command;

/***
 * Class used to store exception messages that should be thrown
 * when a command class validate method fails.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class ValidateException extends Exception {

	private int exceptionCode = 0;

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
	 * @param exception code.
	 * @param message to insert.
	 */
	public ValidateException(int exceptionCode, String message) {
		super(message);
		this.exceptionCode = exceptionCode;
	}

	/**
	 * method used to
	 * @return
	 */
	public int getCode() {
		return exceptionCode;
	}

}
