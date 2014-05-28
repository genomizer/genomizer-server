package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import response.ErrorResponse;
import response.Response;
import response.StatusCode;
import response.GetExperimentResponse;
import database.DatabaseAccessor;
import database.containers.Experiment;

/**
 * Class used to retrieve an experiment.
 *
 * @author tfy09jnn, Hugo Källström
 * @version 1.1
 */
public class GetExperimentCommand extends Command {

	/**
	 * Empty constructor.
	 */
	public GetExperimentCommand(String rest) {

		header = rest;

	}

	/**
	 * Method used to validate the GetExperimentCommand class.
	 *
	 * @return boolean depending on result.
	 * @throws ValidateException
	 */
	@Override
	public boolean validate() throws ValidateException {

		if(header == null) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Experiment name was missing.");

		} else if(header.length() < 1 || header.length() > database.constants.MaxSize.EXPID) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Experiment name has to be between 1 and "
					+ database.constants.MaxSize.EXPID + " characters long.");

		} else if(!hasOnlyValidCharacters(header)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in experiment name. Valid characters are: a-z, A-Z, 0-9");
		}

		return true;

	}

	@Override
	public Response execute() {

		Experiment exp;
		DatabaseAccessor db = null;

		try {
			db = initDB();
		}
		catch(SQLException | IOException e){
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Could not initialize db: " + e.getMessage());
		}

		try{
			exp = db.getExperiment(header);
		}catch(SQLException e){
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Could not get experiment: " + e.getMessage());
		}

		db.close();

	if(exp == null) {
		return new ErrorResponse(StatusCode.BAD_REQUEST, "Experiment requested from database is null, not found or does not exist.");
	}
	return new GetExperimentResponse(StatusCode.OK, getInfo(exp), exp.getAnnotations(), exp.getFiles());
}

public ArrayList<String> getInfo(Experiment exp) {
	ArrayList<String> info = new ArrayList<String>();
	info.add(exp.getID());
	return info;
}

}
