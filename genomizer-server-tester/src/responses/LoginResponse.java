package responses;

import com.google.gson.annotations.Expose;

/**
 * Response for the log in attempts.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class LoginResponse extends Response {

    @Expose
    public String token;

    @Expose
    public String role;


    public LoginResponse(String token, String role) {
        super("login");
        this.token = token;
        this.role = role;
    }
}
