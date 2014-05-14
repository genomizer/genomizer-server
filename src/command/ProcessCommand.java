package command;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map.Entry;

import process.classes.ProcessHandler;
import response.ProcessResponse;
import response.Response;
import response.StatusCode;
import server.DatabaseSettings;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import database.*;

public class ProcessCommand extends Command {

	private String username;

	//Following fields corresponds to the JSON body of a process command.
	@Expose
	private String processtype;
	@Expose
	private String metadata;
	@Expose
	private String[] parameters;
	@Expose
	private String genomeRelease;
	@Expose
	private String filename;
	@Expose
	private String fileId;
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
		if(genomeRelease == null){
			System.err.println("ProcessCommand - Validate\n" +
					"genomerelease is null");
			return false;
		}
		if(filename == null){
			System.err.println("ProcessCommand - Validate\n" +
					"filename is null");
			return false;
		}

		if(expid == null){
			System.err.println("ProcessCommand - Validate\n" +
					"expid is null");
			return false;
		}

		if(fileId == null){
			System.err.println("ProcessCommand - Validate\n" +
					"fileid is null");
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



		//TODO Hardcoded. Not using CanBeNull class
		if(username.length() > MaxSize.USERNAME || username.length() <= 0 ||
				processtype.length() <= 0 ||
				metadata.length() > MaxSize.FILE_METADATA ||
				genomeRelease.length() > MaxSize.GENOME_VERSION ||
				filename.length() > MaxSize.FILE_FILENAME || filename.length() <= 0 ||
				author.length() > MaxSize.FILE_AUTHOR ||
				fileId.length() <= 0 ||
				expid.length() > MaxSize.EXPID || expid.length() <= 0)

		{
			System.err.println("ProcessCommand - Validate\n" +
					"Wrong lengths of annotations. Could not continue");
			return false;
		}

		return true;
	}

	/**
	 * Method that runs when the processCommand is executed.
	 *
	 */
	@Override
	public Response execute() {
		System.out.println("-------------ProcessCommand - Execute----------------");




		DatabaseAccessor db;
		ProcessHandler processHandler;
		Entry<String, String> filepaths;

		try {

			db = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			System.out.println("created databaseaccesor");
			processHandler = new ProcessHandler();

			switch(processtype){
			case "rawtoprofile":
				//The process type was a rawtoprofile

				filepaths = db.processRawToProfile(expid);

				//TODO Ask database for path to genome release

				//Prints for checking what filepaths are given by database.
				System.err.println("Filepath.getKey(): " + filepaths.getKey());
				System.err.println("Filepath.getValue(): " + filepaths.getValue());

				try {
					String log = processHandler.executeProcess("rawToProfile", parameters, filepaths.getKey(), filepaths.getValue());
				} catch (InterruptedException e) {
					// TODO Close db accessor. Log response
					System.err.println("CATCH InterruptedException in ProcessCommand.Execute when running processHandler.executeProcess");
					e.printStackTrace();
					return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
				} catch (IOException e) {
					// TODO Close db accessor. Log response
					System.err.println("CATCH IO exception in ProcessCommand.Execute when running processHandler.executeProcess");
					e.printStackTrace();
					return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
				}

				break;
			default:
				// TODO Close db accessor. Log response
				System.err.println("Unknown process type in processcommand execute");
				return new ProcessResponse(StatusCode.BAD_REQUEST);

			}
		} catch (SQLException e) {
			// TODO Close db accessor. Log response
			System.err.println("SQL Exception in ProcessCommand execute:");
			e.printStackTrace();
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
		} catch (IOException e1) {
			// TODO Close db accessor. Log response
			System.err.println("IO Exception in ProcessCommand execute.");
			e1.printStackTrace();
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
		}


		//The execute executed correctly
		try {
			//TODO isPrivate hardcoded.
			db.addGeneratedProfiles(expid, filepaths.getValue(), filepaths.getKey(), metadata, genomeRelease, username, false);
		} catch (SQLException e) {
			// TODO Close db accessor. Log response
			System.err.println("SQL Exception in ProcessCommand execute when using addGeneratedProfiles:");
			e.printStackTrace();
		}

		// TODO Close db accessor.
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
				"Filename: " + filename + "\n" +
				"Processtype: " + processtype + "\n" +
				"metadata:" + metadata + "\n" +
				"username: " + username + "\n" +
				"expid: " + expid + "\n" +
				"fileid: " + fileId + "\n" +
				"genomeRelease: " + genomeRelease + "\n" +
				"author:" + author + "\n";
	}
}
