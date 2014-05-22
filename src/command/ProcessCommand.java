package command;

/**
 * @author Robin Ödling - c11rog
 */

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map.Entry;

import process.classes.ProcessException;
import process.classes.ProcessHandler;
import response.ProcessResponse;
import response.Response;
import response.StatusCode;
import server.DatabaseSettings;
import server.ResponseLogger;

import com.google.gson.annotations.Expose;
import database.*;

public class ProcessCommand extends Command {

	private String username;

	private long timestamp;

	private String processtype;

	private Entry<String,String> filepaths;

	//Following fields corresponds to the JSON body of a process command.
	@Expose
	private String metadata;
	@Expose
	private String[] parameters;
	@Expose
	private String genomeVersion;
	@Expose
	private String expid;
	@Expose
	private String author;

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
	public boolean validate() {


		if(username == null){
			System.err.println("ProcessCommand - Validate\n" +
					"username is null");
			return false;
		}
		if(processtype == null){
			System.err.println("ProcessCommand - Validate\n" +
					"processtype is null");
			return false;
		}
		if(metadata == null){
			System.err.println("ProcessCommand - Validate\n" +
					"metadata is null");
			return false;
		}
		if(genomeVersion == null){
			System.err.println("ProcessCommand - Validate\n" +
					"genomerelease is null");
			return false;
		}

		if(expid == null){
			System.err.println("ProcessCommand - Validate\n" +
					"expid is null");
			return false;
		}

		if(parameters == null){
			System.err.println("ProcessCommand - Validate\n" +
					"parameters are null");
			return false;
		}
		if(author == null){
			System.err.println("ProcessCommand - Validate\n" +
					"author is null");
			return false;
		}

		switch (processtype) {
		case "rawtoprofile":
			if(parameters.length != 8){
				return false;
			}
		case "profiletoregion":
			//TODO Implement parameter size
		default:
			break;
		}

		if(username.length() > MaxSize.USERNAME || username.length() <= 0){
			System.err.println("Username has the wrong length of annotation");
			return false;
		}
		if(processtype.length() <= 0){
			System.err.println("Processtype has the wrong length of annotation");
			return false;
		}

		if(metadata.length() > MaxSize.FILE_METADATA || doesNotHaveCorrectLength(metadata, CanBeNull.FILE_METADATA)){
			System.err.println("Metadata [" + metadata + "] has the wrong length of annotation");
			return false;
		}
		if(genomeVersion.length() > MaxSize.GENOME_VERSION || doesNotHaveCorrectLength(genomeVersion, CanBeNull.GENOME_VERSION)){
			System.err.println("GenomeRelease has the wrong length of annotation");
			return false;
		}
		if(author.length() > MaxSize.FILE_AUTHOR || doesNotHaveCorrectLength(author, CanBeNull.FILE_AUTHOR)){
			System.err.println("Author has the wrong length of annotation");
			return false;
		}
		if(expid.length() > MaxSize.EXPID || doesNotHaveCorrectLength(expid, CanBeNull.EXPID)){
			System.err.println("Expid has the wrong length of annotation");
			return false;
		}


		return true;
	}

