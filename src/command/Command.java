package command;

import java.sql.SQLException;

import response.Response;
import server.DatabaseSettings;

import database.DatabaseAccessor;

/**
 * Class used to represent a command in the software.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public abstract class Command {

	//Used to get the header for the respons.
	protected String header;

	//Used to validate the class object.
	public abstract boolean validate();

	//Method used to run command.
	public abstract Response execute();

	//Method used to get the header.
	public String getHeader() {

		return header;

	}

	//Method used to set header.
	public void setHeader(String header) {
		this.header = header;
	}

	public DatabaseAccessor initDB() throws SQLException {
		DatabaseAccessor db = null;
		db = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
		return db;
	}


}
