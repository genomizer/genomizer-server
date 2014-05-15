package command;

import com.google.gson.annotations.Expose;

public class ProcessStatus {

	@Expose
	private String experimentName;
	@Expose
	public String status;
	@Expose
	public long timeAdded;
	@Expose
	public long timeStarted;
	@Expose
	public long timeFinished;

	public ProcessStatus(ProcessCommand command) {
		status = "Waiting";
		timeAdded = System.currentTimeMillis();
	}
}
