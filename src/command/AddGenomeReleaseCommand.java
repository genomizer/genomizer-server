package command;

import com.google.gson.annotations.Expose;

import response.Response;

/**
 * Class used to handle adding a genome release.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddGenomeReleaseCommand extends Command {

	@Expose
	String temporaryString = null;


	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Response execute() {
		// TODO Auto-generated method stub
		return null;
	}

}
