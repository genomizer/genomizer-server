package command.experiment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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
 * Class used to retrieve an experiment.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class GetExperimentCommand extends Command {
	private String expID;

	/**
	 * Set the UserType Uri and Uuid. expID also set from uri.
	 * @param uri the URI from the http request.
	 * @param uuid the uuid from the http request.
	 * @param userType the userType
	 */
	@Override
	public void setFields(String uri, String uuid, UserType userType) {

		super.setFields(uri, uuid, userType);
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
		catch(SQLException | IOException e){
			Debug.log("Retrieval of experiment " + expID + " didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Temporarily could not " +
					"initialize db.");
		}
		try{
			exp = db.getExperiment(expID);
		}catch(SQLException e){
			Debug.log("Retrieval of experiment " + expID + " didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Could not get " +
					"experiment: " + expID+ ". The reason was temporary problems with the database.");
		}finally {
				db.close();
		}
		if(exp == null) {
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Experiment " +
					"requested from database is null, not found or does not " +
					"exist.");
		}
		return new GetExperimentResponse(HttpStatusCode.OK, getInfo(exp),
				exp.getAnnotations(), exp.getFiles());
	}

	//TODO Handle multiple experiments?
	private ArrayList<String> getInfo(Experiment exp) {
		ArrayList<String> info = new ArrayList<>();
		info.add(exp.getID());
		return info;
	}
}
