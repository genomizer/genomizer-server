package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to handle removal on an existing annotation-field.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteAnnotationFieldCommand extends Command {
	private String name;

	@Override
	public void setFields(String uri, String uuid, UserType userType) {
		this.userType = userType;
		name = uri.split("/")[3];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(name, MaxLength.ANNOTATION_LABEL, "Annotation label");
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		try {
			db = initDB();
			ArrayList<String> annotations = db.getAllAnnotationLabels();

			if(annotations.contains(name)) {
				db.deleteAnnotation(name);
				return new MinimalResponse(200);
			} else {
				return new ErrorResponse(StatusCode.BAD_REQUEST,
						"The annotation " + name + " does not exist and " +
								"can not be deleted");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE,
					e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

}
