package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import response.AddAnnotationFieldResponse;
import response.ErrorResponse;
import response.Response;
import response.StatusCode;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import database.DatabaseAccessor;
import database.constants.MaxSize;


/**
 * Class used to add annotation fields.
 *
 * @author tfy09jnn, Hugo Källström
 * @version 1.1
 */
public class AddAnnotationFieldCommand extends Command {

	@Expose
	private String name = null;;

	@Expose
	private ArrayList<String> type = new ArrayList<String>();

	@SerializedName("default")
	@Expose
	private String defaults = null;

	@Expose
	private Boolean forced = null;

	/**
	 * Empty constructor.
	 */
	public AddAnnotationFieldCommand() {

	}

	/**
	 * Method used to validate all attributes.
	 *
	 *
	 */
	@Override
	public boolean validate() throws ValidateException {

		if(name == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify a name for the annotation.");
		}
		if(type == null || type.size() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify a type for the annotation.");
		}
		if(forced == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify if the value is forced.");
		}
		if(name.length() > MaxSize.ANNOTATION_LABEL || name.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Annotation label has to be between 1 and "
					+ database.constants.MaxSize.ANNOTATION_LABEL + " characters long.");
		}
		if(defaults != null) {
			if(defaults.length() > MaxSize.ANNOTATION_DEFAULTVALUE || defaults.length() < 1) {
				throw new ValidateException(StatusCode.BAD_REQUEST, "Annotation default value has to be between 1 and "
						+ database.constants.MaxSize.ANNOTATION_DEFAULTVALUE + " characters long.");
			}
			if(!hasOnlyValidCharacters(defaults)){
				throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in annotation default value. Valid characters are: a-z, A-Z, 0-9");
			}
		}
		if(name.indexOf('/') != -1 || !hasOnlyValidCharacters(name)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in annotation name. Valid characters are: a-z, A-Z, 0-9");
		}

		for(int i = 0; i < type.size(); i++) {
			if(type.get(i).indexOf("/") != -1) {
				throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in annotation type. Valid characters are: a-z, A-Z, 0-9");
			}
			if(!hasOnlyValidCharacters(type.get(i))){
				throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in annotation type. Valid characters are: a-z, A-Z, 0-9");
			}
		}
		return true;
	}


	/**
	 * Method used to execute the command and add the
	 * annotation field.
	 */
	@Override
	public Response execute() {

		int addedAnnotations = 0;
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
				addedAnnotations = db.addFreeTextAnnotation(name, defaults, forced);
			} else {
				addedAnnotations = db.addDropDownAnnotation(name, type, defaultValueIndex, forced);
			}
			if(addedAnnotations != 0) {
				return new AddAnnotationFieldResponse(StatusCode.CREATED);
			} else {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "Annotation could not be added, database error.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			if(e.getErrorCode() == 0) {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The annotation " + name + " already exists.");
			} else {
				return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, e.getMessage());
			}

		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally{
				db.close();
		}
	}
}
