package requests;

/**
 * This class represents a "Add a file to an experiment" request in an
 * application for genome researchers. This request adds a file to an experiment
 * in the database of the application.
 *
 * @author worfox
 * @version 1.0
 * 25 April 2014
 */

public class RemoveFileFromExperimentRequest extends Request {

    public RemoveFileFromExperimentRequest(String fileID) {
        super("removefile", "/file/" + fileID, "DELETE");

    }

}
