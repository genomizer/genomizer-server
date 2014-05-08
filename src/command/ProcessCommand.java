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

	//Empty constructor
	public ProcessCommand() {

	}

	@Override
	public boolean validate() {

		if((username == null && CanBeNull.USERNAME == false) || (processtype == null && CanBeNull.FILE_FILETYPE == false) ||
				(metadata == null && CanBeNull.FILE_METADATA == false) || (genomeRelease == null && CanBeNull.GENOME_VERSION == false) 	||
				(filename == null && CanBeNull.FILE_FILENAME == false))
		{
			System.err.println("ProcessCommand - Validate\n" +
					"Some annotation that can not be null is null.");
			return false;
		}

		if(expid == null){
			System.err.println("ProcessCommand - Validate\n" +
					"ExpID is null");
			return false;
		}

		if(fileid == null){
			System.err.println("ProcessCommand - Validate\n" +
					"FileID is null");
			return false;
		}

		if(parameters == null){
			System.err.println("ProcessCommand - Validate\n" +
					"Parameters are null");
			return false;
		}

		if(username.length() > MaxSize.USERNAME ||
				processtype.length() > MaxSize.FILE_FILETYPE ||
				metadata.length() > MaxSize.FILE_METADATA ||
				genomeRelease.length() > MaxSize.GENOME_VERSION ||
				filename.length() > MaxSize.FILE_FILENAME)
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
			processHandler = new ProcessHandler();

			switch(processtype){
			case "rawtoprofile":
				//The process type was a rawtoprofile

				//TODO Remove print for validating data.
				System.out.println("Uploader of file: " + username);
				System.out.println("filename:" + filename);
				System.out.println("metadata:" + metadata);
				System.out.println("username: " + username);
				System.out.println("expid: " + expid);
				System.out.println("fileid: " + fileid);
				System.out.println("genomeRelease: " + genomeRelease);


				//Profile hardcoded since in case "rawtoprofile"
				ArrayList<String> filepaths = dbac.process(fileid, "profile", filename, metadata, username, genomeRelease, expid);



				//Receive the path for the profile data from the database accessor.
				//	String outfilepath = dbac.addFile("profile", filename, metadata, "yuri", username, false, expid, genomeRelease);


				try {

					processHandler.executeProcess("rawToProfile", parameters, filepaths.get(0), filepaths.get(1));

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
