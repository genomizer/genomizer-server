package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import response.AddAnnotationFieldResponse;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import database.DatabaseAccessor;

/**
 * Class used to add annotation fields.
 *
 * @author tfy09jnn, Hugo Källström
 * @version 1.1
 */
public class AddAnnotationFieldCommand extends Command {

	@Expose
	private String name;

	@Expose
	private ArrayList<String> type = new ArrayList<String>();

	@SerializedName("default")
	@Expose
	private String defaults;

	@Expose
	private Boolean forced;

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
		if(name.length() > 20 || type.size() < 1 ) {
			return false;
		}
		return true;
	}

	/**
	 * Method used to execute the command and add the
	 * annoation field.
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
				System.out.println(forced);
				addedAnnotations = db.addFreeTextAnnotation(name, defaults, forced);
			} else {
				System.out.println(forced);
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
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
