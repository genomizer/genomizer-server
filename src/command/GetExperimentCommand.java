package command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import response.GetExperimentResponse;

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
		this.header = rest;
	}

	@Override
	public boolean validate() {

		if(this.header == null) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public Response execute() {

		String username = "c5dv151_vt14";
	    String password = "shielohh";
	    String host = "postgres";
	    String database = "c5dv151_vt14";
	    Experiment exp;
	    DatabaseAccessor db = null;

		try {
			db = new DatabaseAccessor(username, password, host, database);
			exp = db.getExperiment(this.header);
		} catch (SQLException e) {
			return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
		}

		return new GetExperimentResponse(getInfo(exp), exp.getAnnotations(), exp.getFiles(), 200);
	}

	public ArrayList<String> getInfo(Experiment exp) {
		ArrayList<String> info = new ArrayList<String>();
		info.add(exp.getID());
		return info;

	}





}