package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import database.DatabaseAccessor;
import database.Experiment;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import server.DatabaseSettings;

/**
 * Class used to represent a remove experiment command.
 *
 * @author tfy09jnn, Hugo Källström
 * @version 1.1
 */
public class DeleteExperimentCommand extends Command {

	public DeleteExperimentCommand(String restful) {
		this.setHeader(restful);
	}
	/**
	 * Used to validate the command.
	 */
	public boolean validate() {

		if(this.getHeader() == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Used to execute the command.
	 */
	public Response execute() {

		DatabaseAccessor db = null;

		try {
			db = initDB();
			int tup = db.deleteExperiment(this.header);
		} catch (SQLException e) {
			return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
		} finally {
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
			}
		}
		return new MinimalResponse(StatusCode.OK);
	}

}
