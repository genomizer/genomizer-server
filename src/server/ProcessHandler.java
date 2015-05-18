package server;


import command.process.PutProcessCommand;
import command.Process;
import response.Response;
import response.HttpStatusCode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.*;


public class ProcessHandler implements Callable<Response> {

	private PutProcessCommand processCommand;
	private Process process;


	public ProcessHandler(PutProcessCommand processCommand,
						  Process process) {
		this.processCommand = processCommand;
		this.process = process;
	}


	@Override
	public Response call() {

		Response response = null;

		if (processCommand != null && process != null) {
			Debug.log("Execution of process with id " + processCommand.getPID()
					+ " in experiment "
					+ processCommand.getExpId() + "has begun.");

			process.status = Process.STATUS_STARTED;

			// Attempt to setup file paths
			try {
				processCommand.setFilePaths();
			} catch (SQLException | IOException e) {
				Debug.log(e.getMessage());
				ErrorLogger.log(process.author,
						"Could not run process command: " + e.getMessage());
				process.status = Process.STATUS_CRASHED;
				return null;
			}

			process.outputFiles = processCommand.getFilePaths();
			process.timeStarted = System.currentTimeMillis();


			try {
				/* Execute the process command */
				response = processCommand.execute();

				if (response.getCode() == HttpStatusCode.CREATED) {
					process.status = Process.STATUS_FINISHED;
					String successMsg = "Execution of process with id " + processCommand.getPID()
							+ " in experiment "
							+ processCommand.getExpId() + " has finished.";
					Debug.log(successMsg);
					ErrorLogger.log("PROCESS", successMsg);
				} else {
					process.status = Process.STATUS_CRASHED;
					String crashedMsg = "FAILURE! Execution of process with id "
							+ processCommand.getPID() + " in experiment "
							+ processCommand.getExpId() + " has crashed.";
					Debug.log(crashedMsg);
					ErrorLogger.log("PROCESS", crashedMsg);
				}

			} catch (NullPointerException e) {
				process.status = Process.STATUS_CRASHED;
			}

			process.timeFinished = System.currentTimeMillis();

			String timeMsg = "Elapsed time: " + new Date(process
					.timeFinished-process.timeStarted).toString();
			Debug.log(timeMsg);
			ErrorLogger.log("PROCESS", timeMsg);

		}

		return response;

	}


}

