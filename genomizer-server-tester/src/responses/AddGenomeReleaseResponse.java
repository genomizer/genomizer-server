package responses;

/**
 * Response when adding new genome releases.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class AddGenomeReleaseResponse extends Response {

    public String URLupload;

    public AddGenomeReleaseResponse(String responseName, String URLupload) {
        super(responseName);
        this.URLupload = URLupload;

    }

}
