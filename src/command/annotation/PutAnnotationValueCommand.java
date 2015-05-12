package command.annotation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.HttpStatusCode;

/**
 * This class is used to handle changes to annotation values.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PutAnnotationValueCommand extends Command {
	@Expose
	private String name = null;

	@Expose
	private String oldValue = null;

	@Expose
	private String newValue = null;

	@Override
	public void setFields(String uri, String uuid, UserType userType) {

		super.setFields(uuid, userType);
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(name, MaxLength.ANNOTATION_LABEL, "Annotation label");
		validateName(oldValue, MaxLength.ANNOTATION_LABEL,
				"Old annotation value");
		validateName(newValue, MaxLength.ANNOTATION_LABEL,
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
					return new MinimalResponse(HttpStatusCode.OK);
				} else {
					return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
							"The value" + oldValue + " does not exist");
				}
			} else {
				return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"The annotation " + name + " does not");
			}

		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
}
