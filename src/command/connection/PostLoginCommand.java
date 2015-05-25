package command.connection;

import authentication.Authenticate;
import authentication.LoginAttempt;
import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.LoginResponse;
import response.Response;
import server.Debug;

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
	public int getExpectedNumberOfURIFields() {
		return 1;
	}

	@Override
	public void validate() throws ValidateException {
		validateUserAndPassword(username, MaxLength.USERNAME, "Username/Password");
		validateUserAndPassword(password, MaxLength.PASSWORD, "Username/Password");
	}
	public void validateUserAndPassword(String string, int maxLength, String field)
			throws ValidateException {
		if(string == null) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Specify " +
					"an " + field.toLowerCase() + ".");
		}
		if(string.equals("null")){
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Invalid "
					+ field.toLowerCase() + ".");
		}
		if(string.length() > maxLength || string.length() < 1) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, field +
					" has to be between 1 and " + maxLength +
					" characters long.");
		}
		if(hasInvalidCharacters(string)) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Invalid" +
					" characters in " + field.toLowerCase() +
					". Valid characters are: " + validCharacters);
		}
	}
	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		String dbHash;
		try {
			db = initDB();
			dbHash = db.getPasswordHash(username);
		} catch (SQLException | IOException e) {
			Debug.log("LOGIN WAS UNSUCCESSFUL FOR: " + username + ". REASON: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Login was unsuccessful for user: " + username +
							". The reason is temporary problems with database.");
		}finally {
			if (db != null) {
				db.close();
			}
		}

		if(dbHash == null || dbHash.isEmpty()){
			return new ErrorResponse(HttpStatusCode.UNAUTHORIZED, "Login failed, invalid username");
		}

		LoginAttempt login = Authenticate.login(username, password, dbHash);

		if(!login.wasSuccessful()) {
			Debug.log("LOGIN WAS UNSUCCESSFUL FOR: " + username + ". REASON: " +
					login.getErrorMessage());
			return new ErrorResponse(HttpStatusCode.UNAUTHORIZED,
					"Login failed, incorrect password");
		}

		Debug.log("LOGIN WAS SUCCESSFUL FOR: "+ username + ". GAVE UUID: " +
				Authenticate.getID(username));
		return new LoginResponse(200, login.getUUID());
	}
}
