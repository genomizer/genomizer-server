package process.classes;

import java.io.IOException;

public class ProcessCommand {

	private String processName;
	private String [] parameters;

	public ProcessCommand(String processName,String[] procedureParams) {
		parameters = new String[3];
		this.processName = processName;
		this.parameters = procedureParams;
	}

//	Future Sprint
//	private ProcessCommand(String processName,String bowTieParams, String ratioCalcParams) {
//		this.processName = processName;
//		this.parameters[0] = bowTieParams;
//		this.parameters[1] =  ratioCalcParams;
//	}

	public String execute() throws IllegalArgumentException, InterruptedException, IOException {
		switch(processName){
			case "rawToProfile":
				RawToProfileConverter rawToProfileConverter = new RawToProfileConverter();
				rawToProfileConverter.procedure(parameters);
			case "profileToRegion":
				// TODOOO
			default: throw new IllegalArgumentException();
		}
	}
}
