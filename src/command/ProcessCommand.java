package command;

/**
 * @author Robin Ödling - c11rog
 */

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map.Entry;

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
	 * Fields wich cannot be null:
	 * - username, processtype, metadata, genomeRelease, filename, expid,
	 *   fileId, parameters, author
	 * If these fields are null, false is returned.
	 *
	 * Length of fields are also checked to match the lengths specified by
	 * MaxSize class in the database package
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
	 * Execute to simulate flow.
	 */
	/*
	@Override
	public Response execute(){
//		System.err.println("Executing process command");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.err.println("Logging created uname: " + username);
		ResponseLogger.log(username, new ProcessResponse(StatusCode.CREATED, "raw to profile processing completed"));
		return new ProcessResponse(StatusCode.CREATED);
	}*/


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

				//filepaths = db.processRawToProfile(expid);


				Genome g = db.getGenomeRelease(genomeVersion);
			//	parameters[1] = g.path;
				parameters[1] = "/var/www/data/genome_releases/Rat/d_melanogaster_fb5_22";


				//Prints for checking what filepaths are given by database.
			//	System.err.println("Filepath.getKey(): " + filepaths.getKey());
			//	System.err.println("Filepath.getValue(): " + filepaths.getValue());

				try {

					//processHandler.executeProcess("rawToProfile", parameters, filepaths.getKey(), filepaths.getValue());
					processHandler.executeProcess("rawToProfile", parameters, "/home/pvt/infileDir", "/home/pvt/outfileDir/");
					System.out.println("------------------Running execute with parameters:--------------------");
					for(String s : parameters){
						System.out.println("Parameter: " + s);
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
					ResponseLogger.log(username, "InterruptedException: " + e.getMessage());
					return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, e.getMessage());
				} catch (IOException e1) {
					e1.printStackTrace();
					ResponseLogger.log(username, "IOException: " + e1.getMessage());
					return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, e1.getMessage());
				} finally{
					db.close();
				}

				break;
			default:
				System.err.println("Unknown process type in processcommand execute");
				db.close();
				ResponseLogger.log(username, "Unknown process type in processcommand execute");
				return new ProcessResponse(StatusCode.BAD_REQUEST, "Unknown process type in processcommand execute");

			}
		} catch (SQLException e) {
			e.printStackTrace();
			ResponseLogger.log(username, "SQL Exception in ProcessCommand execute:" + e.getMessage());
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, "SQL Exception in ProcessCommand execute:" + e.getMessage());
		} catch (IOException e1) {
			e1.printStackTrace();
			ResponseLogger.log(username, "IO Exception in ProcessCommand execute." + e1.getMessage());
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
		} finally {
			try {
				db.close();
			} catch (SQLException e) {
				ResponseLogger.log(username, "Could not close Database accessor2: " + e.getMessage());
				e.printStackTrace();
			}
		}


		//The execute executed correctly
		try {
			//TODO isPrivate hardcoded.
			db.addGeneratedProfiles(expid, filepaths.getValue(), filepaths.getKey(), metadata, genomeVersion, username, false);
		} catch (SQLException e) {
			e.printStackTrace();
			ResponseLogger.log(username, "SQL Exception in ProcessCommand execute when using addGeneratedProfiles: " + e.getMessage());
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
		} finally {
			try {
				db.close();
			} catch (SQLException e) {
				ResponseLogger.log(username, "Could not close Database accessor" + e.getMessage());
				e.printStackTrace();
			}
		}

		try {
			db.close();
		} catch (SQLException e) {
			ResponseLogger.log(username, "Could not close Database accessor3: " + e.getMessage());
			e.printStackTrace();
		}

		ResponseLogger.log(username, "Raw to profile processing completed");
		return new ProcessResponse(StatusCode.CREATED, "Raw to profile processing completed");


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
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String[] getFilePaths() {
		return new String[] {filepaths.getKey(), filepaths.getValue()};
	}

}
