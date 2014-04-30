package command;

import com.google.gson.annotations.Expose;

import response.Response;

/**
 * Class used to represent a command of the type Search.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class SearchForExperimentsCommand extends Command {

	private String annotations;

	/**
	 * Empty constructor.
	 */
	public SearchForExperimentsCommand() {

	}

	/**
	 * Used to validate the correctness of the
	 * class when built.
	 */
	@Override
	public boolean validate() {

		// TODO Auto-generated method stub (Should maybe be private?)

		return false;

	}

	/**
	 * Runs the actual code needed to search
	 * the database.
	 */
	@Override
	public Response execute() {

		//SearchResult result = database.getsearchresult(annotations)

		// TODO Auto-generated method stub
		return null;
	}

}
