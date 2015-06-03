package requests;

/**
 * Request for logging out.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class LogoutRequest extends Request {

    /**
     * A constructor creating the request.
     *
     */
    public LogoutRequest() {
        super("logout", "/login", "DELETE");
    }

}
