package command.annotation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import command.Command;
import command.UserRights;
import command.ValidateException;
import response.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import server.Debug;

/**
 * Command used to add a new annotation field. Both normal free text fields
 * are supported.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PostAnnotationFieldCommand extends Command {
	@Expose
	private String name = null;
	@Expose
	private ArrayList<String> type = null;
	@Expose
	@SerializedName("default")
	private String defaults = null;
	@Expose
	private Boolean forced = null;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(name, MaxLength.ANNOTATION_LABEL, "Annotation label");
		defaults = "";
		if (forced == null) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST,
					"Adding annotation field was unsuccessful, specify if " +
							"the value should be forced.");
		}

		if (type == null || type.size() < 1) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST,
					"Adding annotation field was unsuccessful, specify " +
							"type(s) for the field.");
		}

		for (String t : type) {
			validateName(t, MaxLength.ANNOTATION_VALUE, "Annotation type");
		}

		if (type.contains("freetext") && type.size() > 1) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST,
					"Adding annotation field was unsuccessful, can not add a " +
							"dropdown option called \"freetext\"");
		}

		type.add(0, "");
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		Response response;

		try {
			db = initDB();
			if (type.size() == 1 && type.get(0).equals("freetext")) {
				db.addFreeTextAnnotation(name, defaults, forced);
			} else {
				int defaultValueIndex = type.indexOf(defaults);
				if (defaultValueIndex == -1)
					defaultValueIndex = 0;
				db.addDropDownAnnotation(name, type, defaultValueIndex,
						forced);
			}

			response = new MinimalResponse(HttpStatusCode.OK);
		} catch (SQLException e) {
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Adding annotation field " + name + " unsuccessful due " +
							"to temporary database problems");
			Debug.log("Reason :" + e.getMessage());
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Adding annotation field '" + name + "' unsuccessful. " +
							e.getMessage());
		} finally {
			if (db != null)
				db.close();
		}

		return response;
	}
}
