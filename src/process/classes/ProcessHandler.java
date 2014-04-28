package process.classes;

import java.util.HashMap;

public class ProcessHandler {
	private ProcessHandler() {

	}
	public static ProcessHandler createProcessHandler(){
		return new ProcessHandler();
	}

	public String runExecutable(String process, String[] param, String inFilePath, String outFilePath) throws IllegalArgumentException {
		switch(process){
			case "rawToProfile":
				return "rawToProfile";
			default: throw new IllegalArgumentException();
		}

	}

	public String runConversion(String conversion, String inFilePath, String outFilePath){

		return null;
	}




}
