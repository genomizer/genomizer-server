package command.process;

/**
 * @author Robin Ödling - c11rog
 */

import authentication.Authenticate;
import com.google.gson.annotations.Expose;
import command.*;
import command.Process;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.Genome;
import database.subClasses.UserMethods.UserType;
import process.ProcessException;
import process.RawToProfileConverter;
import response.*;
import server.Debug;
import server.Doorman;
import server.ErrorLogger;
import server.ProcessPool;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

@Deprecated
public class PutProcessCommand extends Command {

	public static final String CMD_RAW_TO_PROFILE = "rawtoprofile";

	private String username;

	private long timestamp;

	private String processtype;

	private Entry<String,String> filepaths;

	//Following fields corresponds to the JSON body of a process command.
	@Expose
	String expid;

	@Expose
	String[] parameters;

	@Expose
	String metadata;

	@Expose
	String genomeVersion;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	@Override
	public void setFields(String uri, HashMap<String, String> query,
						  String uuid, UserType userType) {

		super.setFields(uri, query, uuid, userType);
		setTimestamp(System.currentTimeMillis());
		processtype = uri.split("/")[2];
		username = Authenticate.getUsernameByID(uuid);
	}

	/**
	 * Method for validating the process command.
	 *
	 * No field can be null.
	 *
	 */
	@Override
	public void validate() throws ValidateException {

		hasRights(UserRights.getRights(this.getClass()));
		validateName(username, MaxLength.USERNAME, "Username");
		validateName(expid, MaxLength.EXPID, "Experiment name");
		validateExists(metadata, MaxLength.FILE_METADATA, "Metadata");
		validateName(genomeVersion, MaxLength.GENOME_VERSION, "Genome version");
		validateExists(processtype, Integer.MAX_VALUE, "Process type");

		if(parameters == null || parameters.length < 1) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST,
					"Specify parameters.");
		}

		switch (processtype) {
			case CMD_RAW_TO_PROFILE:
				if(parameters.length != 8){
					throw new ValidateException(HttpStatusCode.BAD_REQUEST,
							"Specify the right number of parameters.(8)");
				}
				validateExists(parameters[0], Integer.MAX_VALUE, "First parameter");
				break;
			default:
				throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Invalid " +
						"process type");
		}
	}

	@Override
	public Response execute() {
		return addToProcessPool(Doorman.getProcessPool());
	}

	public Response addToProcessPool(ProcessPool pool) {
		Process process = new Process(this);

		// Attempt to setup file paths
		try {
			setFilePaths();
		} catch (SQLException | IOException e) {
			Debug.log(e.getMessage());
			ErrorLogger.log(process.author,
					"Could not run process command: " + e.getMessage());
			process.status = Process.STATUS_CRASHED;
			return new ProcessResponse(HttpStatusCode.INTERNAL_SERVER_ERROR);
		}
		process.outputFiles = getFilePaths();

		pool.addProcess(process, new ProcessingJob());

		return new ProcessResponse(HttpStatusCode.OK);
	}

	private class ProcessingJob implements Callable<Response> {

		public Response call() {
			try (DatabaseAccessor db = initDB()) {

				switch(processtype){
					case PutProcessCommand.CMD_RAW_TO_PROFILE:
						//Get the genome information from the database.
						Genome g = db.getGenomeRelease(genomeVersion);

						if(g == null){
							return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
									"Could not find genome version: " +
											genomeVersion);
						} else {
							//Get the path of the genome.
							String genomeFolderPath = g.folderPath;
							//Get the prefix of the genome files.
							String genomeFilePrefix = g.getFilePrefix();

							if(genomeFilePrefix == null){
								return processError("genomeFilePrefix: " + genomeFilePrefix,
										"Could not get genome file prefix when " +
												"processing");
							}

							if(genomeFolderPath == null){
								return processError("genomeFolderPath: " +
										genomeFolderPath, "Could not get genome " +
										"folder path when processing");
							}

							//Set parameter on index 1 to the path to the
							// genome folder + the name of the genome files.
							parameters[1] = genomeFolderPath + genomeFilePrefix;

						}

						try {
							RawToProfileConverter rawToProfileConverter =
									new RawToProfileConverter();
							String logString = rawToProfileConverter.procedure(parameters,
									filepaths.getKey(), filepaths.getValue());

							ErrorLogger.log("SYSTEM", "Process: " + logString);
						}
						catch (ProcessException e) {
							return processError(e.getMessage(), "Process " +
									"exception when processing");
						}
						break;

					default:
						return processError("", "ERROR: Unknown process " +
								"type when processing");
				}

				db.addGeneratedProfiles(expid, filepaths.getValue(),
						filepaths.getKey(), metadata, genomeVersion, username,
						false);
			} catch (SQLException e) {
				return processError(e.getMessage(), "SQL Exception  " +
						"when processing");
			} catch (IOException e1) {
				return processError(e1.getMessage(), "IO Exception  " +
						"when processing");
			}

			Debug.log(username + "Raw to profile processing completed " +
					"running " + processtype + " on experiment" + expid + "\n" +
					"metadata: " + metadata + "\n" +
					"parameters: "
					+ arrayToString(parameters, parameters.length) + "\n" +
					"genomeVersion: " + genomeVersion + "\n");
			return new ProcessResponse(HttpStatusCode.OK, "Raw to profile " +
					"processing completed running " + processtype +
					" on experiment" + expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: "
					+ arrayToString(parameters, parameters.length) + "\n" +
					"genomeVersion: " + genomeVersion + "\n");
		}
	}


	private String arrayToString(String[] arr, int length) {

		StringBuilder stringBuilder = new StringBuilder();

		for (int i=0; i<length; i++) {
			stringBuilder.append(arr[i]);
		}

		return stringBuilder.toString();
	}

	/**
	 * Logs an error and returns a ProcessResponse with
	 * the error message.
	 *
	 * @param error - the actual error
	 * @param headerError - the string which starts the error message
	 */
	private Response processError(String error, String headerError){
		ErrorLogger.log(username, headerError +
				" " + processtype +
				" on experiment" + expid + "\n" +
				"metadata: " + metadata + "\n" +
				"parameters: "
					+ arrayToString(parameters, parameters.length) + "\n" +
				"genomeVersion: " + genomeVersion + "\n" +
				error + "\n");
		return new ProcessResponse(HttpStatusCode.
				INTERNAL_SERVER_ERROR, headerError +
				" when processing " + processtype +
				" on experiment" + expid + "\n"+
				"metadata: " + metadata + "\n"+
				"parameters: "
					+ arrayToString(parameters, parameters.length) + "\n" +
				"genomeVersion: " + genomeVersion + "\n" +
				error + "\n");
	}

	/**
	 * Set the username of the uploader which will be added to the database
	 * annotation.
	 *
	 * @param username - the username of the uploader.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the process command fields as a formatted string.
	 */
	public String toString(){

		return "Uploader of file: " + username + "\n" +
				"ProcessType: " + processtype + "\n" +
				"metadata:" + metadata + "\n" +
				"username: " + username + "\n" +
				"expId: " + expid + "\n" +
				"genomeRelease: " + genomeVersion + "\n";
	}

	public void setTimestamp(long currentTimeMillis) {
		this.timestamp = currentTimeMillis;
	}
	public long getTimestamp(){
		return this.timestamp;
	}

	public void setProcesstype(String processtype) {
		this.processtype = processtype;

	}

	public String getExpId() {
		return expid;
	}

	public void setFilePaths() throws SQLException, IOException {
		try (DatabaseAccessor db = initDB()) {
			filepaths = db.processRawToProfile(expid);
		}
	}

	public String[] getFilePaths() {
		return new String[] {filepaths.getKey(), filepaths.getValue()};
	}

	public String getUsername() {
		return this.username;
	}


}
