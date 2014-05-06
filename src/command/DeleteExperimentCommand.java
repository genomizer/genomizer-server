package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import database.DatabaseAccessor;
import database.Experiment;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a remove experiment command.
 *
 * @author tfy09jnn, Hugo Källström
 * @version 1.0
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

	    String username = "c5dv151_vt14";
	    String password = "shielohh";
	    String host = "postgres";
	    String database = "c5dv151_vt14";
	    DatabaseAccessor db = null;
		try {
			db = new DatabaseAccessor(username, password, host, database);
			db.deleteExperiment(this.header);
		} catch (SQLException e) {
			return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
		} 

		return new MinimalResponse(200);
	}

}
