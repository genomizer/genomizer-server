package command.experiment;

import java.io.IOException;
import java.sql.SQLException;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;

import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;

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

		super.setFields(uuid, userType);
		expID = uri.split("/")[2];
	}

	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(expID, MaxLength.EXPID, "Experiment name");
	}

	public Response execute() {
		DatabaseAccessor db = null;
		try {
			db = initDB();
			int tuples = db.deleteExperiment(expID);
			if(tuples == 0) {
				return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"The experiment " + expID + " does not exist and " +
								"can not be deleted");

			}
		} catch (SQLException e) {
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					Integer.toString(e.getErrorCode()));

		} catch (IOException e) {
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return new MinimalResponse(HttpStatusCode.OK);
	}
}
