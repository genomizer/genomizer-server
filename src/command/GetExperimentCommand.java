package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import response.GetExperimentResponse;
import server.DatabaseSettings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import database.DatabaseAccessor;
import database.Experiment;
import database.FileTuple;

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

	@Override
	public boolean validate() {
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