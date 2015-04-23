package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import database.constants.MaxSize;
import response.ErrorResponse;
import response.Response;
import response.StatusCode;
import response.GetExperimentResponse;
import database.DatabaseAccessor;
import database.containers.Experiment;

/**
 * Class used to retrieve an experiment.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class GetExperimentCommand extends Command {
	/**
	 * Constructs a new instance of GetExperimentCommand using the supplied
	 * experiment ID.
	 * @param expID header to set.
	 */
	public GetExperimentCommand(String expID) {
		this.setHeader(expID);
	}

	@Override
	public void validate() throws ValidateException {
		validateString(header, MaxSize.EXPID, "Experiment name");
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
			exp = db.getExperiment(header);
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
		ArrayList<String> info = new ArrayList<String>();
		info.add(exp.getID());
		return info;
	}
}
