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
import database.containers.Experiment;

import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.Response;
import response.SearchResponse;

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

	@Override
	public void setFields(String uri, String query, String uuid, UserType userType) {

		super.setFields(uri, query, uuid, userType);
		for (String keyVal : query.split("&")) {
			String[] splitKeyVal = keyVal.split("=");
			if (splitKeyVal[0].equals("annotations") && splitKeyVal.length == 2) {
				annotations = splitKeyVal[1];
			}
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
			e.printStackTrace();
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Bad encoding " +
					"on search query.");
		}
		try {
			db = initDB();
			searchResult = db.search(annotations);
		} catch (SQLException | IOException e) {
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					e.getMessage());
		} catch (ParseException e) {
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
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
