package command.annotation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;
import server.Debug;

/**
 * Class used to handle removal on an existing annotation-field.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteAnnotationFieldCommand extends Command {
	private String name;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 3;
	}

	@Override
	public void setFields(String uri, String uuid, UserType userType) {

		super.setFields(uri, uuid, userType);
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
				return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"The annotation " + name + " does not exist and " +
								"can not be deleted");
			}
		} catch (SQLException | IOException e) {
			Debug.log("Removal of annotation field: "+name+" didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Removal of annotation field:"+name+
					" didn't work because of temporary problems with database.");
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

}
