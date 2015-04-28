package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

import response.Response;
import response.StatusCode;
import server.ServerSettings;
import database.DatabaseAccessor;
import database.subClasses.UserMethods.UserType;

/**
 * This class contains common methods and attributes that are needed
 * to create a "command". A command should extend this class and represents
 * a task that the server needs to execute.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public abstract class Command {
	/* When creating a new command, these steps needs to be followed:
	 * 1. 	Create the command and extend this class.
	 * 2. 	Create the method that builds the actual command in CommandFactory.
	 * 3. 	Add a check for the command to be able to build it in
	 * 		CommandHandler.
	 * 4. 	In the Doorman class, make sure that the new command is identified
	 * 		if the
	 * 		server receives it so that it can be passed to the CommandHandler.
	 */

	/*These are valid characters that are used with the validation method.*/
	final protected String validCharacters = "^, A-Z, a-z, 0-9, space and _";

	/*This is used to store a RESTful-header.*/
	protected String header;

	/*Contains the user rights level of the sender*/
	protected UserType userType;

	/**
	 * Used to validate the object and its information. The validate method
	 * should be called before the command is executed and should be unique
	 * to each subclass. If the object is not valid a ValidateException is
	 * thrown.
	 * @throws ValidateException containing information describing why the
	 * Command could not be validated.
	 */
	public abstract void validate() throws ValidateException;

	/**
	 * Executes the command and returns the appropriate response.
	 * @return an appropriate Response depending on the command.
	 */
	public abstract Response execute();

	/**
	 * Method used to get the RESTful-header.
	 * @return the header that is set.
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * Method used to set the RESTful-header.
	 * @param header the header as a string.
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * Method used to connect to the database.
	 * @return a database accessor object.
	 * @throws SQLException
	 * @throws IOException
	 */
	public DatabaseAccessor initDB() throws SQLException, IOException {
		DatabaseAccessor db;
		db = new DatabaseAccessor(ServerSettings.databaseUsername,
				ServerSettings.databasePassword, ServerSettings.databaseHost,
				ServerSettings.databaseName);

		return db;
	}

	/**
	 * This method is used to validate a string and check if all
	 * it's characters are valid.
	 * @param string a string to validate.
	 * @return boolean depending on validation result.
	 */
	public boolean hasInvalidCharacters(String string) {
		Pattern p = Pattern.compile("[^A-Za-z0-9 _]");
		return p.matcher(string).find();
	}

	/**
	 * Validates a field by throwing a ValidateException if it doesn't conform
	 * to specifications.
	 * @param string the field to be validated.
	 * @param maxLength the maximum length of the field.
	 * @param field the name of the field in question.
	 * @throws ValidateException if the field does not conform.
	 */
	public void validateString(String string, int maxLength, String field)
			throws ValidateException {
		if(string == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify " +
					"an " + field.toLowerCase() + ".");
		}
		if(string.equals("null")){
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid "
					+ field.toLowerCase() + ".");
		}
		if(string.length() > maxLength || string.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, field + ": " +
					string + " has to be between 1 and " + maxLength +
					" characters long.");
		}
		if(hasInvalidCharacters(string)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid" +
					" characters in " + field.toLowerCase() +
					". Valid characters are: " + validCharacters);
		}
	}


	/**
	 * Methods which verifies if the user has the requested user rights.
	 * @param required The user rights level required
	 * @throws ValidateException If the user rights level wasn't high enough
	 */
	public void hasRights(UserType required) throws ValidateException {

		if (userType == UserType.UNKNOWN)
			throw new ValidateException(StatusCode.FORBIDDEN, "You don't have permission.");

		if (required == userType)
			return;

		else if (required == UserType.ADMIN)
			throw new ValidateException(StatusCode.FORBIDDEN, "You don't have permission.");

		else if (required == UserType.USER && userType != UserType.ADMIN)
			throw new ValidateException(StatusCode.FORBIDDEN, "You don't have permission.");

		else if (required == UserType.UNKNOWN && !(userType == UserType.ADMIN))
			throw new ValidateException(StatusCode.FORBIDDEN, "You don't have permission.");
	}

}
