package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

import response.Response;
import server.ServerSettings;

import database.DatabaseAccessor;

/**
 * Class used to represent a command in the software.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public abstract class Command {


	final protected String validCharacters = "^, A-Z, a-z, 0-9, space and _";
	//Used to get the header for the response.
	protected String header;

	//Method used to validate the class object.
	public abstract boolean validate() throws ValidateException;

	//Method used to run command.
	public abstract Response execute();

	/**
	 * Method used to get the header.
	 *
	 * @return the header that is set.
	 */
	public String getHeader() {

		return header;

	}

	/**
	 * Method used to set the restful header.
	 *
	 * @param the header as a string.
	 */
	public void setHeader(String header) {

		this.header = header;

	}

	/**
	 * Method used to connect to the database.
	 *
	 * @return a database accessor class.
	 * @throws SQLException
	 * @throws IOException
	 */
	public DatabaseAccessor initDB() throws SQLException, IOException {

		DatabaseAccessor db = null;
		db = new DatabaseAccessor(ServerSettings.databaseUsername, ServerSettings.databasePassword, ServerSettings.databaseHost, ServerSettings.databaseName);

		return db;

	}

	public boolean hasOnlyValidCharacters(String s){
		Pattern p = Pattern.compile("[^A-Za-z0-9 _]");
		return !p.matcher(s).find();
	}


}
