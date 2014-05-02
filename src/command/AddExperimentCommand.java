package command;

import javax.xml.ws.Response;

import response.ErrorResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class used to add an experiment represented as a command.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddExperimentCommand extends Command {

	@Expose
	private String name;

	@SerializedName("created by")
	@Expose
	private String created_by;

	@Expose
	private Annotations annotations = new Annotations();

	/**
	 * Empty constructor.
	 */
	public AddExperimentCommand() {

	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public response.Response execute() {

		//Method not implemented, send appropriate response
		return 	new ErrorResponse(405);
	}






}
