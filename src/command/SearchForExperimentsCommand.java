package command;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import java.net.URLDecoder;

import database.DatabaseAccessor;
import database.containers.Experiment;

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
	public boolean validate() throws ValidateException {
		if (annotations == null || annotations.equals("")) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify annotations to search for.");
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
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		}

		try {
			db = initDB();
			searchResult = db.search(annotations);
		} catch (SQLException | IOException e) {
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, e.getMessage());
		} catch (ParseException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			db.close();
		}
		SearchResponse response = new SearchResponse(searchResult);
		return response;
	}

	public String getAnnotations() {
		return annotations;
	}
}
