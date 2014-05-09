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
	private String fileid;
	@Expose
	private String expid;
	@Expose
	private String author;

	//Empty constructor
	public ProcessCommand() {

	}

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

		if(fileid == null){
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
				fileid.length() <= 0 ||
				expid.length() > MaxSize.EXPID || expid.length() <= 0)

		{
			System.err.println("ProcessCommand - Validate\n" +
					"Wrong lengths of annotations. Could not continue");
			return false;
		}

		//TODO Lengths
		//TODO Validate process command
		return true;
	}

	/**
	 * Method that runs when the processCommand is executed.
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
				System.out.println("The process type was a rawtoprofile");
				//The process type was a rawtoprofile

				//TODO Remove print for validating data.
				System.out.println("Uploader of file: " + username);
				System.out.println("filename:" + filename);
				System.out.println("fileid:" + fileid);
				System.out.println("metadata:" + metadata);
				System.out.println("username: " + username);
				System.out.println("expid: " + expid);
				System.out.println("fileid: " + fileid);
				System.out.println("genomeRelease: " + genomeRelease);
				System.out.println("author:" + author);


				//Profile hardcoded since in case "rawtoprofile"
				ArrayList<String> filepaths = dbac.process(fileid, "profile", filename, metadata, username, genomeRelease, expid);



				//Receive the path for the profile data from the database accessor.
				//	String outfilepath = dbac.addFile("profile", filename, metadata, "yuri", username, false, expid, genomeRelease);


				try {
					System.out.println("Executing process");
					processHandler.executeProcess("rawToProfile", parameters, filepaths.get(0), filepaths.get(1));
					System.out.println("Executed process");
				} catch (InterruptedException e) {
					// TODO Fix this
					System.err.println("CATCH (IE) in ProcessCommand.Execute when running processHandler.executeProcess");
					e.printStackTrace();
					return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
				} catch (IOException e) {
					// TODO Fix this
					System.err.println("CATCH (IO) in ProcessCommand.Execute when running processHandler.executeProcess");
					e.printStackTrace();
					return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
				}

				break;
			default:
				System.err.println("Unknown process type in processcommand execute");
				return new ProcessResponse(StatusCode.NO_CONTENT);
				//				break;

			}
		} catch (SQLException e) {
			System.err.println("Error in ProcessCommand execute:");
			e.printStackTrace();
			return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
		}

		//The execute executed correctly. Return created response.
		return new ProcessResponse(StatusCode.CREATED);
	}

	/**
	 * Set the username of the uploader wich will be added to the database annotation.
	 * @param username - the username of the uploader.
	 */
	public void setUsername(String username) {
		this.username = username;

	}

	public String toString(){

		return "Uploader of file: " + username + "\n" +
				"Filename: " + filename + "\n" +
				"Processtype: " + processtype + "\n" +
				"metadata:" + metadata + "\n" +
				"username: " + username + "\n" +
				"expid: " + expid + "\n" +
				"fileid: " + fileid + "\n" +
				"genomeRelease: " + genomeRelease + "\n" +
				"author:" + author + "\n";
	}

	/*
	public String getMetadata() {
		return this.metadata;
	}

	public String[] getParameters() {
		return this.parameters;
	}

	public String getGenomeRelease() {
		return this.genomeRelease;
	}

	public String getProcessType() {
		return this.processtype;
	}

	public void setProcessType(String processType) {
		this.processtype = processType;

	}

	public String getUsername() {
		return this.username;
	}

	public String getFilename() {
		return this.filename;
	}

	public String getFilepath() {
		return this.filepath;
	}

	public String getExpID() {
		return this.expid;
	}
	 */
}
