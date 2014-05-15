package server;

import java.util.LinkedList;
import java.util.Queue;

import command.Command;
import command.GetAnnotationInformationCommand;


public class WorkHandler extends Thread{

	private Queue<Command> workQueue;

	//A queue as a linked list
	public WorkHandler(){
		workQueue = new LinkedList<Command>();
	}

	//Add a command to the queue
	public void addWork(Command command) {
		workQueue.add(command);

	}

	//The thread runs all the time and checks if the queue is empty
	//If the queue is not empty, the command at the head of the queue is
	//is executed
	@Override
	public void run(){
		System.out.println(Thread.currentThread().getName());
		
		while(true){
			if(!workQueue.isEmpty()){
				Command work = workQueue.poll();
				System.out.println("The processcommand is going to be executed");
				//work.execute();
			}
		}
	}

	/*public static void main(String [] args){
		WorkHandler workHandler = new WorkHandler();
		System.out.println(Thread.currentThread().getName());
		workHandler.start();


			workHandler.addWork(new GetAnnotationInformationCommand());
			//System.out.println("added");
			workHandler.addWork(new GetAnnotationInformationCommand());
	
			try {
				workHandler.sleep(20);
				workHandler.addWork(new GetAnnotationInformationCommand());
				workHandler.addWork(new GetAnnotationInformationCommand());
			
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			workHandler.addWork(new GetAnnotationInformationCommand());
			workHandler.addWork(new GetAnnotationInformationCommand());
		
			


	}*/


}

