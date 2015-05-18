package command.experiment;

import java.io.IOException;
import java.sql.SQLException;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.constants.MaxLength;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.Response;
import response.GetExperimentResponse;
import database.DatabaseAccessor;
import database.containers.Experiment;
import database.subClasses.UserMethods.UserType;
import server.Debug;

/**
 * Class used to retrieve an experiment from the database.
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

	/**
	 * Overrides the original command in order to use the uri.
	 * @param uri Contains the experiment id to fetch.
	 * @param query the query of the command
	 * @param uuid the UUID for the user who made the request.
	 * @param userType the user type for the command caller.
	 */
	@Override
	public void setFields(String uri, String query, String uuid, UserType userType) {

		super.setFields(uri, query, uuid, userType);
		expID = uri.split("/")[2];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(expID, MaxLength.EXPID, "Experiment name");
	}

	@Override
	public Response execute() {
		Experiment exp;
		DatabaseAccessor db;

		try {
			db = initDB();
		}
		catch (SQLException | IOException e){
			Debug.log("Retrieval of experiment " + expID + " didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Temporarily could not " +
					"initialize db.");
		}
		try {
			exp = db.getExperiment(expID);
		} catch (SQLException e){
			Debug.log("Retrieval of experiment " + expID + " didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Could not get " +
					"experiment: " + expID+ ". The reason was temporary problems with the database.");
		} finally {
				db.close();
		}
		if (exp == null) {
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Experiment "
					+ "with id " + exp.getID() + " could not be found.");
		}
		return new GetExperimentResponse(HttpStatusCode.OK, exp.getID(),
				exp.getAnnotations(), exp.getFiles());
	}
}
