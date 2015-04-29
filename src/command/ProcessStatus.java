package command;

import com.google.gson.annotations.Expose;

/**
 * Class used to handle the process status.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class ProcessStatus implements Comparable<ProcessStatus> {
	public static final String STATUS_STARTED = "Started";
	public static final String STATUS_FINISHED = "Finished";
	public static final String STATUS_CRASHED = "Crashed";


	@Expose
	public String experimentName;
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
