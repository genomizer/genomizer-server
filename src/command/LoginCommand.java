package command;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.LoginResponse;
import response.Response;
import response.StatusCode;
import server.Debug;
import authentication.Authenticate;
import authentication.LoginAttempt;

import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.sql.SQLException;

/**
 * This class is used to handle user login.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class LoginCommand extends Command {
	@Expose
	private String username = null;

	@Expose
	private String password = null;

	@Override
	public void setFields(String uri, String uuid, UserType userType) {
		this.userType = userType;

		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
	}

	@Override
	public void validate() throws ValidateException {
		validateName(username, MaxLength.USERNAME, "Username/Password");
		validateName(password, MaxLength.PASSWORD, "Username/Password");
	}

	@Override
	public Response execute() {
		DatabaseAccessor db;
		String dbHash;
		String dbSalt;

		try {
			db = initDB();
			dbHash = db.getPasswordHash(username);
		} catch (SQLException | IOException e) {
			Debug.log("LOGIN WAS UNSUCCESSFUL FOR: " + username + ". REASON: " +
					e.getMessage());
			return new ErrorResponse(StatusCode.BAD_REQUEST,
					"LOGIN WAS UNSUCCESSFUL FOR: " + username + ". REASON: " +
							e.getMessage());
		}

		if(dbHash == null || dbHash.isEmpty()){
			return new ErrorResponse(StatusCode.UNAUTHORIZED, "Incorrect user name");
		}

		LoginAttempt login = Authenticate.login(username, password, dbHash);

		if(login.wasSuccessful()) {
			Debug.log("LOGIN WAS SUCCESSFUL FOR: "+ username + ". GAVE UUID: " +
					Authenticate.getID(username));
			return new LoginResponse(200, login.getUUID());
		}

		Debug.log("LOGIN WAS UNSUCCESSFUL FOR: " + username + ". REASON: " +
				login.getErrorMessage());
		return new ErrorResponse(StatusCode.UNAUTHORIZED,
				login.getErrorMessage());
	}
}
