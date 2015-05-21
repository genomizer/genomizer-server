package command.search;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.containers.Experiment;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.Response;
import response.SearchResponse;
import server.Debug;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

/**
 * Class used to handle searching for an experiment.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class SearchCommand extends Command {
	private String annotations;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 1;
	}


	/**
	 * Set the UserType Uri and Uuid. annotations also set from uri.
	 * @param uri the URI from the http request.
	 * @param username the userName from the http request.
	 * @param userType the userType
	 */
	@Override
	public void setFields(String uri, HashMap<String, String> query,
						  String username, UserType userType) {

		super.setFields(uri, query, username, userType);
		if(query.containsKey("annotations")) {
			annotations = query.get("annotations");
		}
		else {
			annotations = ("[expID]");
		}
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
	}

	@Override
	public Response execute() {
	    DatabaseAccessor db = null;
	    List<Experiment> searchResult = null;
		try {
			annotations = URLDecoder.decode(annotations, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Debug.log("Search with annotations: " + annotations + " failed due to bad encoding. " + e.getMessage());
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Search with annotations: "+annotations+
					" failed due to bad encoding.");
		}
		try {
			db = initDB();
			searchResult = db.search(annotations);
		} catch (SQLException e) {
			Debug.log("Search with annotations: " + annotations + " didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Search with annotations: "+annotations+
					" didn't work because of temporary problems with database.");
		}catch (IOException e){
			Debug.log("Search with annotations: " + annotations + " didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Search failed due to query having incorrect format.");

		}catch (ParseException e) {
			Debug.log("Search with annotations: " + annotations + " didn't work. Incorrect date format. " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Search failed due to incorrect date format. " +
					"Should be on form 20150314-20150422 for interval and 20150411 for single dates.");
		} finally {
			if (db != null)
				db.close();
		}
		return new SearchResponse(searchResult);
	}

	/**
	 * Returns the annotations used to query for the experiment.
	 * @return the annotations used to query for the experiment.
	 */
	public String getAnnotations() {
		return annotations;
	}
}
