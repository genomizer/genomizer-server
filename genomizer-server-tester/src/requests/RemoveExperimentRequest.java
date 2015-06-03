package requests;

/**
 * Request for removing experiments.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class RemoveExperimentRequest extends Request {

    public RemoveExperimentRequest(String experimentID) {
        super("removeexperiment", "/experiment/" + experimentID, "DELETE");
    }
}
