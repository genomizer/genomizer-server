package command;

import java.io.IOException;
import java.sql.SQLException;

import authentication.BCrypt;
import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;

import database.constants.MaxLength;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * command used to create a user.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class CreateUserCommand extends Command {
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

	/**
	 * Used to make sure the strings of the command are correct
	 * @throws ValidateException
	 */
	@Override
	public void validate() throws ValidateException {

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
		} catch (SQLException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when " +
					"initiating databaseAccessor. " + e.getMessage());
		} catch (IOException e)  {
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		}
		try {
			String hash = BCrypt.hashpw(password,BCrypt.gensalt());
			db.addUser(username, hash, "SALT",privileges, name, email);

		} catch (SQLException | IOException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when " +
					"adding user to database, user probably already exists. " +
					e.getMessage());
		}
		return new MinimalResponse(StatusCode.CREATED);

	}

}
