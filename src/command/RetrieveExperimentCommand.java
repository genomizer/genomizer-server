package command;

import com.google.gson.annotations.Expose;

/**
 * Class used to retrieve an experiment.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class RetrieveExperimentCommand extends Command {

	@Expose
	private String name;
	
	@Expose
	private String created_by;
	
	//@Expose
	//private String[] annotations;
	
	
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
	public void execute() {

		// TODO Auto-generated method stub

	}

}
/*
{
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
*/