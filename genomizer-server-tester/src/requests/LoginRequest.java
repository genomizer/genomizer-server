package requests;

/**
 * This class represents a "Login to the server" request in an application for
 * genome researchers. This request logs the user in to the server.
 *
 * @author
 *
 */
public class LoginRequest extends Request {
    /**
     * Attributes needed to create the request.
     *
     */
    public String username;
    public String password;

    /**
     * A constructor creating the request.
     *
     * @param username
     *            String representing the user to be logged in.
     * @param password
     *            String with the password of the user.
     */
    public LoginRequest(String username, String password) {
        super("login", "/login", "POST");
        this.username = username;
        this.password = password;
    }

}
