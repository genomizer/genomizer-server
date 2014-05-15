package server;

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


public class WorkHandler extends Thread{

	private Queue<ProcessCommand> workQueue;
	private TreeMap<ProcessCommand,String> processStatus;

	//A queue as a linked list
	public WorkHandler(){
		workQueue = new LinkedList<ProcessCommand>();
		processStatus=new TreeMap<ProcessCommand, String>();
	}

	//Add a command to the queue
	public void addWork(ProcessCommand command) {
		workQueue.add(command);
		processStatus.put(command, "Waiting");
	}

	//The thread runs all the time and checks if the queue is empty
	//If the queue is not empty, the command at the head of the queue is
	//is executed
	@Override
	public void run(){
		System.out.println(Thread.currentThread().getName());

		while(true){
			if(!workQueue.isEmpty()){
				ProcessCommand work = workQueue.poll();
				System.out.println("The processcommand is going to be executed");
				processStatus.put(work,"Started");
				Response resp = work.execute();
				if (resp.getCode()==StatusCode.CREATED){
					processStatus.put(work,"Finished");
				}else{
					processStatus.put(work,"Crashed");
				}
					//else if (503)
					//map.put(work,"crashed")

			}
		}
	}

	public TreeMap<ProcessCommand, String> getProcessStatus() {
		return processStatus;
	}
}

