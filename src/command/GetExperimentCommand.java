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
			exp = db.getExperiment(header);
		} catch (SQLException | IOException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally{
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, "Could not close database connection");
			}
		}
		if(exp == null) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Experiment requested from database is null, not found or does not exist.");
		}
		return new GetExperimentResponse(getInfo(exp), exp.getAnnotations(), exp.getFiles(), StatusCode.OK);
	}

	public ArrayList<String> getInfo(Experiment exp) {
		ArrayList<String> info = new ArrayList<String>();
		info.add(exp.getID());
		return info;
	}

}