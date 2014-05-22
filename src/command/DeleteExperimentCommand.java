package command;

import java.io.IOException;
import java.sql.SQLException;

import database.DatabaseAccessor;

import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a remove experiment command.
 *
 * @author tfy09jnn, Hugo Källström
 * @version 1.1
 */
public class DeleteExperimentCommand extends Command {

	/**
	 * Constructor used to initiate the object.
	 *
	 * @param restful string to set.
	 */
	public DeleteExperimentCommand(String restful) {

		this.setHeader(restful);

	}
	/**
	 * Used to validate the command.
	 *
	 * @return boolean depending on results.
	 */
	public boolean validate() {

		if(this.getHeader() == null) {

			return false;

		} else {

			return true;

		}

	}

	/**
	 * Used to execute the actual command that deletes
	 * the experiment.
	 *
	 * @return Response object depending on result.
	 */
	public Response execute() {

		DatabaseAccessor db = null;

		try {

			db = initDB();
			int tuples = db.deleteExperiment(header);

			if(tuples == 0) {

				return new ErrorResponse(StatusCode.BAD_REQUEST, "The experiment " + header + " does not exist and can not be deleted");

			}
		} catch (SQLException e) {

			return new ErrorResponse(StatusCode.BAD_REQUEST, Integer.toString(e.getErrorCode()));

		} catch (IOException e) {

			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());

		} finally {
			db.close();
		}

		return new MinimalResponse(StatusCode.OK);

	}

}
