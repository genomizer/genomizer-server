package server.test;

public class CommandMock {

	private String msg;

	public CommandMock(String msg){
		this.msg = msg;
	}

	public void execute() {
		System.out.println(msg);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
