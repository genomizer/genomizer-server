package command;


import java.sql.SQLException;
import java.util.ArrayList;

import process.classes.ProcessHandler;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

import authentication.Authenticate;

import com.google.gson.annotations.Expose;

import database.*;

public class ProcessCommand extends Command {

	private String userID;

	@Expose
	private String processtype;
	@Expose
	private String metadata;
	@Expose
	private String[] parameters;
	@Expose
	private String genomeRelease;

	//Empty constructor
	public ProcessCommand() {

	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Response execute() {

		//TODO Parse metadata to get GRversion and parameters?
		String GRversion = "placeholderGRversion";
		metadata = "meta1,meta2,meta3";
		String[] parameters = {"param1","param2","param3"};

		String username = "c5dv151_vt14";
		String password = "shielohh";
		String host = "postgres";
		String database = "c5dv151_vt14";
		DatabaseAccessor dbac;
		//Har ett filID,,processtype,param till profile->region parsa i commandFactory
		try {
			dbac = new  DatabaseAccessor(username, password, host, database);
			switch(processtype){
				case "rawtoprofile":
					String uploader=Authenticate.getUsername(userID);
					System.out.println("Uploader of file: " + uploader);




//					ArrayList<String> filepaths=dbac.convertFromRawtoProfile(fileID,metadata,uploader,GRversion);

					ProcessHandler processHandler = new ProcessHandler();

//					processHandler.executeProcess(processType, parameters, filepaths.get(0), filepaths.get(1));

					break;
				default:
					System.err.println("Unknown process type in processcommand execute");
					break;

			}
		} catch (SQLException e) {
			System.err.println("Error in ProcessCommand execute:");
			e.printStackTrace();
		}


		//FileExists till databas
		//Kontrollera vart outfilepath,infilepath
		//skicka filen till processmetoden (runexecutable),returvärde massa text ifrån script?
		//return respons 201

		//Method not implemented, send appropriate response
		return new MinimalResponse(StatusCode.NO_CONTENT);
	}

	public String getMetadata() {
		// TODO Auto-generated method stub
		return metadata;
	}

	public String[] getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	public String getGenomeRelease() {
		// TODO Auto-generated method stub
		return genomeRelease;
	}

	public String getProcessType() {
		return processtype;
	}

	public void setProcessType(String processType) {
		// TODO Auto-generated method stub
		this.processtype = processType;

	}

	public Object getUserID() {
		// TODO Auto-generated method stub
		return this.userID;
	}

	public void setUserID(String uuid) {
		this.userID = uuid;

	}

}
