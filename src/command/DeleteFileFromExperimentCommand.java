package command;

import java.sql.SQLException;

import database.DatabaseAccessor;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import server.DatabaseSettings;

/**
 * Class used to represent a logout command.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class DeleteFileFromExperimentCommand extends Command {

	/**
	 * Used to validate the logout command.
	 */
	private String fileID;

	public DeleteFileFromExperimentCommand(String restful){
		fileID=restful;

	}

	@Override
	public boolean validate() {
		if(fileID==null){
			return false;
		}

		return true;
	}

	/**
	 * Used to execute the logout command.
	 */
	@Override
	public Response execute() {

		try {
			DatabaseAccessor accessor = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			if(accessor.deleteFile(fileID)==1){
				return new MinimalResponse(StatusCode.OK);
			}else{
				//which response code is appropriate?
				return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
		}
		//Method not implemented, send appropriate response
		//return 	new MinimalResponse(StatusCode.NO_CONTENT);

	}

}
