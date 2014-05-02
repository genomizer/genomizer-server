package command;

import response.ErrorResponse;
import response.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//TODO: Implement response.

/**
 * Class used to retrieve an experiment.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class RetrieveExperimentCommand extends Command {

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
	public RetrieveExperimentCommand() {

	}

	@Override
	public boolean validate() {

		// TODO Auto-generated method stub
		return false;

	}

	@Override
	public Response execute() {

		//Method not implemented, send appropriate response
		return 	new ErrorResponse(405);

	}

}