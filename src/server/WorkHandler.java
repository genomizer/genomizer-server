package server;

import java.util.ArrayList;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import response.Response;
import response.StatusCode;

import command.ProcessCommand;
import command.ProcessStatus;


public class WorkHandler extends Thread{

	private static final long statusTimeToLive = 2*1000*60*60*24;

	private Queue<ProcessCommand> workQueue;
	private HashMap<ProcessCommand,ProcessStatus> processStatus;

	//A queue as a linked list
	public WorkHandler(){
		workQueue = new LinkedList<ProcessCommand>();
		processStatus=new HashMap<ProcessCommand, ProcessStatus>();
	}

	//Add a command to the queue
	public synchronized void addWork(ProcessCommand command) {
		workQueue.add(command);
		processStatus.put(command, new ProcessStatus(command));
	}

	public synchronized void removeOldStatuses() {
		long now = System.currentTimeMillis();
		ArrayList<ProcessCommand> toBeRemoved = new ArrayList<>();
		for (ProcessCommand proc : processStatus.keySet()) {
			ProcessStatus procStat = processStatus.get(proc);
			String statusString = procStat.status;
			if (statusString.equals("Finished") || statusString.equals("Crashed")) {
				long time = procStat.timeAdded;
				long diff = now - time;
				if (diff > statusTimeToLive) {
					toBeRemoved.add(proc);
				}
			}
		}
		for (ProcessCommand proc : toBeRemoved) {
			Debug.log("Removing old process status: " + proc.getExpId());
			processStatus.remove(proc);
		}


	}

	//The thread runs all the time and checks if the queue is empty
	//If the queue is not empty, the command at the head of the queue is
	//is executed
	@Override
	public void run(){
		Debug.log(Thread.currentThread().getName());


		while(true){
			if(!workQueue.isEmpty()){
				ProcessCommand work = workQueue.poll();
				Debug.log("The processcommand is going to be executed");
				ProcessStatus stat = processStatus.get(work);
				stat.status = "Started";

				try {
					work.setFilePaths();
				} catch (SQLException | IOException e) {
					Debug.log(e.getMessage());
					ResponseLogger.log(stat.author, "Could not run process command: " +  e.getMessage());
					stat.status = "Crashed";
					continue;

				}

				stat.outputFiles = work.getFilePaths();
				stat.timeStarted = System.currentTimeMillis();

				try{
					Response resp = work.execute();
					Debug.log("AFTER EXECUTE PROCESS");
					if (resp.getCode()==StatusCode.CREATED){
						stat.status = "Finished";
					}else{
						stat.status = "Crashed";
					}
				} catch(NullPointerException e){
					stat.status = "Crashed";
				}

				stat.timeFinished = System.currentTimeMillis();
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					Debug.log("Work Handler thread sleep failed/interrupted");
					ResponseLogger.log("SYSTEM", "Work Handler thread sleep failed/interrupted in between process execution.");
				}
			}
			removeOldStatuses();
		}

	}

	public Collection<ProcessStatus> getProcessStatus() {
		return processStatus.values();
	}
}

