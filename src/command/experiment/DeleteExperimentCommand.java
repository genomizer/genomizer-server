package command.experiment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

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
import server.Debug;

/**
 * Class used to represent a remove experiment command.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteExperimentCommand extends Command {
	private String expID;


	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	/**
	 * Set the UserType Uri and Uuid. expID also set from uri.
	 * @param uri the URI from the http request.
	 * @param username the uuid from the http request.
	 * @param userType the userType
	 */
	@Override
	public void setFields(String uri, HashMap<String, String> query,
						  String username, UserType userType) {

		super.setFields(uri, query, username, userType);
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
		} catch (SQLException | IOException e) {
			Debug.log("Deletion of experiment " + expID + " didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Deletion of experiment " + expID +
					" didn't work due to temporary problems with the database.");
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return new MinimalResponse(HttpStatusCode.OK);
	}
}
