package server;

import command.ProcessCommand;
import command.ProcessStatus;
import response.Response;
import response.StatusCode;

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


	@Deprecated
	public void removeOldStatuses() {

	/*	// Get current time
		long currentTime = System.currentTimeMillis();

		// List to store processes to be removed
		LinkedList<ProcessCommand> toBeRemoved = new LinkedList<>();

		LinkedList<ProcessCommand> processesList = processPool.getProcesses();

		*//* Loop through all processes and check statuses *//*
		for (ProcessCommand proc : processesList) {

			ProcessStatus procStat = processPool.getProcessStatus(proc);
			String statusString = procStat.status;

			if (statusString.equals(ProcessStatus.STATUS_FINISHED)
					|| statusString.equals(ProcessStatus.STATUS_CRASHED)) {
				long processTimeAdded = procStat.timeAdded;
				long timeDifference = currentTime - processTimeAdded;

				if (timeDifference > statusTimeToLive) {
					toBeRemoved.add(proc);
				}
			}
		}
		for (ProcessCommand proc : toBeRemoved) {
			Debug.log("Removing old process status: " + proc.getExpId());
			processPool.cancelProcess(proc);
		}
*/

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

				if (response.getCode() == StatusCode.CREATED) {
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

