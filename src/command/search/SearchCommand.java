package command.search;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.subClasses.UserMethods.UserType;
import response.*;
import server.Debug;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

/**
 * Command used to issue a search.
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
	public void setFields(String uri, HashMap<String, String> query,
						  String uuid, UserType userType) {
		super.setFields(uri, query, uuid, userType);
		if(query.containsKey("annotations"))
			annotations = query.get("annotations");

		if(annotations == null || annotations.isEmpty())
			annotations = ("[expID]");
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
	}

	@Override
	public Response execute() {
		Response response;
		try (DatabaseAccessor db = initDB()) {
			annotations = URLDecoder.decode(annotations, "UTF-8");
			response = new ExperimentListResponse(db.search(annotations));
		} catch (UnsupportedEncodingException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Search for '" + annotations + "' unsuccessful, bad " +
							"encoding");
		} catch (ParseException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Search for '" + annotations + "' unsuccessful, " +
							"incorrect date format");
		} catch (SQLException e) {
			response = new DatabaseErrorResponse("Search for '" + annotations +
					"'");
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Search for '" + annotations + "' unsuccessful. " +
							e.getMessage());
			Debug.log("Reason:" + e.getMessage());
		}

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
