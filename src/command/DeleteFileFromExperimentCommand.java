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
	public DeleteFileFromExperimentCommand(String restful){
		setHeader(restful);
	}

	@Override
	public boolean validate() {
		return true;
	}

	/**
	 * Used to execute the logout command.
	 */
	@Override
	public Response execute() {

		DatabaseAccessor db = null;

		try {
			db = initDB();
			if(db.deleteFile(Integer.parseInt(header))==1){
				return new MinimalResponse(StatusCode.OK);
			}else{
				return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
		} finally {
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
			}
		}
	}

}
