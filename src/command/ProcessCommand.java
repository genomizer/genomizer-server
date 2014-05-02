package command;

import java.sql.SQLException;
import java.util.ArrayList;

import process.classes.ProcessHandler;

import response.Response;

import authentication.Authenticate;

import com.google.gson.annotations.Expose;

import database.SearchResult;
import databaseAccessor.DatabaseAccessor;

public class ProcessCommand extends Command {


	private String fileID;
	private String processType;
	private String metadata;
	private String userID;

	private String outFilePath;
	private String inFilePath;

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
			switch(processType){
				case "rawtoprofile":
					String uploader=Authenticate.getUsername(userID);
					ArrayList<String> filepaths=dbac.convertFromRawtoProfile(fileID,metadata,uploader,GRversion);

					ProcessHandler processHandler = new ProcessHandler();

					processHandler.executeProcess(processType, parameters, filepaths.get(0), filepaths.get(1));

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

		return null;
	}

}
