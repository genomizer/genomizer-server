package command;

import response.ErrorResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a command handling experiments.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class ExperimentCommand extends Command {

	/* This class responds on success only
	 * with header = 201 (Created).
	 *
	 *
	 *
	 * This class handles both retrieve and add.
	 */
	/**
	 * Empty constructor.
	 */
	public ExperimentCommand() {

	}

	@Override
	public boolean validate() {

		// TODO Auto-generated method stub
		return false;

	}

	@Override
	public Response execute() {

		// TODO Auto-generated method stub

		return 	new ErrorResponse(StatusCode.NO_CONTENT);

	}

}

/* {
    "name": "experimentName",
    "created by": "user",
    "annotations": {
            "pubmedId": "ex23",
            "type": "raw",
            "specie": "human",
            "genoRelease": "v1.23",
            "cellLine": "yes",
            "devStage": "larva",
            "sex": "male",
            "tissue": "eye"
    }
}
 *
 */

