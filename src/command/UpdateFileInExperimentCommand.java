package command;

import java.io.IOException;
import java.sql.SQLException;

import database.DatabaseAccessor;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a logout command.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class UpdateFileInExperimentCommand extends Command {

	/**
	 * Used to validate the logout command.
	 */
	@Override
	public boolean validate() {

		// TODO Auto-generated method stub
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//Method not implemented, send appropriate response
		return 	new MinimalResponse(StatusCode.NO_CONTENT);

	}

}
