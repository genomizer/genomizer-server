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


	@Override
	public int getExpectedNumberOfURIFields() {
		return 1;
	}

	/**
	 * Validate username and password to check neither was null or an empty string.
	 * @throws ValidateException
	 */
	@Override
	public void validate() throws ValidateException {
		validateUserAndPassword(username, MaxLength.USERNAME,
				"Username/Password");
		validateUserAndPassword(password, MaxLength.PASSWORD,
				"Username/Password");
	}

	public void validateUserAndPassword(String string, int maxLength,
										String field) throws ValidateException {
		if (string == null) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Invalid "
					+ field.toLowerCase() + ".");
		}

		if (string.equals("null")) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Invalid "
					+ field.toLowerCase() + ".");
		}
		if(string.length() > maxLength || string.length() < 1) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Invalid "
					+ field.toLowerCase() + ".");
		}
		if(hasInvalidCharacters(string)) {
				throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Invalid "
						+ field.toLowerCase() + ".");
		}
	}

	/**
	 * Try the login command and return a response with the result.
	 * @return
	 */
	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		Response response;

		try {
			db = initDB();
			String dbHash;
			if ((dbHash = db.getPasswordHash(username)) != null) {
				LoginAttempt login = Authenticate.login(uuid, username, password,
						dbHash);
				if (login.wasSuccessful())
					response = new LoginResponse(login.getUUID(),
							db.getRole(username).name());
				else
					response = new ErrorResponse(HttpStatusCode.UNAUTHORIZED,
							"Login attempt unsuccessful, incorrect Username/Password.");
			} else {
				response = new ErrorResponse(HttpStatusCode.UNAUTHORIZED,
						"Login attempt unsuccessful, incorrect Username/Password");
			}
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Login attempt unsuccessful. " + e.getMessage());
		} catch (SQLException e) {
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Login attempt unsuccessful due to temporary database problems.");
			Debug.log("Reason: " + e.getMessage());
		} finally {
			if (db != null)
				db.close();
		}

		return response;
	}
}
