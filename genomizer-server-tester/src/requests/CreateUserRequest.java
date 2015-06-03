package requests;

/**
 * Request for creating a new user.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class CreateUserRequest extends Request{

    public String username;
    public String password;
    public String privileges;
    public String name;
    public String email;

    /**
     * The information to send to create a new user.
     * @param username
     * @param password
     * @param privileges
     * @param name
     * @param email
     */
    public CreateUserRequest(String username,
                             String password, String privileges, String name, String email) {
        super("createuser", "/admin/user", "POST");
        this.username = username;
        this.password = password;
        this.privileges = privileges;
        this.name = name;
        this.email = email;
    }

}
