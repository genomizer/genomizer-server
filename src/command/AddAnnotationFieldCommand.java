package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import response.AddAnnotationFieldResponse;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import database.DatabaseAccessor;
import database.MaxSize;

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
	 */
	@Override
	public boolean validate() {

		if(name == null || defaults == null || type.size() < 1) {
			return false;
		}
		if(name.length() < 1 || name.length() > MaxSize.ANNOTATION_LABEL) {
			return false;
		}
		if(defaults.length() < 1 || defaults.length() > MaxSize.ANNOTATION_DEFAULTVALUE) {
			return false;
		}
		if(name.indexOf('/') != -1) {
			return false;
		}

		if(!hasOnlyValidCharacters(name)){
			return false;
		}
		if(!hasOnlyValidCharacters(defaults)){
			return false;
		}

		for(int i = 0; i < type.size(); i++) {

			if(type.get(i).indexOf("/") != -1) {
				return false;
			}
			if(!hasOnlyValidCharacters(type.get(i))){
				return false;
			}

		}
		return true;
	}

	public boolean hasOnlyValidCharacters(String s){
		Pattern p = Pattern.compile("[^A-Za-z0-9 ]");
		return !p.matcher(s).find();
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
			/* Catch dubplicate key*/
			if(e.getErrorCode() == 0) {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The annotation " + name + " already exists.");
			} else {
				return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, "Database unavailable");
			}

		} catch (IOException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.BAD_REQUEST);
		} finally{
				db.close();
		}
	}
}
