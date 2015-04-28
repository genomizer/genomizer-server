package command;

import java.io.IOException;
import java.sql.SQLException;

import authentication.PasswordHash;
import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;

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

	@Override
	public void setFields(String uri, String uuid) {

		/*No fields from the uri is needed, neither is the UUID. Dummy
		implementation*/
	}

	@Override
	public void validate() {
		//TODO Change to exceptions.

		/*if(username == null || password == null || privileges == null) {
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
		return username.indexOf('/') == -1;*/

	}

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



			// get a new salt
			String salt = PasswordHash.getNewSalt();
			// get hash using salt and password
			String hash = PasswordHash.hashString(password+salt);
			// insert into DB, requires new table from DB group
			db.addUser(username, salt, hash, privileges, name, email);


			//db.addUser(username, password, privileges, name, email);
		} catch (SQLException | IOException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when " +
					"adding user to database, user probably already exists. " +
					e.getMessage());
		}
		return new MinimalResponse(StatusCode.CREATED);

	}

}
