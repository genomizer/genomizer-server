package command;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;
import database.constants.MaxSize;

import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * command used to create a user.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class CreateUserCommand extends Command {
	/** All attributes in this class are serialized with
	 * a JSON string. This is done in CommandHandler.
	 */

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
	 * Used to validate the CreateUserCommand.
	 */
	@Override
	public boolean validate() {

		if(username == null || password == null || privileges == null) {
			return false;
		}
		if(username.length() < 1 || username.length() > MaxSize.USERNAME) {
			return false;
		}
		if(password.length() < 1 || password.length() > MaxSize.PASSWORD) {
			return false;
		}
		if(privileges.length() < 1 || privileges.length() > MaxSize.ROLE) {
			return false;
		}
		if(username.indexOf('/') != -1) {
			return false;
		}

		return true;
	}

	/**
	 * Used to execute the actual creation of the user.
	 */
	@Override
	public Response execute() {



		DatabaseAccessor db = null;
		try {
			db = initDB();
		} catch (SQLException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when initiating databaseAccessor. " + e.getMessage());
		} catch (IOException e)  {
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		}
		try {

			/* For insertion of new user into db

			// get a new salt
			String salt = PasswordHash.getNewSalt();
			// get hash using salt and password
			String hash = PasswordHash.hashString(password+salt);
			// insert into DB, requires new table from DB group
			db.addUser(username, salt, hash, privileges, name, email);
			*/


			db.addUser(username, password, privileges, name, email);
		} catch (SQLException | IOException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when adding user to database, user probably already exists. " + e.getMessage());
		}
		return new MinimalResponse(StatusCode.CREATED);

	}

}
