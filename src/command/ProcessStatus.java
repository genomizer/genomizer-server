package command;

import com.google.gson.annotations.Expose;

public class ProcessStatus implements Comparable<ProcessStatus> {

	@Expose
	private String experimentName;
	@Expose
	public String status;
	@Expose
	public String[] outputFiles;
	@Expose
	public String author;
	@Expose
	public long timeAdded;
	@Expose
	public long timeStarted;
	@Expose
	public long timeFinished;

	public ProcessStatus(ProcessCommand command) {
		status = "Waiting";
		author = command.getAuthor();
		experimentName = command.getExpId();
		timeAdded = System.currentTimeMillis();
		outputFiles = new String[0];
	}

	@Override
	public int compareTo(ProcessStatus other) {
		if (timeAdded < other.timeAdded) {
			return -1;
		} else if (timeAdded > other.timeAdded) {
			return 1;
		}
		return 0;
	}
}
