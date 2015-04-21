package command;

import com.google.gson.annotations.Expose;

/**
 * Class used to handle the process status.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
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

	/**
	 * Constructor used to initialize the class.
	 *
	 * @param command ProcessCommand to create the ProcessStatus from.
	 */
	public ProcessStatus(ProcessCommand command) {

		status = "Waiting";
		author = command.getUsername();
		experimentName = command.getExpId();
		timeAdded = System.currentTimeMillis();
		outputFiles = new String[0];

	}

	/**
	 * Method used to compare the creation time for the ProcessStatus'.
	 * @param other ProcessStatus to compare with.
	 */
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
