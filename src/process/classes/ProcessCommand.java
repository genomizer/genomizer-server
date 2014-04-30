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

	public String execute() throws IllegalArgumentException, InterruptedException, IOException {
		switch(processName){
			case "rawToProfile":
				RawToProfileConverter rawToProfileConverter = new RawToProfileConverter();
//				rawToProfileConverter.procedure(parameters);
				break;
			case "profileToRegion":
				// TODOOO
			default: throw new IllegalArgumentException();
		}
		return null;
	}
}
