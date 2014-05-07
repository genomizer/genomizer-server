package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;
import database.Experiment;

import response.MinimalResponse;
import response.Response;
import response.SearchResponse;
import response.StatusCode;
import server.DatabaseSettings;

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

	    DatabaseAccessor db = null;
	    List<Experiment> searchResult = null;

		try {
			db = new DatabaseAccessor(DatabaseSettings.database, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			searchResult = db.search(annotations);
		} catch (SQLException e) {
			return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
		} catch (IOException e) {
			return new MinimalResponse(StatusCode.BAD_REQUEST);
		}


		SearchResponse response = new SearchResponse(searchResult);

		return response;
	}

	public String getAnnotations() {
		return annotations;
	}
}
