package command;

/***
 * Class used to store exception messages that should be thrown
 * when a command class validate method fails.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class ValidateException extends Exception {

	private int exceptionCode = 0;
	private static final long serialVersionUID = 1L;

	/**
	 * Empty constructor.
	 */
	public ValidateException() {

	}

	/**
	 * Constructor that takes a message.
	 *
	 * @param exceptionCode an int containing the exception code.
	 * @param message to insert.
	 */
	public ValidateException(int exceptionCode, String message) {

		super(message);
		this.exceptionCode = exceptionCode;

	}

	/**
	 * Method used to get the code that is set.
	 *
	 * @return the Code that is set.
	 */
	public int getCode() {

		return exceptionCode;

	}

}
