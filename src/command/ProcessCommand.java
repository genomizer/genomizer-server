package command;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import process.classes.ProcessHandler;

import response.MinimalResponse;
import response.ProcessResponse;
import response.Response;
import response.StatusCode;

import authentication.Authenticate;

import com.google.gson.annotations.Expose;

import database.*;

public class ProcessCommand extends Command {

	private String username;

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
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Response execute() {

		//TODO Parse metadata to get GRversion and parameters?
		String GRversion = "placeholderGRversion";
		metadata = "meta1,meta2,meta3";
		String[] parameters = {"param1","param2","param3"};

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
					// TODO Auto-generated catch block
					System.err.println("CATCH (IE) in ProcessCommand.Execute when running processHandler.executeProcess");
					e.printStackTrace();
					return new ProcessResponse(StatusCode.SERVICE_UNAVAILABLE);
				} catch (IOException e) {
					// TODO Auto-generated catch block
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

	public String getMetadata() {
		// TODO Auto-generated method stub
		return this.metadata;
	}

	public String[] getParameters() {
		// TODO Auto-generated method stub
		return this.parameters;
	}

	public String getGenomeRelease() {
		// TODO Auto-generated method stub
		return this.genomeRelease;
	}

	public String getProcessType() {
		return this.processtype;
	}

	public void setProcessType(String processType) {
		// TODO Auto-generated method stub
		this.processtype = processType;

	}

	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;

	}

	public String getFilename() {
		// TODO Auto-generated method stub
		return this.filename;
	}

	public String getFilepath() {
		// TODO Auto-generated method stub
		return this.filepath;
	}

	public String getExpID() {
		// TODO Auto-generated method stub
		return this.expid;
	}

}
