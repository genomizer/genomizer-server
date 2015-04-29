package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * This class is used to handle changes to annotation values.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class EditAnnotationValueCommand extends Command {
	@Expose
	private String name = null;

	@Expose
	private String oldValue = null;

	@Expose
	private String newValue = null;

	@Override
	public void setFields(String uri, String username) {

		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateString(name, MaxLength.ANNOTATION_LABEL, "Annotation label");
		validateString(oldValue, MaxLength.ANNOTATION_LABEL,
				"Old annotation value");
		validateString(newValue, MaxLength.ANNOTATION_LABEL,
				"New annotation value");
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		try {
			db = initDB();
			ArrayList<String> annotations = db.getAllAnnotationLabels();
			if(annotations.contains(name)) {
				List<String> values = db.getChoices(name);
				if(values.contains(oldValue)) {
					db.changeAnnotationValue(name, oldValue, newValue);
					return new MinimalResponse(StatusCode.OK);
				} else {
					return new ErrorResponse(StatusCode.BAD_REQUEST,
							"The value" + oldValue + " does not exist");
				}
			} else {
				return new ErrorResponse(StatusCode.BAD_REQUEST,
						"The annotation " + name + " does not");
			}

		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	public void setRights(UserType userType){
		this.userType = userType;
	}
}
