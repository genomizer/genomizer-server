package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;

/**
 * Class used to handle the process of adding annotation
 * values.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class AddAnnotationValueCommand extends Command {
	@Expose
	private String name = null;

	@Expose
	private String value = null;

	@Override
	public void setFields(String uri, String username, UserType userType) {
		this.userType = userType;

		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(name, MaxLength.ANNOTATION_LABEL, "Annotation label");
		validateName(value, MaxLength.ANNOTATION_VALUE, "Annotation value");
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		try {
			db = initDB();
			List<String> values = db.getChoices(name);
			if(values.contains(value)) {
				db.close();
				return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "The " +
						"annotation " + name + " already contains the value " +
						value);
			}
			db.addDropDownAnnotationValue(name, value);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorResponse(HttpStatusCode.SERVICE_UNAVAILABLE,
					e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
 		}

		return new MinimalResponse(HttpStatusCode.CREATED);
	}
}
