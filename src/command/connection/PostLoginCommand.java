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
 * Command used to log in a user.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PostLoginCommand extends Command {
	@Expose
	private String username = null;
	@Expose
	private String password = null;

	private final static String errMsg = "Login attempt unsuccessful, incorrect Username/Password.";

	@Override
	public int getExpectedNumberOfURIFields() {
		return 1;
	}

	@Override
	public void validate() throws ValidateException {
		validateUserAndPassword(username, MaxLength.USERNAME);
		validateUserAndPassword(password, MaxLength.PASSWORD);
	}

	public void validateUserAndPassword(String string, int maxLength) throws
			ValidateException {
		if (string == null) {
			badRequest();
		}
		if (string.equals("null")) {
			badRequest();
		}
		if(string.length() > maxLength || string.length() < 1) {
			badRequest();
		}
		if(hasInvalidCharacters(string)) {
			badRequest();
		}
	}

	private void badRequest() throws ValidateException {
		throw new ValidateException(HttpStatusCode.BAD_REQUEST, errMsg);
	}

	@Override
	public Response execute() {
		try (DatabaseAccessor db = initDB()) {
			String dbHash = db.getPasswordHash(username);
			if (dbHash != null) {
				LoginAttempt login = Authenticate.login(uuid, username,
						password, dbHash);
				if (login.wasSuccessful()) {
					return new LoginResponse(login.getUUID(),
							db.getRole(username).name());
				}
				else {
					return unauthorized();
				}
			} else {
				return unauthorized();
			}
		}
		catch (IOException | SQLException e) {
			Debug.log("Reason: " + e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Login attempt unsuccessful due to temporary database " +
							"problems.");
		}
	}

	private Response unauthorized() {
		return new ErrorResponse(HttpStatusCode.UNAUTHORIZED, errMsg);
	}
}
