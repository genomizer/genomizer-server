package server;

import command.Process;
import command.process.PutProcessCommand;
import response.Response;
import response.HttpStatusCode;

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
		LinkedList<PutProcessCommand> toBeRemoved = new LinkedList<>();

		LinkedList<PutProcessCommand> processesList = workPool.getProcesses();

		/* Loop through all processes and check statuses */
		for (PutProcessCommand proc : processesList) {

			Process procStat = workPool.getProcessStatus(proc);
			String statusString = procStat.status;

			if (statusString.equals(Process.STATUS_FINISHED)
					|| statusString.equals(Process.STATUS_CRASHED)) {
				long processTimeAdded = procStat.timeAdded;
				long timeDifference = currentTime - processTimeAdded;

				if (timeDifference > statusTimeToLive) {
					toBeRemoved.add(proc);
				}
			}
		}
		for (PutProcessCommand proc : toBeRemoved) {
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

			PutProcessCommand putProcessCommand = workPool.getProcess();
			Process process = workPool.getProcessStatus
						(putProcessCommand);


			if (putProcessCommand != null && process != null) {
				Debug.log("Executing process in experiment "
						+ putProcessCommand.getExpId());

				process.status = Process.STATUS_STARTED;

				try {
					putProcessCommand.setFilePaths();
				} catch (SQLException | IOException e) {
					Debug.log(e.getMessage());
					ErrorLogger.log(process.author,
							"Could not run process command: " +  e.getMessage());
					process.status = Process.STATUS_CRASHED;

					continue;
				}

				process.outputFiles = putProcessCommand.getFilepaths();
				process.timeStarted = System.currentTimeMillis();

				try {
					Response resp = putProcessCommand.execute();
					Debug.log("AFTER EXECUTE PROCESS");
					if (resp.getCode()== HttpStatusCode.OK){
						process.status = Process.STATUS_FINISHED;
					} else {
						process.status = command.Process.STATUS_CRASHED;
					}
				} catch (NullPointerException e){
					process.status = Process.STATUS_CRASHED;
				}


				process.timeFinished = System.currentTimeMillis();


			}

			removeOldStatuses();
		}

	}


}