	private boolean doesNotHaveCorrectLength(String field, boolean canBeNull){
		System.out.println("field: "+ field + " length: " + field.length() + " canbenull: " + canBeNull);
		if(field.length() <= 0){
			if(canBeNull == false){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}


	/**
	 * Method that runs when the processCommand is executed.
	 *
	 */

	@Override
	public Response execute() {
		System.out.println("-------------ProcessCommand - Execute----------------");




		DatabaseAccessor db = null;
		ProcessHandler processHandler;

		try {

			db = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			processHandler = new ProcessHandler();

			switch(processtype){
			case "rawtoprofile":
				//The process type was a rawtoprofile

				filepaths = db.processRawToProfile(expid);

				if(!db.isConnected()){
					db = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
				}
				Genome g = db.getGenomeRelease(genomeVersion);

				String genomeFolderPath = g.folderPath;
				String genomeFilePrefix = g.getFilePrefix();

				if(genomeFolderPath == null){
					ResponseLogger.log(username, "Could not get genome folder path when " + processtype + " on experiment" + expid + "\n"+
							"metadata: " + metadata + "\n"+
							"parameters: " + parameters + "\n" +
							"genomeFilePrefix: " + genomeFilePrefix + "\n" +
							"genomeVersion: " + genomeVersion + "\n");
					db.close();
					return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, "Could not get genome folder path when " + processtype + " on experiment" + expid + "\n"+
							"metadata: " + metadata + "\n"+
							"parameters: " + parameters + "\n" +
							"genomeFilePrefix: " + genomeFilePrefix + "\n" +
							"genomeVersion: " + genomeVersion + "\n");
				}

				if(genomeFilePrefix == null){
					ResponseLogger.log(username, "Could not get genome file prefix when " + processtype + " on experiment" + expid + "\n"+
							"metadata: " + metadata + "\n"+
							"parameters: " + parameters + "\n" +
							"genomeFolderPath: " + genomeFolderPath + "\n" +
							"genomeVersion: " + genomeVersion + "\n");
					db.close();
					return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, "Could not get genome file prefix when " + processtype + " on experiment" + expid + "\n"+
							"metadata: " + metadata + "\n"+
							"parameters: " + parameters + "\n" +
							"genomeFolderPath: " + genomeFolderPath + "\n" +
							"genomeVersion: " + genomeVersion + "\n");
				}

				parameters[1] = genomeFolderPath + genomeFilePrefix;
				//parameters[1] = "/var/www/data/genome_releases/Rat/d_melanogaster_fb5_22";


				//Prints for checking what filepaths are given by database.
				//	System.err.println("Filepath.getKey(): " + filepaths.getKey());
				//	System.err.println("Filepath.getValue(): " + filepaths.getValue());

				try {

					processHandler.executeProcess("rawToProfile", parameters, filepaths.getKey(), filepaths.getValue());
					//processHandler.executeProcess("rawToProfile", parameters, "/home/pvt/infileDir", "/home/pvt/outfileDir/");
					System.out.println("------------------Running execute with parameters:--------------------");
					for(String s : parameters){
						System.out.println("Parameter: " + s);
					}
				} catch (ProcessException e) {
					e.printStackTrace();
					ResponseLogger.log(username, "Process Exception when running " + processtype + " on experiment" + expid + "\n"+
							"metadata: " + metadata + "\n"+
							"parameters: " + parameters + "\n" +
							"genomeVersion: " + genomeVersion + "\n" +
							"author: " + author + "\n" +
							e.getMessage());
					db.close();
					return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, e.getMessage());
				}
				break;
			default:
				System.err.println("Unknown process type in processcommand execute");
				db.close();
				ResponseLogger.log(username, "Unknown process type in processcommand execute when running " + processtype + " on experiment" + expid + "\n"+
						"metadata: " + metadata + "\n"+
						"parameters: " + parameters + "\n" +
						"genomeVersion: " + genomeVersion + "\n" +
						"author: " + author + "\n");
				return new ProcessResponse(StatusCode.BAD_REQUEST, "Unknown process type in processcommand execute when running " + processtype + " on experiment" + expid + "\n"+
						"metadata: " + metadata + "\n"+
						"parameters: " + parameters + "\n" +
						"genomeVersion: " + genomeVersion + "\n" +
						"author: " + author + "\n");

			}
		} catch (SQLException e) {
			e.printStackTrace();
			ResponseLogger.log(username, "SQL Exception in ProcessCommand execute when running " + processtype + " on experiment" + expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" +
					"author: " + author + "\n" +
					e.getMessage());
			db.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			ResponseLogger.log(username, "IO Exception in ProcessCommand execute when running " + processtype + " on experiment" + expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" +
					"author: " + author + "\n" +
					e1.getMessage());
			db.close();
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, "IO Exception in ProcessCommand execute when running " + processtype + " on experiment" + expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" +
					"author: " + author + "\n" +
					e1.getMessage());
		}


		//The execute executed correctly
		try {
			//TODO isPrivate hardcoded.
			//TODO Check if the connection is open
			if(!db.isConnected()){
				db = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			}
			db.addGeneratedProfiles(expid, filepaths.getValue(), filepaths.getKey(), metadata, genomeVersion, username, false);
		} catch (SQLException e) {
			e.printStackTrace();
			ResponseLogger.log(username, "SQL Exception in ProcessCommand execute when using addGeneratedProfiles with " + processtype + " on experiment" + expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" +
					"author: " + author + "\n" +
					e.getMessage());
			db.close();
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, "SQL Exception in ProcessCommand execute when using addGeneratedProfiles with " + processtype + " on experiment" + expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" +
					"author: " + author + "\n" +
					e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			ResponseLogger.log(username, "IO Exception in ProcessCommand execute when creating new DatabaseAccesor before addGeneratedProfiles with " + processtype + " on experiment" + expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" +
					"author: " + author + "\n" +
					e.getMessage());
			db.close();
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, "IO Exception in ProcessCommand execute when creating new DatabaseAccesor before addGeneratedProfiles running " + processtype + " on experiment" + expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" +
					"author: " + author + "\n" +
					e.getMessage());
		}

		db.close();

		ResponseLogger.log(username, "Raw to profile processing completed running " + processtype + " on experiment" + expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" +
					"author: " + author + "\n");
		return new ProcessResponse(StatusCode.CREATED, "Raw to profile processing completed running " + processtype + " on experiment" + expid + "\n"+
					"metadata: " + metadata + "\n"+
					"parameters: " + parameters + "\n" +
					"genomeVersion: " + genomeVersion + "\n" +
					"author: " + author + "\n");


	}

	/**
	 * Set the username of the uploader wich will be added to the database annotation.
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
				//				"Filename: " + filename + "\n" +
				"Processtype: " + processtype + "\n" +
				"metadata:" + metadata + "\n" +
				"username: " + username + "\n" +
				"expid: " + expid + "\n" +
				//				"fileid: " + fileId + "\n" +
				"genomeRelease: " + genomeVersion + "\n" +
				"author:" + author + "\n";
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

	public String getAuthor() {
		return author;
	}

	public String getExpId() {
		return expid;
	}

	public void setFilePaths() {
		DatabaseAccessor db = null;
		try {
			db = initDB();
			filepaths = db.processRawToProfile(expid);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	public String[] getFilePaths() {
		return new String[] {filepaths.getKey(), filepaths.getValue()};
	}

}
