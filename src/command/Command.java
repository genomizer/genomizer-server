package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;
import response.Response;
import server.ServerSettings;
import database.DatabaseAccessor;

/**
 * This class contains common methods and attributes that are needed
 * to create a "command". A command should extend this class and represents
 * a task that the server needs to execute.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public abstract class Command {
	/* When creating a new command, these steps needs to be followed:
	 * 1. 	Create the command and extend this class.
	 * 2. 	Create the method that builds the actual command in CommandFactory.
	 * 3. 	Add a check for the command to be able to build it in CommandHandler.
	 * 4. 	In the Doorman class, make sure that the new command is identified if the
	 * 		server receives it so that it can be passed to the CommandHandler.
	 */

	//These are valid characters that are used with the validation method.
	final protected String validCharacters = "^, A-Z, a-z, 0-9, space and _";

	//This is used to store a RESTful-header.
	protected String header;

	/* This method is used to validates the object and its information.
	 * The validate method should be called before the command is executed and
	 * should be unique to each child.
	 */
	public abstract boolean validate() throws ValidateException;

	//Method used to execute the actual command.
	public abstract Response execute();

	/**
	 * Method used to get the RESTful-header.
	 *
	 * @return the header that is set.
	 */
	public String getHeader() {

		return header;

	}

	/**
	 * Method used to set the RESTful-header.
	 *
	 * @param the header as a string.
	 */
	public void setHeader(String header) {

		this.header = header;

	}

	/**
	 * Method used to connect to the database.
	 *
	 * @return a database accessor object.
	 * @throws SQLException
	 * @throws IOException
	 */
	public DatabaseAccessor initDB() throws SQLException, IOException {

		DatabaseAccessor db = null;
		db = new DatabaseAccessor(ServerSettings.databaseUsername, ServerSettings.databasePassword, ServerSettings.databaseHost, ServerSettings.databaseName);

		return db;

	}

	/**
	 * This method is used to validate a string and check if all
	 * it's characters are valid.
	 *
	 * @param a String to validate.
	 * @return boolean depending on validation result.
	 */
	public boolean hasOnlyValidCharacters(String s) {

		Pattern p = Pattern.compile("[^A-Za-z0-9 _]");

		return !p.matcher(s).find();

	}

}
