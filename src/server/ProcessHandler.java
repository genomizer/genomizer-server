package server;


import command.process.PutProcessCommand;
import command.Process;
import response.ProcessResponse;
import response.Response;
import response.HttpStatusCode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.*;


public class ProcessHandler implements Callable<Response> {

	private PutProcessCommand processCommand;
	private Process process;
	private boolean simulateLongProcess;


	public ProcessHandler(PutProcessCommand processCommand,
						  Process process) {
		this.processCommand = processCommand;
		this.process = process;
		simulateLongProcess = false;
	}


	@Override
	public Response call() {

		Response response = null;

		if (processCommand != null && process != null) {
			Debug.log("Execution of process with id " + processCommand.getPID()
					+ " in experiment "
					+ processCommand.getExpId() + " has begun.");

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

				if (response.getCode() == HttpStatusCode.CREATED ||
					response.getCode() == HttpStatusCode.OK) {
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

			// A simulation of a long executing process
			if (simulateLongProcess) {
				/* Long time process execution simulation */
				ErrorLogger.log("PROCESS", "Process is sleeping for 30 seconds.");
				Debug.log("Process is sleeping for 30 seconds. PID " +
						processCommand.getPID());
				try {
					Thread.sleep(30000);
				} catch (InterruptedException ex) {
					Debug.log("Sleep interrupted");
				}
				Debug.log("End of sleep. PID " + processCommand.getPID());
			}



			process.timeFinished = System.currentTimeMillis();

			String timeMsg = "PID: " + processCommand.getPID() + "\nElapsed time: " +
					formatTimeDifference((process.timeFinished - process.timeStarted) / 1000) ;
			Debug.log(timeMsg);
			ErrorLogger.log("PROCESS", timeMsg);

		}

		Debug.log("PID: " + processCommand.getPID());
		Debug.log("Process response: " +
				((ProcessResponse) response).getMessage());

		return response;

	}

	public void setSimulation(boolean flag) {
		simulateLongProcess = flag;
	}

	private String formatTimeDifference(long diffMillis) {
		long seconds = diffMillis / 1000;
		long minutes = seconds / 60;
		long hours   = minutes / 60;
		long days    = hours   / 24;


		if (days > 0) {
			return new String(days + " days, " + hours + " hours, "
					+ minutes + " minutes, " + seconds + " seconds");
		}

		if (hours > 0) {
			return new String(hours + " hours, "
					+ minutes + " minutes, " + seconds + " seconds");
		}

		if (minutes > 0) {
			return new String(minutes + " minutes, " + seconds + " seconds");
		}

		return new String(seconds + " seconds");
	}


}

