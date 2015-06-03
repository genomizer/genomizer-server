package requests;

/**
 * Request for the search command.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class SearchRequest extends Request {

    public SearchRequest(String pubmedString) {
        super("search", ("/search/?annotations=" + pubmedString), "GET");
    }

}
