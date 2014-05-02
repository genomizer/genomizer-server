package command;

import response.AddAnnotationFieldResponse;
import response.ErrorResponse;
import response.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

		// TODO: connect to database


		//Need to get some kind of boolean as a response if success to add.
		boolean success = true;

		//Add check on user ID, privileges etc.. ?

		if(success) {

			rsp = new AddAnnotationFieldResponse(201);


		} else {

			rsp = new ErrorResponse(400);

		}

		//Method not implemented, send appropriate response
		return 	new ErrorResponse(405);

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