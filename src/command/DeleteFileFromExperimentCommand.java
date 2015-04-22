package command;

import java.io.IOException;
import java.sql.SQLException;

import database.DatabaseAccessor;
import database.constants.MaxSize;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a command that is used to
 * delete a file from an experiment.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteFileFromExperimentCommand extends Command {

	/**
	 * Constructs a new instance of DeleteFileFromExperimentCommand using the
	 * supplied restful string.
	 * @param restful string to set.
	 */
	public DeleteFileFromExperimentCommand(String restful) {
		this.setHeader(restful);
	}

	@Override
	public void validate() throws ValidateException {
		validateString(header, MaxSize.EXPID, "Experiment name");
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		try {
			db = initDB();
			try {
				if(db.deleteFile(Integer.parseInt(header))==1) {
					return new MinimalResponse(StatusCode.OK);
				} else {
					return new ErrorResponse(StatusCode.BAD_REQUEST,
							"The file " + header + " does not exist and can " +
									"not be deleted");
				}
			} catch (NumberFormatException e) {
				if (db.deleteFile(header) > 0) {
					return new MinimalResponse(StatusCode.OK);
				} else {
					return new ErrorResponse(StatusCode.BAD_REQUEST,
							"The file " + header + " does not exist and can " +
									"not be deleted");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE,
					e.getMessage());
		} catch (IOException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
}
