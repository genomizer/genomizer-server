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
 * @author tfy09jnn, Hugo Källström
 * @version 1.1
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
	 * Method that validates the DeleteFileFromExperimentCommand
	 * class.
	 *
	 * @return boolean depending on result.
	 * @throws ValidateException
	 */
	@Override
	public boolean validate() throws ValidateException {

		if(header == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "File-id was missing.");
		} else if(header.length() < 1 || header.length() > database.constants.MaxSize.FILE_EXPID) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "File-id has to be between 1 and "
					+ database.constants.MaxSize.FILE_EXPID + " characters long.");
		} else if(!hasOnlyValidCharacters(header)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in file name. Valid characters are: a-z, A-Z, 0-9");
		}

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
			try {
				if(db.deleteFile(Integer.parseInt(header))==1) {
					return new MinimalResponse(StatusCode.OK);
				} else {
					return new ErrorResponse(StatusCode.BAD_REQUEST, "The file " + header + " does not exist and can not be deleted");
				}
			} catch (NumberFormatException e) {
				if (db.deleteFile(header) > 0) {
					return new MinimalResponse(StatusCode.OK);
				} else {
					return new ErrorResponse(StatusCode.BAD_REQUEST, "The file " + header + " does not exist and can not be deleted");
				}
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
