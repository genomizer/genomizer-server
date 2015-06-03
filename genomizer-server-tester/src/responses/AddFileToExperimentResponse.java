package responses;

import com.google.gson.annotations.Expose;

/**
 * Response when an experiment is added.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class AddFileToExperimentResponse extends Response {
    @Expose
    public String URLupload;

    public AddFileToExperimentResponse(String responseName, String wwwTunnelPath) {
        super(responseName);
        this.URLupload = wwwTunnelPath;
    }

}
