package server;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import response.Response;
import response.StatusCode;
import sun.misc.Cleaner;

import command.Command;
import command.GetAnnotationInformationCommand;
import command.ProcessCommand;
import command.ProcessStatus;


public class WorkHandler extends Thread{

	private static final long statusTimeToLive = 3000;//1000*60*60*24*3;

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
		System.out.println("Entering Remove");
		long now = System.currentTimeMillis();

		for (ProcessCommand proc : processStatus.keySet()) {
			ProcessStatus procStat = processStatus.get(proc);
			String statusString = procStat.status;
			if (statusString.equals("Finished") || statusString.equals("Crashed")) {
				long time = procStat.timeFinished;
				long diff = now - time;
				if (diff > statusTimeToLive) {
					System.out.println("Removing " + proc.getExpId());
					processStatus.remove(proc);
				}
			}
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
				work.setFilePaths();
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
				ResponseLogger.printLog();
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			removeOldStatuses();
		}

	}

	public Collection<ProcessStatus> getProcessStatus() {
		return processStatus.values();
	}
}

