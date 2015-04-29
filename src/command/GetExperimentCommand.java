package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import database.constants.MaxLength;
import response.ErrorResponse;
import response.Response;
import response.StatusCode;
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
	public void setFields(String uri, String uuid, UserType userType) {
		this.userType = userType;
		expID = uri.split("/")[1];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateString(header, MaxLength.EXPID, "Experiment name");
	}

	@Override
	public Response execute() {
		Experiment exp;
		DatabaseAccessor db;
		try {
			db = initDB();
		}
		catch(SQLException | IOException e){
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Could not " +
					"initialize db: " + e.getMessage());
		}
		try{
			exp = db.getExperiment(expID);
		}catch(SQLException e){
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Could not get " +
					"experiment: " + e.getMessage());
		}
		db.close();
		if(exp == null) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Experiment " +
					"requested from database is null, not found or does not " +
					"exist.");
		}
		return new GetExperimentResponse(StatusCode.OK, getInfo(exp),
				exp.getAnnotations(), exp.getFiles());
	}

	//TODO Handle multiple experiments?
	private ArrayList<String> getInfo(Experiment exp) {
		ArrayList<String> info = new ArrayList<>();
		info.add(exp.getID());
		return info;
	}
}
