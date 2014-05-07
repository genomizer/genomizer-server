package command;


import java.io.IOException;
import java.sql.SQLException;
import process.classes.ProcessHandler;
import response.ProcessResponse;
import response.Response;
import response.StatusCode;

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
	private String filepath;
	@Expose
	private String expid;

	//Empty constructor
	public ProcessCommand() {

	}

	@Override
	public boolean validate() {

		if(username == null || processtype == null || metadata == null || parameters == null || genomeRelease == null || filename == null || filepath == null || expid == null){
			return false;
		}

		//TODO Lengths

		/*
		 * Path VARCHAR(128) UNIQUE NOT NULL,
    FileType VARCHAR(32) NOT NULL,
    FileName VARCHAR(32) NOT NULL,
    Date DATE NOT NULL,
    MetaData VARCHAR(256),
    Author VARCHAR(32),
    Uploader VARCHAR(32) NOT NULL,
    IsPrivate BOOLEAN NOT NULL,
    ExpID VARCHAR(64),
    GRVersion VARCHAR(16),
		 */
		//Not null
		//length of file info
		//

		// TODO Validate process command
		return true;
	}

	/**
	 * Method that runs when the processCommand is executed.
	 */
	@Override
	public Response execute() {
		System.out.println("-------------ProcessCommand - Execute----------------");


		String[] parameters = {"param1","param2","param3"};


		//TODO Should not be hardcoded here!
		String DBusername = "c5dv151_vt14";
		String DBpassword = "shielohh";
		String DBhost = "postgres";
		String DBdatabase = "c5dv151_vt14";
		DatabaseAccessor dbac;
		ProcessHandler processHandler;

		try {

			dbac = new  DatabaseAccessor(DBusername, DBpassword, DBhost, DBdatabase);
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
				System.out.println("genomeRelease: " + genomeRelease);

				//Receive the path for the profile data from the database accessor.
				String outfilepath = dbac.addFile("profile", filename, metadata, "yuri", username, false, expid, genomeRelease);

				try {

					processHandler.executeProcess("rawToProfile", parameters, filepath, outfilepath);

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
				break;

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
