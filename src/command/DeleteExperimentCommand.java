package command;

import java.io.IOException;
import java.sql.SQLException;

import database.DatabaseAccessor;

import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a remove experiment command.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteExperimentCommand extends Command {
	private String expID;

	@Override
	public void setFields(String uri, String uuid, UserType userType) {
		this.userType = userType;
		expID = uri.split("/")[1];
	}

	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateString(header, MaxLength.EXPID, "Experiment name");
	}

	public Response execute() {
		DatabaseAccessor db = null;
		try {
			db = initDB();
			int tuples = db.deleteExperiment(expID);
			if(tuples == 0) {
				return new ErrorResponse(StatusCode.BAD_REQUEST,
						"The experiment " + expID + " does not exist and " +
								"can not be deleted");

			}
		} catch (SQLException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST,
					Integer.toString(e.getErrorCode()));

		} catch (IOException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return new MinimalResponse(StatusCode.OK);
	}
}
