package server;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import response.Response;
import response.StatusCode;

import command.Command;
import command.GetAnnotationInformationCommand;
import command.ProcessCommand;
import command.ProcessStatus;


public class WorkHandler extends Thread{

	private Queue<ProcessCommand> workQueue;
	private HashMap<ProcessCommand,ProcessStatus> processStatus;

	//A queue as a linked list
	public WorkHandler(){
		workQueue = new LinkedList<ProcessCommand>();
		processStatus=new HashMap<ProcessCommand, ProcessStatus>();
	}

	//Add a command to the queue
	public void addWork(ProcessCommand command) {
		workQueue.add(command);
		processStatus.put(command, new ProcessStatus(command));
	}

	//The thread runs all the time and checks if the queue is empty
	//If the queue is not empty, the command at the head of the queue is
	//is executed
	@Override
	public void run(){
		System.out.println(Thread.currentThread().getName());


		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(!workQueue.isEmpty()){
				ProcessCommand work = workQueue.poll();
				System.out.println("The processcommand is going to be executed");
				ProcessStatus stat = processStatus.get(work);
				try{
					stat.status = "Started";
					work.setFilePaths();
					stat.outputFiles = work.getFilePaths();
					stat.timeStarted = System.currentTimeMillis();

					Response resp = work.execute();
					System.err.println("AFTER EXECUTE PROCESS");
					if (resp.getCode()==StatusCode.CREATED){
						stat.status = "Finished";
					}else{
						stat.status = "Crashed";
					}
					stat.timeFinished = System.currentTimeMillis();
				}catch(NullPointerException e){
					e.printStackTrace();
				}
				ResponseLogger.printLog();
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public Collection<ProcessStatus> getProcessStatus() {
		return processStatus.values();
	}
}

