package process.classes;

public class ProcessCommand {

	private String processName;
	private String [] parameters;


	public ProcessCommand(String processName,String bowTieParams) {
		this.processName = processName;
		this.parameters[0] = bowTieParams;
	}

//	Future Sprint
//	private ProcessCommand(String processName,String bowTieParams, String ratioCalcParams) {
//		this.processName = processName;
//		this.parameters[0] = bowTieParams;
//		this.parameters[1] =  ratioCalcParams;
//	}

	public String execute() throws IllegalArgumentException {
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
