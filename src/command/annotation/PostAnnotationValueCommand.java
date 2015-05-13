package command.annotation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;
import server.Debug;

/**
 * Class used to handle the process of adding annotation
 * values.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PostAnnotationValueCommand extends Command {
	@Expose
	private String name = null;

	@Expose
	private String value = null;


	/**
	 * Set the UserType. Uri and Uuid not used in this command.
	 * @param uri the URI from the http request.
	 * @param uuid the uuid from the http request.
	 * @param userType the userType
	 */
	@Override
	public void setFields(String uri, String uuid, UserMethods.UserType userType) {
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
		} catch(SQLException | IOException e) {
			Debug.log("Adding of annotation value: "+value+" on annotation "+name+" failed. Reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Could not add annotation value: "
					+value+ " on annotation "+name+" because of temporary problems with database.");
		} finally {
			if (db != null) {
				db.close();
			}
 		}

		return new MinimalResponse(HttpStatusCode.OK);
	}
}
