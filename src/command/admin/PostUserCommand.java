package command.admin;

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

import java.io.IOException;
import java.sql.SQLException;

/**
 * command used to create a user.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PostUserCommand extends Command {
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
	/**
	 * Used to make sure the strings of the command are correct
	 * @throws command.ValidateException
	 */
	@Override
	public void validate() throws ValidateException {

		hasRights(UserRights.getRights(this.getClass()));

		validateName(username, MaxLength.USERNAME, "User");
		validateName(password, MaxLength.PASSWORD, "Password");
		validateName(privileges, MaxLength.ROLE, "Privileges");
		validateExists(name, MaxLength.FULLNAME, "Name");
		validateExists(email, MaxLength.EMAIL, "Email");
	}

	/**
	 * Runs the command. The user gets added to the database.
	 * @return a MinimalResponse or ErrorResponse
	 */
	@Override
	public Response execute() {
		DatabaseAccessor db;
		try {
			db = initDB();
		} catch (SQLException | IOException e) {
			Debug.log("Creation of user: " + username + " didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Creation of user: " + username +
					" didn't work because of temporary problems with database.");
		}
		try {
			String hash = BCrypt.hashpw(password,BCrypt.gensalt());
			db.addUser(username, hash, "SALT",privileges, name, email);

		} catch (SQLException | IOException e) {
			Debug.log("Creation of user: " + username + " didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Creation of user: " + username +
					" didn't work because of temporary problems with database.");
		}finally {
			db.close();
		}
		return new MinimalResponse(HttpStatusCode.OK);

	}

}
