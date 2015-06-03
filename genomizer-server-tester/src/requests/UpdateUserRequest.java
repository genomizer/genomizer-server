package requests;

/**
 * Request for the update user command.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class UpdateUserRequest extends Request{

    public String username;
    public String password;
    public String privileges;
    public String name;
    public String email;

    /**
     * Defines the userand information to update.
     * @param username
     * @param password
     * @param privileges
     * @param name
     * @param email
     */
    public UpdateUserRequest(String username, String password,
                             String privileges, String name, String email) {
        super("updateuser", "/admin/user", "PUT");
        this.username = username;
        this.password = password;
        this.privileges = privileges;
        this.name = name;
        this.email = email;
    }

}
