package command;

import database.DatabaseAccessor;
import database.subClasses.UserMethods.UserType;
import response.HttpStatusCode;
import response.Response;
import server.ServerSettings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * This class contains common methods and attributes that are needed
 * to create a "command". A command should extend this class and represents
 * a task that the server needs to execute.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public abstract class Command {
	/*	Steps to follow when creating a new command:
		1.	Create a command that extends this class.

		2.	Make sure the needed methods are implemented in the newly created
			class (take a close look at getExpectedNumberOfURIFields,
			setFields, validate and execute).

		3.	Place the the newly created command class in the hash map contained
			in the CommandClasses class.

		4.	Create a context that matches the request header URI in the
			Doorman class.
	 */

	/*These are valid characters that are used with the validation method.*/
	final static protected String VALID_CHARACTERS =
			"A-Z, a-z, 0-9, -, _ and .";

	/*Keeps track of the user rights level for the request sender.*/
	protected UserType userType = UserType.UNKNOWN;

	/*Contains the user's uuid.*/
	protected String uuid = null;

	/**
	 * Returns the number of expected fields in the URI of the request that
	 * corresponds to this command. For example, number of URI fields in a
	 * "remove annotation value" request is 4 as the URI has 4 fields
	 * (/annotation/value/<field-name>/<value-name>).
	 * @return the expected number of fields in the URI of the request that
	 * corresponds to this command.
	 */
	public abstract int getExpectedNumberOfURIFields();

	/**
	 * Used to set the required fields of the command. This method should
	 * always be called prior to using the execute method.
	 * @param uri the request URI.
	 * @param query the query part of the request.
	 * @param uuid the UUID of the user who made the request.
	 * @param userType the user type of the user who made the request.
	 */
	public void setFields(String uri, HashMap<String, String> query,
						  String uuid, UserType userType){
		this.uuid = uuid;
		this.userType = userType;
	}

	/**
	 * Used to validate the object and its information. The validate method
	 * should be called before the command is executed and should be unique
	 * to each subclass. If the object is not valid a ValidateException is
	 * thrown.
	 * @throws ValidateException containing information describing why the
	 * command could not be validated.
	 */
	public abstract void validate() throws ValidateException;

	/**
	 * Executes the command and returns the appropriate response.
	 * @return an appropriate Response depending on the command.
	 */
	public abstract Response execute();

	/**
	 * Method used to connect to the database.
	 * @return a database accessor object.
	 * @throws SQLException if there are problems with the database not caused
	 * by the user.
	 * @throws IOException if there is some other fault with the database.
	 */
	public static DatabaseAccessor initDB() throws SQLException, IOException {
		return new DatabaseAccessor(ServerSettings.databaseUsername,
				ServerSettings.databasePassword, ServerSettings.databaseHost,
				ServerSettings.databaseName);
	}

	/**
	 * This method is used to validate a string and check if all the characters
	 * are valid.
	 * @param string a string to validate.
	 * @return true if the string has invalid characters, else false.
	 */
	public static boolean hasInvalidCharacters(String string) {
		Pattern p = Pattern.compile("[^A-Za-z0-9-_.]");
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
	public static void validateName(String string, int maxLength, String field)
			throws ValidateException {
		if(string == null) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Specify " +
					"an " + field.toLowerCase() + ".");
		}
		if(string.equals("null")){
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Invalid "
					+ field.toLowerCase() + ".");
		}
		if(string.length() > maxLength || string.length() < 1) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, field +
					" has to be between 1 and " + maxLength +
					" characters long.");
		}
		if(hasInvalidCharacters(string)) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Invalid" +
					" characters in " + field.toLowerCase() +
					". Valid characters are: " + VALID_CHARACTERS);
		}
	}

	/**
	 * Validates that a string is a valid MD5 checksum.
	 * @param checkSumMD5 the field to be validated.
	 * @throws ValidateException if the field does not conform.
	 */
	public static void validateMD5(String checkSumMD5)
			throws ValidateException {
		if (checkSumMD5 != null) {
			if (checkSumMD5.length() != 32)
				throw new ValidateException(HttpStatusCode.BAD_REQUEST,
						"MD5 checksum has incorrect length (should be 32)!");
			if (!checkSumMD5.matches("[0-9a-fA-F]+"))
				throw new ValidateException(HttpStatusCode.BAD_REQUEST,
						"Invalid characters in MD5 "
								+ "checksum string (should be '[0-9a-fA-F]')!");
		}
	}

	/**
	 * Validates a field by throwing a ValidateException if it doesn't conform
	 * to specifications.
	 * @param string the field to be validated.
	 * @param maxLength the maximum length of the field.
	 * @param field the name of the field in question.
	 * @throws ValidateException if the field does not conform.
	 */
	public static void validateExists(String string, int maxLength,
									  String field) throws ValidateException {
		if(string == null) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Specify " +
					"an " + field.toLowerCase() + ".");
		}
		if(string.equals("null")){
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Invalid "
					+ field.toLowerCase() + ".");
		}
		if(string.length() > maxLength || string.length() < 1) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, field +
					" has to be between 1 and " + maxLength +
					" characters long.");
		}
	}


	/**
	 * Methods which verifies if the user has the requested user rights.
	 * @param required The user rights level required
	 * @throws ValidateException If the user rights level wasn't high enough
	 */
	public void hasRights(UserType required) throws ValidateException {

		if (userType == UserType.UNKNOWN)
			throw new ValidateException(HttpStatusCode.FORBIDDEN,
					"You don't have permission.");

		if (required == userType)
			return;

		else if (required == UserType.ADMIN)
			throw new ValidateException(HttpStatusCode.FORBIDDEN,
					"You don't have permission.");

		else if (required == UserType.USER && userType != UserType.ADMIN)
			throw new ValidateException(HttpStatusCode.FORBIDDEN,
					"You don't have permission.");

		else if (required == UserType.UNKNOWN && !(userType == UserType.ADMIN))
			throw new ValidateException(HttpStatusCode.FORBIDDEN,
					"You don't have permission.");
	}

}
