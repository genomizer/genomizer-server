package command;

/***
 * Class used to store exception messages that should be thrown
 * when a command class validate method fails.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class ValidateException extends Exception {
	private int exceptionCode = 0;
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new instance of ValidateException using the supplied
	 * exception code and message.
	 * @param exceptionCode the exception code.
	 * @param message the exception message.
	 */
	public ValidateException(int exceptionCode, String message) {
		super(message);
		this.exceptionCode = exceptionCode;
	}

	/**
	 * Returns the code of the exception.
	 * @return the exception code.
	 */
	public int getCode() {
		return exceptionCode;
	}
}
