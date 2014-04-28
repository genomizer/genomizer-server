package transfer;

public class FileTransferThread implements Runnable {
	private Command command;

	public FileTransferThread(Command command) {
		this.command = command;
	}

	@Override
	public void run() {
		command.execute();
	}
}
