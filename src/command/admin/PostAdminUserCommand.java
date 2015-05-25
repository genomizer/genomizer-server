package command.admin;

import java.io.IOException;
import java.sql.SQLException;

import authentication.BCrypt;
import com.google.gson.annotations.Expose;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;

import database.constants.MaxLength;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;
import server.Debug;

/**
 * Command used to create a user.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PostAdminUserCommand extends Command {
	@Expose
	private String username = null;
	@Expose
	private String password = null;
	@Expose
	private String privileges = null;
	@Expose
	private String name = null;
	@Expose
	private String email = null;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(username, MaxLength.USERNAME, "User");
		validateName(password, MaxLength.PASSWORD, "Password");
		validateName(privileges, MaxLength.ROLE, "Privileges");
		validateExists(name, MaxLength.FULLNAME, "Name");
		validateExists(email, MaxLength.EMAIL, "Email");
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		Response response;

		try {
			db = initDB();
			String hash = BCrypt.hashpw(password,BCrypt.gensalt());
			db.addUser(username, hash, "SALT", privileges, name, email);
			response = new MinimalResponse(HttpStatusCode.OK);
		} catch (SQLException e) {
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Creation of user '" + username + "' unsuccessful, user " +
							"already exists.");
			Debug.log("Reason: " + e.getMessage());
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Creation of user '" + username + "' unsuccessful due to " +
							"temporary database problems.");
		} finally {
			if (db != null)
				db.close();
		}

		return response;
	}
}
