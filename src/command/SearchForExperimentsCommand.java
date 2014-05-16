package command;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import java.net.URLDecoder;

import database.DatabaseAccessor;
import database.Experiment;

import response.MinimalResponse;
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
		if (annotations == null) {
			return false;
		}
		return true;
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
			annotations = URLDecoder.decode(annotations, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.BAD_REQUEST);
		}

		try {
			db = initDB();
			searchResult = db.search(annotations);
		} catch (SQLException e) {
			return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
		} catch (IOException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.BAD_REQUEST);
		} finally {
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
			}
		}
		SearchResponse response = new SearchResponse(searchResult);
		return response;
	}

	public String getAnnotations() {
		return annotations;
	}
}
