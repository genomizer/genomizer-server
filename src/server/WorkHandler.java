package server;

import command.ProcessCommand;
import command.ProcessStatus;
import response.Response;
import response.StatusCode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;


public class WorkHandler implements Runnable {

	private static final long statusTimeToLive = 2*1000*60*60*24;
	private WorkPool workPool;

	public WorkHandler(WorkPool workPool) {
		this.workPool = workPool;
	}

	public void removeOldStatuses() {

		// Get current time
		long currentTime = System.currentTimeMillis();

		// List to store processes to be removed
		LinkedList<ProcessCommand> toBeRemoved = new LinkedList<>();

		LinkedList<ProcessCommand> processesList = workPool.getProcesses();

		/* Loop through all processes and check statuses */
		for (ProcessCommand proc : processesList) {

			ProcessStatus procStat = workPool.getProcessStatus(proc);
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
			workPool.removeProcess(proc);
		}


	}

	//The thread runs all the time and checks if the queue is empty
	//If the queue is not empty, the command at the head of the queue is
	//is executed
	@Override
	public void run() {

		while (true) {

			ProcessCommand processCommand = workPool.getProcess();
			ProcessStatus processStatus = workPool.getProcessStatus
						(processCommand);


			if (processCommand != null && processStatus != null) {
				Debug.log("Executing process in experiment "
						+ processCommand.getExpId());

				processStatus.status = ProcessStatus.STATUS_STARTED;

				try {
					processCommand.setFilePaths();
				} catch (SQLException | IOException e) {
					Debug.log(e.getMessage());
					ErrorLogger.log(processStatus.author,
							"Could not run process command: " +  e.getMessage());
					processStatus.status = ProcessStatus.STATUS_CRASHED;

					continue;
				}

				processStatus.outputFiles = processCommand.getFilePaths();
				processStatus.timeStarted = System.currentTimeMillis();

				try {
					Response resp = processCommand.execute();
					Debug.log("AFTER EXECUTE PROCESS");
					if (resp.getCode()==StatusCode.CREATED){
						processStatus.status = ProcessStatus.STATUS_FINISHED;
					} else {
						processStatus.status = ProcessStatus.STATUS_CRASHED;
					}
				} catch (NullPointerException e){
					processStatus.status = ProcessStatus.STATUS_CRASHED;
				}


				processStatus.timeFinished = System.currentTimeMillis();


			}

			removeOldStatuses();
		}

	}


}

