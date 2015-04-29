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
 * Class used to handle searching for an experiment.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class SearchForExperimentsCommand extends Command {
	private String annotations;

	/**
	 * Constructs a new instance of SearchForExperimentsCommand using the
	 * supplied annotations.
	 * @param annotations the annotations (hopefully) belonging to the
	 *                       experiment.
	 */
	public SearchForExperimentsCommand(String annotations) {
		this.annotations = annotations;
	}

	@Override
	public void validate() throws ValidateException {
		if (annotations == null || annotations.equals("") ||
				annotations.equals("null")) {
			throw new ValidateException(StatusCode.BAD_REQUEST,
					"Specify annotations to search for.");
		}
	}

	@Override
	public Response execute() {
	    DatabaseAccessor db = null;
	    List<Experiment> searchResult = null;
		try {
			annotations = URLDecoder.decode(annotations, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Bad encoding " +
					"on search query.");
		}
		try {
			db = initDB();
			searchResult = db.search(annotations);
		} catch (SQLException | IOException e) {
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE,
					e.getMessage());
		} catch (ParseException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			if (db != null)
				db.close();
		}
		SearchResponse response = new SearchResponse(searchResult);
		return response;
	}

	/**
	 * Returns the annotations used to query for the experiment.
	 * @return the annotations used to query for the experiment.
	 */
	public String getAnnotations() {
		return annotations;
	}
}
