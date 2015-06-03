package requests;

/**
 * Request for getting the user list.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class GetUserNamesRequest extends Request {

    /**
     * Request to send.
     */
    public GetUserNamesRequest() {
        super("getuserlist", "/admin/userlist", "GET");
    }
}