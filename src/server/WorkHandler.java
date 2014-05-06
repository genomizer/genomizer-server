package server;

import java.util.LinkedList;
import java.util.Queue;

import server.test.CommandMock;






public class WorkHandler extends Thread{

	private Queue<CommandMock> workQueue;

	public WorkHandler(){
		workQueue = new LinkedList<CommandMock>();
	}

	public void addWork(CommandMock commandMock) {
		workQueue.add(commandMock);

	}

	@Override
	public void run(){
		System.out.println(Thread.currentThread().getName());

		while(true){
			if(!workQueue.isEmpty()){

				CommandMock work = workQueue.poll();
				work.execute();
			}
		}

	}

	public static void main(String [] args){
		WorkHandler workHandler = new WorkHandler();
		System.out.println(Thread.currentThread().getName());
		workHandler.start();


			workHandler.addWork(new CommandMock("lol"));
			workHandler.addWork(new CommandMock("lol"));
			workHandler.addWork(new CommandMock("lol"));
			workHandler.addWork(new CommandMock("lol"));


	}


}

