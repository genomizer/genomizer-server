package command;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import java.net.URLDecoder;

import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.Experiment;

import database.subClasses.UserMethods.UserType;
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

	@Override
	public void setFields(String uri, String uuid, UserType userType) {
		this.userType = userType;
		int index = uri.indexOf("=");
		annotations = uri.substring(index+1);
	}

	@Override
	public void validate() throws ValidateException {

		hasRights(UserRights.getRights(this.getClass()));

		if (annotations == null || annotations.equals("")) {
			throw new ValidateException(StatusCode.BAD_REQUEST,
					"Specify annotations to search for.");
		}
		validateExists(annotations, MaxLength.ANNOTATION_VALUE, "Experiment ");
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
