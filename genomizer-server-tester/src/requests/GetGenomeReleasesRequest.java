package requests;

/**
 * Request for getting the genome releases.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class GetGenomeReleasesRequest extends Request {

    public GetGenomeReleasesRequest() {
        super("getGenomeReleases", "/genomeRelease", "GET");

    }

}
