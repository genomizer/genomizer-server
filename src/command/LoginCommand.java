package command;
import database.DatabaseAccessor;
import database.constants.MaxSize;
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
	public void validate() throws ValidateException {
		validateString(username, MaxSize.USERNAME, "Username/Password");
		validateString(password, MaxSize.PASSWORD, "Username/Password");
	}

	@Override
	public Response execute() {

		DatabaseAccessor db = null;

		String dbHash = null;
		String dbSalt = null;

		try {
			db = initDB();
		} catch (SQLException | IOException e) {
			Debug.log("LOGIN WAS UNSUCCESSFUL FOR: " + username + ". REASON: " +
					e.getMessage());
			return new ErrorResponse(StatusCode.BAD_REQUEST,
					"LOGIN WAS UNSUCCESSFUL FOR: " + username + ". REASON: " + e.getMessage());
		}

		try {
			dbSalt = db.getPasswordSalt(username);
			dbHash = db.getPasswordHash(username);
		}catch (SQLException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Database error " + e.getMessage());
		}

		if(dbSalt == null || dbSalt.isEmpty() || dbHash == null || dbHash.isEmpty()){
			return new ErrorResponse(StatusCode.UNAUTHORIZED, "Incorrect user name");
		}

		LoginAttempt login = Authenticate.login(username, password, dbHash, dbSalt);

		LoginAttempt login = Authenticate.login(username, password);
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
