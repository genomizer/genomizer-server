package server;

import command.ProcessCommand;
import command.ProcessStatus;
import response.Response;
import response.StatusCode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.*;


public class ProcessHandler implements Callable<Response> {

	private static final long statusTimeToLive = 2*1000*60*60*24;
	private ProcessPool processPool;
	private ProcessCommand processCommand;


	public ProcessHandler(ProcessPool processPool, ProcessCommand processCommand) {
		this.processPool = processPool;
		this.processCommand = processCommand;
	}


	public void removeOldStatuses() {

		// Get current time
		long currentTime = System.currentTimeMillis();

		// List to store processes to be removed
		LinkedList<ProcessCommand> toBeRemoved = new LinkedList<>();

		LinkedList<ProcessCommand> processesList = processPool.getProcesses();

		/* Loop through all processes and check statuses */
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


	}

	//The thread runs all the time and checks if the queue is empty
	//If the queue is not empty, the command at the head of the queue is
	//is executed
	@Override
	public Response call() {

		ProcessStatus processStatus = processPool.getProcessStatus
				(processCommand);


		Response response = null;

		if (processCommand != null && processStatus != null) {
			Debug.log("Executing process in experiment "
					+ processCommand.getExpId());

			processStatus.status = ProcessStatus.STATUS_STARTED;

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

			/* Execute the process command */
			try {
				response = processCommand.execute();


				Debug.log("AFTER EXECUTE PROCESS");
				if (response.getCode() == StatusCode.CREATED) {
					processStatus.status = ProcessStatus.STATUS_FINISHED;
				} else {
					processStatus.status = ProcessStatus.STATUS_CRASHED;
				}
			} catch (NullPointerException e) {
				processStatus.status = ProcessStatus.STATUS_CRASHED;
			}


			processStatus.timeFinished = System.currentTimeMillis();


		}

		removeOldStatuses();

		return response;

	}


}

