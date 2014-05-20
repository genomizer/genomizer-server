package process.classes;

public class ProcessException extends Exception {



	private String message;
	public ProcessException(String string) {
		message = string;
	}

	@Override
	public String getMessage() {
		return "ProcessException: "+message;
	}


}
