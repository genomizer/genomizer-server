package command.search;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import java.net.URLDecoder;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.Experiment;

import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.Response;
import response.SearchResponse;
import server.Debug;

/**
 * Class used to handle searching for an experiment.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class SearchCommand extends Command {
	private String annotations;

	@Override
	public void setFields(String uri, String uuid, UserType userType) {

		super.setFields(uri, uuid, userType);
		int index = uri.indexOf("=");
		annotations = uri.substring(index+1);
	}

	@Override
	public void validate() throws ValidateException {

		hasRights(UserRights.getRights(this.getClass()));

		if (annotations == null || annotations.equals("")) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST,
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
			Debug.log("Search with annotations: "+annotations+" failed due to bad encoding. "+e.getMessage());
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Search with annotations: "+annotations+
					" failed due to bad encoding.");
		}
		try {
			db = initDB();
			searchResult = db.search(annotations);
		} catch (SQLException | IOException e) {
			Debug.log("Search with annotations: " + annotations + " didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Search with annotations: "+annotations+
					" didn't work because of temporary problems with database.");
		} catch (ParseException e) {
			Debug.log("Search with annotations: " + annotations + " didn't work. Incorrect date format. " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Search failed due to incorrect date format. " +
					"Should be on form 20150314-20150422 for interval and 20150411 for single dates.");
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
