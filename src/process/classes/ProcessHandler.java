package process.classes;

public class ProcessHandler {

	private ProcessHandler() {

	}
	public static ProcessHandler createProcessHandler(){
		return new ProcessHandler();
	}

	public void runExecutable(String process, String[] param, String inFilePath, String outFilePath) throws ProcessException {

	}

	public void runConversion(String conversion, String inFilePath, String outFilePath){

	}



}
