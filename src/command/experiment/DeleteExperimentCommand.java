package command.experiment;

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

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Command used to remove an experiment.
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
		Response response;

		try (DatabaseAccessor db = initDB()) {
			if (db.deleteExperiment(expID) != 0)
				response = new MinimalResponse(HttpStatusCode.OK);
			else
				response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"Deletion of experiment unsuccessful, experiment '" +
								expID + "' does not exist");
		} catch (SQLException e) {
				response = new ErrorResponse(HttpStatusCode.
						INTERNAL_SERVER_ERROR, "Deletion of experiment'" +
						expID + "' unsuccessful due to temporary database " +
						"problems.");
			Debug.log("Reason: " + e.getMessage());
		} catch (IOException e) {
				response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"Deletion of experiment '" + expID +
								"' unsuccessful. " + e.getMessage());
		}

		return response;
	}
}
