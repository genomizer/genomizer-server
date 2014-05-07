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

	//Following fields correspond to the JSON body of a process command.
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
		// TODO Validate process command
		return true;
	}

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

				System.out.println("Uploader of file: " + username);
				System.out.println("filename:" + filename);
				System.out.println("metadata:" + metadata);
				System.out.println("username: " + username);
				System.out.println("expid: " + expid);
				System.out.println("genomeRelease: " + genomeRelease);

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

		return new ProcessResponse(StatusCode.CREATED);
	}

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
