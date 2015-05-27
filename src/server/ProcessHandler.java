package server;


import command.Process;
import response.HttpStatusCode;
import response.Response;
import util.Util;

import java.util.concurrent.Callable;


public class ProcessHandler implements Callable<Response> {

	private Callable<Response> callable;
	private Process process;

	public ProcessHandler(Callable<Response> callable,
						  Process process) {
		this.callable = callable;
		this.process = process;
	}

	@Override
	public Response call() {

		Response response = null;

		if (callable != null && process != null) {
			Debug.log("Execution of process with id " + process.PID
					+ " in experiment "
					+ process.experimentName + " has begun.");

			process.status = Process.STATUS_STARTED;
			process.timeStarted = System.currentTimeMillis();


			try {
				/* Execute the process command */
				response = callable.call();

				if (response.getCode() == HttpStatusCode.CREATED ||
					response.getCode() == HttpStatusCode.OK) {
					process.status = Process.STATUS_FINISHED;
					String successMsg = "Execution of process with id " + process.PID
							+ " in experiment "
							+ process.experimentName + " has finished.";
					Debug.log(successMsg);
					ErrorLogger.log("PROCESS", successMsg);
				} else {
					System.out.println("Process status: " + response.getCode());
					process.status = Process.STATUS_CRASHED;
					String crashedMsg = "FAILURE! Execution of process with id "
							+ process.PID + " in experiment "
							+ process.experimentName + " has crashed.";
					Debug.log(crashedMsg);
					ErrorLogger.log("PROCESS", crashedMsg);
				}

			} catch (Exception e) {
				process.status = Process.STATUS_CRASHED;
			}

			process.timeFinished = System.currentTimeMillis();

			String timeMsg = "PID: " + process.PID + "\nElapsed time: " +
					Util.formatTimeDifference((process.timeFinished - process.timeStarted) / 1000) ;
			Debug.log(timeMsg);
			ErrorLogger.log("PROCESS", timeMsg);

		}

		Debug.log("PID: " + process.PID);
		Debug.log("Process response: " + response.getMessage());

		return response;

	}

}

