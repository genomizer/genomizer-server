package requests;

/**
 * Request for retrieving experiments.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class RetrieveExperimentRequest extends Request {

    /**
     * Constructor creating the request.
     *
     * @param experimentID
     *            String representing the experiment id.
     */
    public RetrieveExperimentRequest(String experimentID) {
        super("retrieveexperiment", "/experiment/" + experimentID, "GET");
    }
}
