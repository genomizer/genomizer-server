package server;

import command.ProcessCommand;
import command.ProcessStatus;
import response.Response;
import response.HttpStatusCode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.*;


public class ProcessHandler implements Callable<Response> {

	private ProcessCommand processCommand;
	private ProcessStatus processStatus;


	public ProcessHandler(ProcessCommand processCommand,
						  ProcessStatus processStatus) {
		this.processCommand = processCommand;
		this.processStatus = processStatus;
	}


	@Override
	public Response call() {

		Response response = null;

		if (processCommand != null && processStatus != null) {
			Debug.log("Executing process in experiment "
					+ processCommand.getExpId());

			processStatus.status = ProcessStatus.STATUS_STARTED;

			// Attempt to setup file paths
			try {
				processCommand.setFilePaths();
			} catch (SQLException | IOException e) {
				Debug.log(e.getMessage());
				ErrorLogger.log(processStatus.author,
						"Could not run process command: " + e.getMessage());
				processStatus.status = ProcessStatus.STATUS_CRASHED;
				return null;
			}

			processStatus.outputFiles = processCommand.getFilePaths();
			processStatus.timeStarted = System.currentTimeMillis();


			try {
				/* Execute the process command */
				response = processCommand.execute();

				if (response.getCode() == HttpStatusCode.CREATED) {
					processStatus.status = ProcessStatus.STATUS_FINISHED;
					Debug.log("Process execution in experiment " +
							processCommand.getExpId() + " has finished!");
				} else {
					processStatus.status = ProcessStatus.STATUS_CRASHED;
					Debug.log("FAILURE! Process execution in experiment " +
							processCommand.getExpId() + " has crashed.");
				}
			} catch (NullPointerException e) {
				processStatus.status = ProcessStatus.STATUS_CRASHED;
			}

			processStatus.timeFinished = System.currentTimeMillis();

		}

		return response;

	}


}

