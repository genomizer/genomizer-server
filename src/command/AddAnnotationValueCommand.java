package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

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
	public void setFields(String uri, String uuid) {

		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
	}

	@Override
	public void validate() throws ValidateException {
		validateString(name, MaxLength.ANNOTATION_LABEL, "Annotation label");
		validateString(value, MaxLength.ANNOTATION_VALUE, "Annotation value");
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		try {
			db = initDB();
			List<String> values = db.getChoices(name);
			if(values.contains(value)) {
				db.close();
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The " +
						"annotation " + name + " already contains the value " +
						value);
			}
			db.addDropDownAnnotationValue(name, value);
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

		return new MinimalResponse(StatusCode.CREATED);
	}

}
