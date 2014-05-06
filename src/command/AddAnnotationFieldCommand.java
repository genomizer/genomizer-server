package command;

import java.sql.SQLException;

import response.AddAnnotationFieldResponse;
import response.ErrorResponse;
import response.Response;
import response.StatusCode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import databaseAccessor.DatabaseAccessor;

/**
 * Class used to add annotation fields.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddAnnotationFieldCommand extends Command {

	@Expose
	private String name;

	@Expose
	private String[] type;

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

		//TODO: Add some more validation.

		if(name == null || type == null || defaults == null || forced == null) {

			return false;

		}

		return true;

	}

	@Override
	public Response execute() {

		Response rsp;
		int addedAnnotations = 0;
		
		try {
			
			//Get database access.
			DatabaseAccessor dbAccess = new DatabaseAccessor("c5dv151_vt14", "shielohh", "postgres", "c5dv151_vt14");
			
			//Add freetext field.
			//addedAnnotations = dbAccess.addFreeTextAnnotation(label);
			
			//Create response.
			if(addedAnnotations != 0) {
				
				rsp = new AddAnnotationFieldResponse(201);
				
			} else {

				rsp = new ErrorResponse(400);
				
			}
			
		} catch (SQLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ErrorResponse(400);
			
		}

		//Method not implemented, send appropriate response
		return 	new ErrorResponse(StatusCode.NO_CONTENT);

	}

}

/*
{
"name": "species",
"type": [
         "fly",
         "rat",
         "human"
        ],
"default": "human",
"forced": "true/false"
}
*/