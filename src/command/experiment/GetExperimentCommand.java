package command.experiment;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.Experiment;
import database.subClasses.UserMethods.UserType;
import response.*;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Command used to retrieve an experiment.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class GetExperimentCommand extends Command {
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

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(expID, MaxLength.EXPID, "Experiment name");
	}

	@Override
	public Response execute() {
		Response response;

		try (DatabaseAccessor db = initDB()) {
			Experiment exp;
			if ((exp = db.getExperiment(expID)) != null)
				response = new SingleExperimentResponse(exp);
			else
				response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"Retrieval of experiment '" + expID +
								"' unsuccessful, experiment does not exist.");
		} catch (SQLException e) {
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Retrieval of experiment '" + expID +
							"' unsuccessful due to temporary database " +
							"problems");
			Debug.log("Reason: " + e.getMessage());
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Retrieval of experiment '" + expID + "' unsuccessful. " +
							e.getMessage());
		}

		return response;
	}
}
