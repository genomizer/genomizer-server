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

/**
 * Class used to retrieve an experiment.
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
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Could not " +
					"initialize db: " + e.getMessage());
		}
		try{
			exp = db.getExperiment(expID);
		}catch(SQLException e){
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Could not get " +
					"experiment: " + e.getMessage());
		}
		db.close();
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
