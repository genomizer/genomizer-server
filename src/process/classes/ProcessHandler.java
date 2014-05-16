package src.process.classes;


import java.io.IOException;

public class ProcessHandler {
	public ProcessHandler createProcessHandler() {
		return new ProcessHandler();
	}
	//SPRINT 1: Let procedureParams be empty!
	public String executeProcess(String processName, String[] procedureParams, String inFile, String outFile) throws InterruptedException, IOException {
		switch(processName){
		case "rawToProfile":
			RawToProfileConverter rawToProfileConverter = new RawToProfileConverter();
			System.out.println("JORÅ!");
			rawToProfileConverter.procedure(procedureParams, inFile, outFile);
			break;
		case "profileToRegion":
			// TODOOO
			break;
		default: throw new IllegalArgumentException();
		}
		return null;
	}
}
