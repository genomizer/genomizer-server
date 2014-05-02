package command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Used to create and return different commands that
 * will be executed later by calling the execute() method.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class CommandFactory {

	private final GsonBuilder builder;
	private final Gson gson;

	/**
	 * Constructor that creates the gson builder.
	 */
	public CommandFactory() {

		//Create the builder.
	    builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    gson = builder.create();

	}

	/**
	 * Used to create the command needed for login.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createLoginCommand(String json, String[] restful) {

		//Create command with json.
		final Command loginCmd = gson.fromJson(json, LoginCommand.class);

		//Set headers
		loginCmd.setHeader(restful);

		return loginCmd;

	}

	/**
	 * Used to create logout command.
	 * @param restful
	 * @return
	 */
	public Command createLogoutCommand(String[] restful) {

		//Create command with json.
		final Command logoutCmd = new LogoutCommand();

		//Set headers
		logoutCmd.setHeader(restful);

		return logoutCmd;
	}

	/**
	 * Used to create the command needed for retrieving experiments.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createRetrieveExperimentCommand(String json, String[] restful) {

		return null;

	}

	/**
	 * Used to create the command needed for adding experiments.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createAddExperimentCommand(String json, String[] restful) {

		return null;

	}

	/**
	 * Used to create the command needed for updateing experiments.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createUpdateExperimentCommand(String json, String[] restful) {

		return null;

	}

	/**
	 * Used to create the command needed for removeing experiments.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createRemoveExperimentCommand(String json, String[] restful) {

		return null;

	}

	/**
	 * Used to create the command needed for retrieving experiment files.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createGetFileFromExperimentCommand(String json, String[] restful) {

		return new GetFileFromExperimentCommand(restful);
	}

	/**
	 * Used to create the command needed for adding files to experiments.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createAddFileToExperimentCommand(String json, String[] restful) {

		return null;

	}

	/**
	 * Used to create the command needed to update files in experiments.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createUpdateFileInExperimentCommand(String json, String[] restful) {

		return null;

	}

	/**
	 * Used to create the command needed to remove files from experiments.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createDeleteFileFromExperimentCommand(String json, String[] restful) {

		return null;

	}

	/**
	 * Used to create the command needed for searching experiments.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 * @throws SQLException
	 */
	public Command createSearchForExperimentCommand(String json, String[] restful) {

		String queryParameters = restful.substring(restful.lastIndexOf('/')+1);
		String[] params = queryParameters.split("=");
		if (params[0].equals("annotations")) {
			return new SearchForExperimentsCommand(params[1]);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Used to create the command needed for users.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createUserCommand(String json, String[] restful) {

		return null;

	}

	/**
	 * Used to create the command needed for updating users.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createUpdateUserCommand(String json, String[] restful) {

		return null;

	}

	/**
	 * Used to create the command needed for deleting users.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createDeleteUserCommand(String json, String[] restful) {

		return null;

	}

	/**
	 * Used to create the command needed for converting raw to profile.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createConvertRawToProfileCommand(String json, String[] restful) {

		return null;

	}

	/**
	 * Used to create the command needed to get annotation information.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createGetAnnotationInformationCommand(String json, String[] restful) {

		return null;

	}

	/**
	 * Used to create the command needed to add annotation values.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createAddAnnotationValueCommand(String json, String[] restful) {

		return null;

	}

	/**
	 * Used to create the command needed to remove annotation fields.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createRemoveAnnotationFieldCommand(String json, String[] restful) {

		return null;

	}

	/**
	 * Used to create the command needed to get annotation privileges.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createGetAnnotationPrivilegesCommand(String json, String[] restful) {

		return null;

	}

	/**
	 * Used to create the command needed to update annotation privileges.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createUpdateAnnotationPrivilegesCommand(String json, String[] restful) {

		return null;

	}

}
