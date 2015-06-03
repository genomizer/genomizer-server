package requests;

/**
 * Request for changing the user password.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class ChangePasswordRequest extends Request {

    public String oldPassword;
    public String newPassword;
    public String email;
    public String name;

    public ChangePasswordRequest(String oldPassword, String newPassword,
            String name, String email) {
        super("updateusersettings", "/user", "PUT");
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.name = name;
        this.email = email;
    }
}
