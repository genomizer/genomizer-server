package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import response.AddAnnotationFieldResponse;
import response.MinimalResponse;
import response.Response;
import server.DatabaseSettings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import database.DatabaseAccessor;

/* TODO: Fix error handling in execute method.
 *		 Test class vs the database.
 *		 Make JUnit test cases.
 *		 Add some more validation.
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

	//private String[] type;
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

		/* Restrictions on size on name? types?
		 */
		//Check if anything was not set.
		if(name == null || type == null || defaults == null || forced == null) {

			return false;

		}

		//Check if name is to long, no types exists.
		if(name.length() > 10 || type.size() < 1 ) {

			return false;

		}

		return true;

	}

	/**
	 * Method used to execute the command and add the
	 * annoation field.
	 */
	@Override
	public Response execute() {

		Response rsp;
		int addedAnnotations = 0;

		int defaultValueIndex = 0;

		try {

			//Get database access.
			//DatabaseAccessor dbAccess = new DatabaseAccessor("c5dv151_vt14", "shielohh", "postgres", "c5dv151_vt14");
			DatabaseAccessor dbAccess = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);

			//Add types to arraylist to pass them to the database. //TODO: Make pretty.

			/*
			ArrayList<String> types = new ArrayList<String>();
			for(int i = 0; i < type.length; i++) {

				//types.add(type[i]);
				types.add(i, type[i]);
				if(type[i].equals(defaults)) {
					defaultValueIndex = i;
				}

			}
			*/
			for(int i = 0; i < type.size(); i++) {

				if(type.get(i).equals(defaults)) {

					defaultValueIndex = i;
					break;

				}

			}

			//Add annotation field.
			addedAnnotations = dbAccess.addDropDownAnnotation(name, type, defaultValueIndex, forced);

			//Create response.
			if(addedAnnotations != 0) {

				rsp = new AddAnnotationFieldResponse(201);

			} else {

				rsp = new MinimalResponse(400);

			}

		} catch (SQLException e) {

			e.printStackTrace();
			rsp = new MinimalResponse(400);

		} catch (IOException e) {

			e.printStackTrace();
			rsp = new MinimalResponse(400);

		}

		return rsp;

	}

}
