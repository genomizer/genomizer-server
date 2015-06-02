package command;

import com.google.gson.annotations.Expose;
import command.process.*;

import java.util.UUID;

/**
 * Class used to handle the process status.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class Process implements Comparable<Process> {
	public static final String STATUS_WAITING = "Waiting";
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
	@Expose
	public String PID;

	/**
	 * Constructs a new instance of Process using default values.
	 */
	public Process() {
		experimentName = "UNKNOWN EXPERIMENT";
		status = STATUS_WAITING;
		outputFiles = new String[] {};
		author = "UNKNOWN AUTHOR";
		timeAdded = System.currentTimeMillis();
		PID = UUID.randomUUID().toString();
	}

	public Process(String expId, String author) {
		this();
		experimentName = expId;
		this.author = author;
	}

	/**
	 * Constructs a new instance of DeleteExperimentCommand using the supplied
	 * PutProcessCommand.
	 * @param command a PutProcessCommand.
	 */
	@SuppressWarnings("deprecation")
	public Process(PutProcessCommand command) {
		status = STATUS_WAITING;
		author = command.getUsername();
		experimentName = command.getExpId();
		timeAdded = System.currentTimeMillis();
		outputFiles = new String[0];
	}

	@Override
	public int compareTo(Process other) {
		if (timeAdded < other.timeAdded) {
			return -1;
		} else if (timeAdded > other.timeAdded) {
			return 1;
		}
		return 0;
	}

}
