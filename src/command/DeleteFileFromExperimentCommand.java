package command;

import java.io.IOException;
import java.sql.SQLException;

import database.DatabaseAccessor;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a command that is used to
 * delete a file from an experiment.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class DeleteFileFromExperimentCommand extends Command {

	/**
	 * Constructor that initiates the class.
	 *
	 * @param restful header as a string to set.
	 */
	public DeleteFileFromExperimentCommand(String restful) {

		setHeader(restful);

	}

	/**
	 * Method that validates the class.
	 *
	 * @return boolean depending on result.
	 */
	@Override
	public boolean validate() {

		return true;

	}

	/**
	 * Used to execute the actual command to delete the
	 * file from an experiment.
	 *
	 * @return Response object depending on result.
	 */
	@Override
	public Response execute() {

		DatabaseAccessor db = null;

		try {

			db = initDB();

			if(db.deleteFile(Integer.parseInt(header))==1) {

				return new MinimalResponse(StatusCode.OK);

			} else {

				return new ErrorResponse(StatusCode.BAD_REQUEST, "The file " + header + " does not exist and can not be deleted");

			}

		} catch (SQLException e) {

			e.printStackTrace();
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, e.getMessage());

		} catch (IOException e) {

			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());

		} finally {
			db.close();
		}

	}

}
