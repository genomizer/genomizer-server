package command;

import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import database.SearchResult;
import databaseAccessor.DatabaseAccessor;

import response.ErrorResponse;
import response.Response;
import response.SearchResponse;
import response.StatusCode;

/**
 * Class used to represent a command of the type Search.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class SearchForExperimentsCommand extends Command {

	private String annotations;

	/**
	 * Empty constructor.
	 * @param params
	 */
	public SearchForExperimentsCommand(String params) {
		annotations = params;
	}

	/**
	 * Used to validate the correctness of the
	 * class when built.
	 */
	@Override
	public boolean validate() {

		// TODO Auto-generated method stub (Should maybe be private?)

		return false;

	}

	/**
	 * Runs the actual code needed to search
	 * the database.
	 */
	@Override
	public Response execute() {
	    String username = "c5dv151_vt14";
	    String password = "shielohh";
	    String host = "postgres";
	    String database = "c5dv151_vt14";
	    DatabaseAccessor db = null;
		try {
			db = new DatabaseAccessor(username, password, host, database);
		} catch (SQLException e) {
			return new ErrorResponse(503);
		}
		SearchResult result = db.searchExperiment(annotations);

		SearchResponse response = new SearchResponse();

		//Method not implemented, send appropriate response
		return 	new ErrorResponse(StatusCode.NO_CONTENT);
	}

	public String getAnnotations() {
		return annotations;
	}
}
