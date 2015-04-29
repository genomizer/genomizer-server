package transfer;

public class FileTransferThread implements Runnable {
	private TransferCommand transferCommand;

	public FileTransferThread(TransferCommand transferCommand) {
		this.transferCommand = transferCommand;
	}

	@Override
	public void run() {
		transferCommand.execute();
	}
}
