package command.connection;
import command.Command;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.LoginResponse;
import response.Response;
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
public class PostLoginCommand extends Command {
	@Expose
	private String username = null;

	@Expose
	private String password = null;


	@Override
	public void validate() throws ValidateException {
		validateName(username, MaxLength.USERNAME, "Username/Password");
		validateName(password, MaxLength.PASSWORD, "Username/Password");
	}

	@Override
	public Response execute() {
		DatabaseAccessor db;
		String dbHash;
		try {
			db = initDB();
			dbHash = db.getPasswordHash(username);
		} catch (SQLException | IOException e) {
			Debug.log("LOGIN WAS UNSUCCESSFUL FOR: " + username + ". REASON: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"could not verify username and password");
		}

		if(dbHash == null || dbHash.isEmpty()){
			return new ErrorResponse(HttpStatusCode.UNAUTHORIZED, "Invalid username");
		}

		LoginAttempt login = Authenticate.login(username, password, dbHash);

		if(!login.wasSuccessful()) {
			Debug.log("LOGIN WAS UNSUCCESSFUL FOR: " + username + ". REASON: " +
					login.getErrorMessage());
			return new ErrorResponse(HttpStatusCode.UNAUTHORIZED,
					login.getErrorMessage());
		}

		Debug.log("LOGIN WAS SUCCESSFUL FOR: "+ username + ". GAVE UUID: " +
				Authenticate.getID(username));
		return new LoginResponse(200, login.getUUID());
	}
}
