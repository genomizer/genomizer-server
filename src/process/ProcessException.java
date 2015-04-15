package process;

/**
 * Class used by the process part of the genomizer program to throw our own
 * specified exceptions.
 * 
 * v 1.0
 */
public class ProcessException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;

	/**
	 * Constructor.
	 * 
	 * @param string
	 */
	public ProcessException(String string) {
		message = string;
	}

	/**
	 * Overriden getMessage method.
	 */
	@Override
	public String getMessage() {
		return "ProcessException: "+message;
	}


}
