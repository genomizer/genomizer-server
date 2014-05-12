package command;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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
				processtype.length() > MaxSize.FILE_FILETYPE || processtype.length() <= 0 ||
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


		String[] parameters = {"param1","param2","param3"};

		DatabaseAccessor dbac;
		ProcessHandler processHandler;

		try {

			dbac = new  DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			System.out.println("created databaseaccesor");
			processHandler = new ProcessHandler();

			switch(processtype){
			case "rawtoprofile":
				//The process type was a rawtoprofile

				//Profile hardcoded since in case "rawtoprofile"
				ArrayList<String> filepaths = dbac.process(fileId, "profile", filename, metadata, username, genomeRelease, expid);


				//TODO Prints for checking what filepaths are given by database.
				System.err.println("Filepath[0]: " + filepaths.get(0));
				System.err.println("Filepath[1]: " + filepaths.get(1));

				//Receive the path for the profile data from the database accessor.
				//	String outfilepath = dbac.addFile("profile", filename, metadata, "yuri", username, false, expid, genomeRelease);


				try {
					System.out.println("Executing process");
					String log = processHandler.executeProcess("rawToProfile", parameters, filepaths.get(0), filepaths.get(1));
					System.err.println("AFter processHandler.executeProcess: " + log);
					System.out.println("Executed process");
				} catch (InterruptedException e) {
					// TODO Fix this
					System.err.println("CATCH InterruptedException in ProcessCommand.Execute when running processHandler.executeProcess");
					e.printStackTrace();
					return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
				} catch (IOException e) {
					// TODO Fix this
					System.err.println("CATCH IO exception in ProcessCommand.Execute when running processHandler.executeProcess");
					e.printStackTrace();
					return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
				}

				break;
			default:
				System.err.println("Unknown process type in processcommand execute");
				return new ProcessResponse(StatusCode.BAD_REQUEST);

			}
		} catch (SQLException e) {
			System.err.println("SQL Exception in ProcessCommand execute:");
			e.printStackTrace();
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
		}

		//The execute executed correctly. Return created response.
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
