package command;

import com.google.gson.annotations.Expose;

/**
 * Class used to handle the process status.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class ProcessStatus implements Comparable<ProcessStatus> {
	@Expose
	@SuppressWarnings("unused")
	private String experimentName;

	@Expose
	@SuppressWarnings("unused")
	private String status;

	@Expose
	@SuppressWarnings("unused")
	private String[] outputFiles;

	@Expose
	@SuppressWarnings("unused")
	private String author;

	@Expose
	@SuppressWarnings("unused")
	private long timeAdded;

	@Expose
	@SuppressWarnings("unused")
	private long timeStarted;

	@Expose
	@SuppressWarnings("unused")
	private long timeFinished;

	/**
	 * Constructs a new instance of DeleteExperimentCommand using the supplied
	 * ProcessCommand.
	 * @param command a ProcessCommand.
	 */
	public ProcessStatus(ProcessCommand command) {
		status = "Waiting";
		author = command.getUsername();
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
