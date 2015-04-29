package command;

/**
 * @author Robin Ödling - c11rog
 */

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map.Entry;

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

	public static final String CMD_RAW_TO_PROFILE = "rawToProfile";
	public static final String CMD_PROFILE_TO_REGION = "profileToRegion";

	private String username;

	private long timestamp;

	private String processtype;

	private Entry<String,String> filepaths;

	//Following fields corresponds to the JSON body of a process command.
	@Expose
	private String expid;

	@Expose
	private String[] parameters;

	@Expose
	private String metadata;

	@Expose
	private String genomeVersion;


	//Empty constructor
	public ProcessCommand() {

	}

	/**
	 * Method for validating the process command.
	 *
	 * No field can be null.
	 *
	 */
	@Override
	public void validate() throws ValidateException {
		validateName(username, MaxLength.USERNAME, "Username");
		validateName(expid, MaxLength.EXPID, "Experiment name");
		validateExists(metadata, MaxLength.FILE_METADATA, "Metadata");
		validateName(genomeVersion, MaxLength.GENOME_VERSION, "Genome version");
		validateExists(processtype, Integer.MAX_VALUE, "Processtype");

		if(parameters == null || parameters.length < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST,
					"Specify parameters.");
		}

		switch (processtype) {
			case CMD_RAW_TO_PROFILE:
				if(parameters.length != 8){
					throw new ValidateException(StatusCode.BAD_REQUEST,
							"Specify the right number of parameters.(8)");
				}
				for(int i = 0; i < parameters.length; i++) {
					validateExists(parameters[i], Integer.MAX_VALUE, "Parameter " +
							parameters[i]);
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

	/**
	 * Method that checks if a json attribute has correct length or not.
	 *
	 * @param field - the json attribute to be checked
	 * @param canBeNull - a boolean stating if the attribute can be an empty
	 *                     string or not.
	 *
	 * @return True if the attribute have the correct length, else false.
	 */
	private boolean doesNotHaveCorrectLength(String field, boolean canBeNull){
		Debug.log("field: "+ field + " length: " + field.length() +
				" canbenull: " + canBeNull);
		if(field.length() <= 0){
			return !canBeNull;
		}
		return false;
	}


	/**
	 * Method that runs when the processCommand is executed.
	 *
	 */

	@Override
	public Response execute() {

		DatabaseAccessor db = null;
		ProcessHandler processHandler;

		try {

			db = new DatabaseAccessor(ServerSettings.databaseUsername,
					ServerSettings.databasePassword,
					ServerSettings.databaseHost, ServerSettings.databaseName);
			processHandler = new ProcessHandler();

			switch(processtype){
				case ProcessCommand.CMD_RAW_TO_PROFILE:
					//The process type was a rawtoprofile

					//filepaths = db.processRawToProfile(expid);

					if(!db.isConnected()){
						db = new DatabaseAccessor(ServerSettings.
								databaseUsername, ServerSettings.
								databasePassword, ServerSettings.
								databaseHost, ServerSettings.
								databaseName);
					}
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

						if(genomeFolderPath == null){
							ErrorLogger.log(username, "Could not get genome " +
									"folder path when " + processtype +
									" on experiment" + expid + "\n"+
									"metadata: " + metadata + "\n"+
									"parameters: " + parameters + "\n" +
									"genomeFilePrefix: " + genomeFilePrefix +
									"\n" + "genomeVersion: " + genomeVersion +
									"\n");
							db.close();
							return new ProcessResponse(StatusCode.
									SERVICE_UNAVAILABLE, "Could not get " +
									"genome folder path when " + processtype +
									" on experiment" + expid + "\n"+
									"metadata: " + metadata + "\n"+
									"parameters: " + parameters + "\n" +
									"genomeFilePrefix: " + genomeFilePrefix + "\n" +
									"genomeVersion: " + genomeVersion + "\n");
						}

						if(genomeFilePrefix == null){
							ErrorLogger.log(username, "Could not get genome " +
									"file prefix when " + processtype +
									" on experiment" + expid + "\n"+
									"metadata: " + metadata + "\n"+
									"parameters: " + parameters + "\n" +
									"genomeFolderPath: " + genomeFolderPath +
									"\n" + "genomeVersion: " + genomeVersion +
									"\n");
							db.close();
							return new ProcessResponse(StatusCode.
									SERVICE_UNAVAILABLE, "Could not get " +
									"genome file prefix when " + processtype +
									" on experiment" + expid + "\n"+
									"metadata: " + metadata + "\n"+
									"parameters: " + parameters + "\n" +
									"genomeFolderPath: " + genomeFolderPath +
									"\n" + "genomeVersion: " + genomeVersion +
									"\n");
						}

						//Set parameter on index 1 to the path to the
						// genomefolder + the name of the genome files.
						parameters[1] = genomeFolderPath + genomeFilePrefix;
					}

					try {

						processHandler.executeProcess(
								ProcessCommand.CMD_RAW_TO_PROFILE,
								parameters, filepaths.getKey(),
								filepaths.getValue());
						Debug.log("------------------Running execute with " +
								"parameters:--------------------");
						for(String s : parameters){
							Debug.log("Parameter: " + s);
						}
					} catch (ProcessException e) {
						e.printStackTrace();
						ErrorLogger.log(username, "Process Exception " +
								"when running " + processtype + " on experiment"
								+ expid + "\n"+
								"metadata: " + metadata + "\n"+
								"parameters: " + parameters + "\n" +
								"genomeVersion: " + genomeVersion + "\n" +
								e.getMessage());
						db.close();
						return new ProcessResponse(StatusCode.
								SERVICE_UNAVAILABLE, e.getMessage());
					}
					break;
				default:
					Debug.log("ERROR: Unknown process type in " +
							"processcommand execute");
					db.close();
					ErrorLogger.log(username, "Unknown process type in " +
							"processcommand execute when running " + processtype
							+ " on experiment" + expid + "\n"+
							"metadata: " + metadata + "\n"+
							"parameters: " + parameters + "\n" +
							"genomeVersion: " + genomeVersion + "\n");
					return new ProcessResponse(StatusCode.BAD_REQUEST,
							"Unknown process type in processcommand execute " +
									"when running " + processtype +
									" on experiment" + expid + "\n"+
							"metadata: " + metadata + "\n"+
							"parameters: " + parameters + "\n" +
							"genomeVersion: " + genomeVersion + "\n");

			}
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorLogger.log(username, "SQL Exception in ProcessCommand " +
					"execute when running " + processtype + " on experiment" +
					expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" + e.getMessage());
			db.close();
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE,
					"SQL Exception in ProcessCommand execute when running " +
							processtype + " on experiment" + expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" + e.getMessage());
		} catch (IOException e1) {
			e1.printStackTrace();
			ErrorLogger.log(username, "IO Exception in ProcessCommand " +
					"execute when running " + processtype + " on experiment" +
					expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" + e1.getMessage());
			db.close();
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE,
					"IO Exception in ProcessCommand execute when running " +
							processtype + " on experiment" + expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" + e1.getMessage());
		}


		//The execute executed correctly
		try {
			//TODO isPrivate hardcoded.
			//TODO Check if the connection is open
			if(!db.isConnected()){
				db = new DatabaseAccessor(ServerSettings.databaseUsername,
						ServerSettings.databasePassword,
						ServerSettings.databaseHost,
						ServerSettings.databaseName);
			}
			db.addGeneratedProfiles(expid, filepaths.getValue(),
					filepaths.getKey(), metadata, genomeVersion, username,
					false);
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorLogger.log(username, "SQL Exception in ProcessCommand " +
					"execute when using addGeneratedProfiles with " +
					processtype + " on experiment" + expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" + e.getMessage());
			db.close();
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE,
					"SQL Exception in ProcessCommand execute when using " +
							"addGeneratedProfiles with " + processtype +
							" on experiment" + expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			ErrorLogger.log(username, "IO Exception in ProcessCommand " +
					"execute when creating new DatabaseAccesor before " +
					"addGeneratedProfiles with " + processtype +
					" on experiment" + expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" + e.getMessage());
			db.close();
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE,
					"IO Exception in ProcessCommand execute when creating " +
							"new DatabaseAccesor before addGeneratedProfiles " +
							"running " + processtype + " on experiment" + expid +
							"\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" + e.getMessage());
		}

		db.close();

		ErrorLogger.log(username, "Raw to profile processing completed " +
				"running " + processtype + " on experiment" + expid + "\n"+
				"metadata: " + metadata + "\n"+
				"parameters: " + parameters + "\n" +
				"genomeVersion: " + genomeVersion + "\n");
		return new ProcessResponse(StatusCode.CREATED, "Raw to profile " +
				"processing completed running " + processtype +
				" on experiment" + expid + "\n"+
				"metadata: " + metadata + "\n"+
				"parameters: " + parameters + "\n" +
				"genomeVersion: " + genomeVersion + "\n");


	}

	/**
	 * Set the username of the uploader wich will be added to the database
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
				"Processtype: " + processtype + "\n" +
				"metadata:" + metadata + "\n" +
				"username: " + username + "\n" +
				"expid: " + expid + "\n" +
				"genomeRelease: " + genomeVersion + "\n";
	}

	public void setTimestamp(long currentTimeMillis) {
		this.timestamp = currentTimeMillis;
	}
	public long getTimestamp(){
		return this.timestamp;
	}

	public void setProcessType(String processtype) {
		this.processtype = processtype;

	}

	public String getExpId() {
		return expid;
	}

	public void setFilePaths() throws SQLException, IOException {
		DatabaseAccessor db;

		db = initDB();
		filepaths = db.processRawToProfile(expid);

		if(db.isConnected()){
			db.close();
		}

	}

	public String[] getFilePaths() {
		return new String[] {filepaths.getKey(), filepaths.getValue()};
	}

	public String getUsername() {
		return this.username;
	}

}
