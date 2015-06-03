package requests;

/**
 * Request for getting the genome specie releases.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class GetGenomeSpecieReleasesRequest extends Request {

    public GetGenomeSpecieReleasesRequest(String specie) {
        super("getGenomeSpecieReleases", "/genomeRelease/" + specie.trim(), "GET");
    }
}
