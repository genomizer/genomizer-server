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
		System.err.println("Executing process command");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.err.println("Logging created uname: " + username);
		ResponseLogger.log(username, new ProcessResponse(StatusCode.CREATED, "raw to profile processing completed"));
		ResponseLogger.printUserLog(username);
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
				parameters[2] = g.path;

				//Prints for checking what filepaths are given by database.
				System.err.println("Filepath.getKey(): " + filepaths.getKey());
				System.err.println("Filepath.getValue(): " + filepaths.getValue());

				try {
					processHandler.executeProcess("rawToProfile", parameters, filepaths.getKey(), filepaths.getValue());
				} catch (InterruptedException e) {
					// TODO Log response
					System.err.println("CATCH InterruptedException in ProcessCommand.Execute when running processHandler.executeProcess");
					e.printStackTrace();
					System.err.println("Logging uname: " + username);
					ResponseLogger.log(username, new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, e.getMessage()));
					return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
				} catch (IOException e1) {
					// TODO Log response
					System.err.println("CATCH IO exception in ProcessCommand.Execute when running processHandler.executeProcess");
					e1.printStackTrace();
					System.err.println("Logging uname: " + username);
					ResponseLogger.log(username, new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, e1.getMessage()));
					return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
				} finally{
					db.close();
				}

				break;
			default:
				// TODO Log response
				System.err.println("Unknown process type in processcommand execute");
				db.close();
				System.err.println("Logging uname: " + username);
				ResponseLogger.log(username, new ProcessResponse(StatusCode.BAD_REQUEST, "Unknown process type in processcommand execute"));
				return new ProcessResponse(StatusCode.BAD_REQUEST);

			}
		} catch (SQLException e) {
			// TODO Log response
			System.err.println("SQL Exception in ProcessCommand execute:");
			e.printStackTrace();
			System.err.println("Logging uname: " + username);
			System.err.println("Logging uname: " + username);
			ResponseLogger.log(username, new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, e.getMessage()));
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
		} catch (IOException e1) {
			// TODO Log response
			System.err.println("IO Exception in ProcessCommand execute.");
			e1.printStackTrace();
			System.err.println("Logging uname: " + username);
			ResponseLogger.log(username, new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, e1.getMessage()));
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
		} finally {
			try {
				db.close();
			} catch (SQLException e) {
				System.err.println("Could not close Database accessor1");
				System.err.println("Logging uname: " + username);
				ResponseLogger.log(username, new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, "Could not close Database accessor"));
				e.printStackTrace();
			}
		}


		//The execute executed correctly
		try {
			//TODO isPrivate hardcoded.
			db.addGeneratedProfiles(expid, filepaths.getValue(), filepaths.getKey(), metadata, genomeVersion, username, false);
		} catch (SQLException e) {
			// TODO Log response
			System.err.println("SQL Exception in ProcessCommand execute when using addGeneratedProfiles:");
			e.printStackTrace();
			System.err.println("Logging uname: " + username);
			ResponseLogger.log(username, new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, e.getMessage()));
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);

		} finally {
			try {
				db.close();
			} catch (SQLException e) {
				System.err.println("Could not close Database accessor2");
				System.err.println("Logging uname: " + username);
				ResponseLogger.log(username, new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, "Could not close Database accessor"));
				e.printStackTrace();
			}
		}

		try {
			db.close();
		} catch (SQLException e) {
			System.err.println("Could not close Database accessor3");
			System.err.println("Logging uname: " + username);
			ResponseLogger.log(username, new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE, "Could not close Database accessor"));
			e.printStackTrace();
		}
		System.err.println("Logging created uname: " + username);
		ResponseLogger.log(username, new ProcessResponse(StatusCode.CREATED, "raw to profile processing completed"));
		return new ProcessResponse(StatusCode.CREATED);


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
