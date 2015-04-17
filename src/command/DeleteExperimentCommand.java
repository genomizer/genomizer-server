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
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
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
	 * Used to validate the DeleteExperimentCommand.
	 *
	 * @return boolean depending on results.
	 * @throws ValidateException
	 */
	public boolean validate() throws ValidateException {

		if(header == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Experiment " +
					"name was missing.");
		} else if(header.length() < 1 || header.length() >
				database.constants.MaxSize.EXPID) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Experiment " +
					"name has to be between 1 and "
					+ database.constants.MaxSize.FILE_EXPID +
					" characters long.");
		} else if(!hasOnlyValidCharacters(header)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid " +
					"characters in experiment name. Valid characters are: " +
					validCharacters);
		}

		return true;

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

				return new ErrorResponse(StatusCode.BAD_REQUEST,
						"The experiment " + header + " does not exist and " +
								"can not be deleted");

			}
		} catch (SQLException e) {

			return new ErrorResponse(StatusCode.BAD_REQUEST,
					Integer.toString(e.getErrorCode()));

		} catch (IOException e) {

			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());

		} finally {
			db.close();
		}

		return new MinimalResponse(StatusCode.OK);

	}

}
