package requests;

/**
 * Request for removing releases.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class RemoveGenomeReleaseRequest extends Request {

    public RemoveGenomeReleaseRequest(String species, String version) {
        super("deleteGenomeRelease",
                "/genomeRelease/" + species + "/" + version, "DELETE");



    }

}
