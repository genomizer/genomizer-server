package command;

/**
 * @author Robin Ödling - c11rog
 */

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map.Entry;

import database.subClasses.UserMethods.UserType;
import authentication.Authenticate;
import process.ProcessException;
import process.ProcessHandler;
import response.ErrorResponse;
import response.ProcessResponse;
import response.Response;
import response.StatusCode;
import server.ServerSettings;
import server.Debug;
import server.ErrorLogger;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;
import database.containers.Genome;
import database.constants.MaxLength;

public class ProcessCommand extends Command {

	public static final String CMD_RAW_TO_PROFILE = "rawtoprofile";
	public static final String CMD_PROFILE_TO_REGION = "profiletoregion";

	private String username;

	private long timestamp;

	private String processType;

	private Entry<String,String> filePaths;

	//Following fields corresponds to the JSON body of a process command.
	@Expose
	private String expid;

	@Expose
	private String[] parameters;

	@Expose
	private String metadata;

	@Expose
	private String genomeVersion;


	@Override
	public void setFields(String uri, String username, UserType userType) {
		this.userType = userType;
		this.username = Authenticate.getUsernameByID(username);
		setTimestamp(System.currentTimeMillis());
		processType = uri.split("/")[1];
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
		validateExists(processType, Integer.MAX_VALUE, "Processtype");

		if(parameters == null || parameters.length < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST,
					"Specify parameters.");
		}

		switch (processType) {
			case CMD_RAW_TO_PROFILE:
				if(parameters.length != 8){
					throw new ValidateException(StatusCode.BAD_REQUEST,
							"Specify the right number of parameters.(8)");
				}
				for(int i = 0; i < parameters.length; i++) {
					if(i != 1) {
						validateExists(parameters[i], Integer.MAX_VALUE, "Parameter " +
								parameters[i]);
					}
				}
				break;
			case CMD_PROFILE_TO_REGION:
				//TODO Implement parameter size
				break;
			default:
				throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid " +
						"process type");
		}
	}

	@Override
	public Response execute() {

		DatabaseAccessor db = null;
		ProcessHandler processHandler;

		try {

			db = initDB();
			processHandler = new ProcessHandler();

			switch(processType){
				case ProcessCommand.CMD_RAW_TO_PROFILE:
					//Get the genome information from the database.
					Genome g = db.getGenomeRelease(genomeVersion);

					if(g == null) {
						return new ErrorResponse(StatusCode.BAD_REQUEST,
								"Could not find genome version: " +
										genomeVersion);
					} else {
						//Get the path of the genome.
						String genomeFolderPath = g.folderPath;
						//Get the prefix of the genome files.
						String genomeFilePrefix = g.getFilePrefix();

						if(genomeFilePrefix == null){
							return processError(db,
									"genomeFilePrefix: " + genomeFilePrefix,
									"Could not get genome file prefix when " +
											"processing");
						}

						if(genomeFolderPath == null){
							return processError(db, "genomeFolderPath: " +
									genomeFolderPath, "Could not get genome " +
									"folder path when processing");
						}

						//Set parameter on index 1 to the path to the
						// genomefolder + the name of the genome files.
						parameters[1] = genomeFolderPath + genomeFilePrefix;

					}

					try {
						processHandler.executeProcess(
								ProcessCommand.CMD_RAW_TO_PROFILE,
								parameters, filePaths.getKey(),
								filePaths.getValue());

					} catch (ProcessException e) {
						return processError(db, e.getMessage(), "Process " +
								"exception when processing");
					}
					break;
				default:
					return processError(db, "", "ERROR: Unknown process " +
							"type when processing");
			}
		} catch (SQLException e) {
			return processError(db, e.getMessage(), "SQL Exception  " +
					"when processing");
		} catch (IOException e1) {
			return processError(db, e1.getMessage(), "IO Exception  " +
					"when processing");
		}

		//The execute executed correctly
		try {
			if(!db.isConnected()){
				db = initDB();
			}

			db.addGeneratedProfiles(expid, filePaths.getValue(),
					filePaths.getKey(), metadata, genomeVersion, username,
					false);

		} catch (SQLException e) {
			return processError(db, e.getMessage(), "SQL Exception after " +
					"finished processing");
		} catch (IOException e) {
			return processError(db, e.getMessage(), "IO Exception after" +
					"finished processing");
		}
		
		db.close();
		Debug.log(username + "Raw to profile processing completed " +
				"running " + processType + " on experiment" + expid + "\n" +
				"metadata: " + metadata + "\n" +
				"parameters: " + parameters + "\n" +
				"genomeVersion: " + genomeVersion + "\n");
		return new ProcessResponse(StatusCode.CREATED, "Raw to profile " +
				"processing completed running " + processType +
				" on experiment" + expid + "\n"+
				"metadata: " + metadata + "\n"+
				"parameters: " + parameters + "\n" +
				"genomeVersion: " + genomeVersion + "\n");


	}

	/**
	 * Logs an error, closes the DB reference and returns a processresponse with
	 * the errormessage.
	 *
	 * @param db - the database reference.
	 * @param error - the actual error
	 * @param headerError - the string which starts the error message
	 */
	private Response processError(DatabaseAccessor db, String error, String headerError){
		ErrorLogger.log(username, headerError +
				" " + processType +
				" on experiment" + expid + "\n"+
				"metadata: " + metadata + "\n"+
				"parameters: " + parameters + "\n" +
				"genomeVersion: " + genomeVersion + "\n" +
				error + "\n");
		db.close();
		return new ProcessResponse(StatusCode.
				SERVICE_UNAVAILABLE, headerError +
				" when processing " + processType +
				" on experiment" + expid + "\n"+
				"metadata: " + metadata + "\n"+
				"parameters: " + parameters + "\n" +
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
				"ProcessType: " + processType + "\n" +
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

	public void setProcessType(String processType) {
		this.processType = processType;

	}

	public String getExpId() {
		return expid;
	}

	public void setFilePaths() throws SQLException, IOException {
		DatabaseAccessor db;

		db = initDB();
		filePaths = db.processRawToProfile(expid);

		if(db.isConnected()){
			db.close();
		}

	}

	public String[] getFilePaths() {
		return new String[] {filePaths.getKey(), filePaths.getValue()};
	}

	public String getUsername() {
		return this.username;
	}

}
