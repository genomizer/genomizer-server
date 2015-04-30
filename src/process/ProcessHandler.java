package process;

import command.ProcessCommand;
import server.ErrorLogger;

/**
 * Class that acts as a handler for the procedure and calculation part of the
 * program. the rest of the serverside should always go through this class when
 * using Process classes.
 * 
 */
public class ProcessHandler {

	public ProcessHandler createProcessHandler() {
		return new ProcessHandler();
	}

	/**
	 * Executes a process depending of which type it is.
	 * 
	 * @param processName
	 * @param procedureParams
	 * @param inFile
	 * @param outFile
	 * @return
	 * @throws ProcessException
	 */
	public String executeProcess(String processName, String[] procedureParams,
			String inFile, String outFile) throws ProcessException {
		String logString = "";
		switch (processName) {
			case ProcessCommand.CMD_RAW_TO_PROFILE:
				RawToProfileConverter rawToProfileConverter =
						new RawToProfileConverter();
				logString = rawToProfileConverter.procedure(procedureParams,
						inFile, outFile);
				
				ErrorLogger.log("SYSTEM","Process: "+logString);
				break;
			case ProcessCommand.CMD_PROFILE_TO_REGION:
				// TODO
				break;
			default:
				throw new IllegalArgumentException("Command "+processName
												   +" not found.");
			}
			return logString;
	}
}
