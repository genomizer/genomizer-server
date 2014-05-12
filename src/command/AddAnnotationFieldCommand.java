package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import response.AddAnnotationFieldResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import server.DatabaseSettings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import database.DatabaseAccessor;

/* TODO: Fix error handling in execute method.
 *		 Test class vs the database.
 *		 Make JUnit test cases.
 *		 Add some more validation.
 *		 REFACTOR CODE.
 */

/**
 * Class used to add annotation fields.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddAnnotationFieldCommand extends Command {

	@Expose
	private String name;

	@Expose
	private ArrayList<String> type = new ArrayList<String>();

	@SerializedName("default")
	@Expose
	private String defaults;

	@Expose
	private Boolean forced;

	/**
	 * Empty constructor.
	 */
	public AddAnnotationFieldCommand() {

	}

	/**
	 * Method used to validate all attributes.
	 */
	@Override
	public boolean validate() {
		//Check if name is to long, no types exists.
		if(name.length() > 20 || type.size() < 1 ) {
			return false;
		}
		return true;
	}

	/**
	 * Method used to execute the command and add the
	 * annotation field.
	 */
	@Override
	public Response execute() {

		DatabaseAccessor db = null;
		Response rsp;
		DatabaseAccessor dbAccess = null;
		int addedAnnotations = 0;
		int defaultValueIndex = 0;

		try {
			//Get database access.
			dbAccess = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);

			for(int i = 0; i < type.size(); i++) {
				if(type.get(i).equals(defaults)) {
					defaultValueIndex = i;
					break;
				}
			}
			if(type.size() == 1 && type.get(0).equals("freetext")) {
				addedAnnotations = db.addFreeTextAnnotation(name, defaults, forced);
			} else {
				//Add annotation field.
				addedAnnotations = db.addDropDownAnnotation(name, type, defaultValueIndex, forced);
			}
			//Create response.
			if(addedAnnotations != 0) {
				rsp = new AddAnnotationFieldResponse(StatusCode.CREATED);
			} else {
				rsp = new MinimalResponse(StatusCode.BAD_REQUEST);
			}
		} catch (SQLException e) {

			rsp = new MinimalResponse(StatusCode.BAD_REQUEST);

		} catch (IOException e) {

			rsp = new MinimalResponse(StatusCode.BAD_REQUEST);

		} finally {

			try {

				dbAccess.close();

			} catch (SQLException e) {

				rsp = new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);

			}

		}

		return rsp;
	}

}
