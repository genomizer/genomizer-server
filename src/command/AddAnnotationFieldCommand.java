package command;

import response.AddAnnotationFieldResponse;
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

	@Override
	public boolean validate() {

		// TODO Auto-generated method stub
		return false;

	}

	@Override
	public Response execute() {

		Response rsp;

		boolean success = true;

		if(success) {

			rsp = new AddAnnotationFieldResponse(201);


		} else {

			rsp = new AddAnnotationFieldResponse(400);

		}

		return rsp;

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