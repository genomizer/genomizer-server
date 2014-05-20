package command;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

public class UpdateUserPrivilegesCommand extends Command {

	private String username = null;

	@Expose
	public String new_privileges = null;

	@Override
	public boolean validate() {

		if(new_privileges == null) {
			return false;
		}

		return true;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public Response execute() {

		DatabaseAccessor db = null;

		System.out.println("UPDATING PRIVILEGES OF " + username + " TO " + new_privileges);

		try {
			db = initDB();

		} catch (SQLException e) {
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, "SQLError when initializing database accessor. " + e.getMessage());
		} catch (IOException e) {
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, "IOError when initializing database accessor. " + e.getMessage());
		}

		try {
			if(db.setRole(username, new_privileges) <= 0) {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "No user were affected by change. Username does probably not exist.");
			}
		} catch (SQLException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when setting new privileges, user probably does not exist. " + e.getMessage());
		}

		return new MinimalResponse(200);
	}

}
