package command.annotation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.subClasses.UserMethods.UserType;
import response.AddAnnotationFieldResponse;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.Response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import database.DatabaseAccessor;
import database.constants.MaxLength;

/**
 * This class is created to handle the process of adding a new
 * annotation-field. Both normal and free-text annotation-field adding is
 * supported.

 * @author Business Logic 2015.
 * @version 1.1
 */
public class PostAnnotationFieldCommand extends Command {
	@Expose
	private String name = null;

	@Expose
	private ArrayList<String> type = new ArrayList<>();

	@SerializedName("default")
	@Expose
	private String defaults = null;

	@Expose
	private Boolean forced = null;


	@Override
	public void validate() throws ValidateException {
		
		hasRights(UserRights.getRights(this.getClass()));

		validateName(name, MaxLength.ANNOTATION_LABEL, "Annotation label");
		
		if(defaults != null) {
			validateName(defaults, MaxLength.ANNOTATION_DEFAULTVALUE,
					"Default value");
		}

		if(forced == null) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Specify if " +
					"the value is forced.");
		}

		if(type == null || type.size() < 1) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Specify a " +
					"type for the annotation.");
		}

		for(int i = 0; i < type.size(); i++) {
			validateName(type.get(i), MaxLength.ANNOTATION_VALUE, "Annotation type");
		}
	}

	@Override
	public Response execute() {
		int addedAnnotations;
		int defaultValueIndex = 0;
		DatabaseAccessor db = null;

		try {
			db = initDB();
			for(int i = 0; i < type.size(); i++) {
				if(type.get(i).equals(defaults)) {
					defaultValueIndex = i;
					break;
				}
			}
			if(type.size() == 1 && type.get(0).equals("freetext")) {
				addedAnnotations = db.addFreeTextAnnotation(name, defaults,
						forced);
			} else {
				addedAnnotations = db.addDropDownAnnotation(name, type,
						defaultValueIndex, forced);
			}
			if(addedAnnotations != 0) {
				return new AddAnnotationFieldResponse(HttpStatusCode.CREATED);
			} else {
				return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Annotation " +
						"could not be added, database error.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			if(e.getErrorCode() == 0) {
				return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "The " +
						"annotation " + name + " already exists.");
			} else {
				return new ErrorResponse(HttpStatusCode.SERVICE_UNAVAILABLE,
						e.getMessage());
			}

		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
}
