package command;

import server.Debug;
import server.WorkHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class is used to create and return different commands that
 * will be executed later by calling their execute() method.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class CommandFactory {

	private final GsonBuilder builder;
	private final Gson gson;

	/**
	 * Constructor that creates the GSON builder.
	 */
	public CommandFactory() {

	    builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    gson = builder.create();

	}

	/**
	 * Used to create the command needed to handle login.
	 *
	 * @param JSON string to initiate class.
	 * @return the actual command.
	 */
	public Command createLoginCommand(String json) {

		final Command loginCmd = gson.fromJson(json, LoginCommand.class);
		return loginCmd;

	}

	/**
	 * Used to create the command needed to handle logout.
	 *
	 * @param username on the person that wants to logout.
	 * @return a logout command.
	 */
	public Command createLogoutCommand(String username) {

		final Command logoutCmd = new LogoutCommand(username);
		return logoutCmd;

	}

	/**
	 * Used to create the command needed to retrieve experiments.
	 *
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createGetExperimentCommand(String restful) {

		return new GetExperimentCommand(restful);

	}

	/**
	 * Used to create the command needed to add experiments.
	 *
	 * @param JSON string to initiate class.
	 * @return the actual command.
	 */
	public Command createAddExperimentCommand(String json) {

		final Command addExperimentCmd = gson.fromJson(json, AddExperimentCommand.class);
		return addExperimentCmd;

	}

	/**
	 * Used to create the command needed to update experiments.
	 *
	 * @param JSON string to initiate class.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createUpdateExperimentCommand(String json, String restful) {

		return new UpdateExperimentCommand();

	}

	/**
	 * Used to create the command needed to remove experiments.
	 *
	 * @param JSON string to initiate class.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createDeleteExperimentCommand(String json, String restful) {

		return new DeleteExperimentCommand(restful);

	}

	/**
	 * Used to create the command needed to retrieve experiment files.
	 *
	 * @param JSON string to initiate class.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createGetFileFromExperimentCommand(String json, String restful) {

		return new GetFileFromExperimentCommand(restful);

	}

	/**
	 * Used to create the command needed to add files to experiments.
	 *
	 * @param JSON string to initiate class.
	 * @return the actual command.
	 */
	public Command createAddFileToExperimentCommand(String json) {

		final Command addFileToExperimentCmd = gson.fromJson(json, AddFileToExperimentCommand.class);
		return addFileToExperimentCmd;

	}

	/**
	 * Used to create the command needed to update files in experiments.
	 *
	 * @param JSON string to initiate class.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createUpdateFileInExperimentCommand(String json, String restful) {

		return new UpdateFileInExperimentCommand();

	}

	/**
	 * Used to create the command needed to remove files from experiments.
	 *
	 * @param JSON string to initiate class.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createDeleteFileFromExperimentCommand(String json, String restful) {

		return new DeleteFileFromExperimentCommand(restful);

	}

	/**
	 * Used to create the command needed to search for experiments.
	 *
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createSearchForExperimentCommand(String restful) {

		int index = restful.indexOf("=");
		return new SearchForExperimentsCommand(restful.substring(index+1));

	}

	/**
	 * Used to create the command needed to create a new user.
	 *
	 * @param JSON string to initiate class.
	 * @return the actual command.
	 */
	public Command createCreateUserCommand(String json) {

		final Command createUserCmd = gson.fromJson(json, CreateUserCommand.class);
		return createUserCmd;

	}

	/**
	 * Used to create the command needed to delete a user.
	 *
	 * @param username to delete.
	 * @return the actual command.
	 */
	public Command createDeleteUserCommand(String username) {

		return new DeleteUserCommand(username);

	}

	/**
	 * Used to create the command needed to convert data from raw to profile.
	 *
	 * @param JSON string to initiate class.
	 * @param username that executed the command.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createProcessCommand(String json, String username, String parsedRest) {

		ProcessCommand processCommand = gson.fromJson(json, ProcessCommand.class);
//		processCommand.setProcessType(restful[2]);
//		processCommand.setFileID(restful[3]);
		processCommand.setUsername(username);
		processCommand.setTimestamp(System.currentTimeMillis());
		processCommand.setProcessType(parsedRest);
		Debug.log("Username: " + username + " timestamp: " + System.currentTimeMillis() + " parsedRest: " + parsedRest);
		//Create from json
		//set userID
		//set fileID
		//set processType
		return processCommand;

	}

	/**
	 * Used to create the command needed to get annotation information.
	 *
	 * @param JSON string to initiate class.
	 * @return the actual command.
	 */
	public Command createGetAnnotationInformationCommand(String json) {

		return new GetAnnotationInformationCommand();

	}

	/**
	 * Used to create a command needed to add annotation fields.
	 *
	 * @param JSON string to initiate class.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createAddAnnotationFieldCommand(String json, String restful) {

		final Command AddAnnotationFieldCmd = gson.fromJson(json, AddAnnotationFieldCommand.class);
		return AddAnnotationFieldCmd;

	}

	/**
	 * Used to create the command needed to add annotation values.
	 *
	 * @param JSON string to initiate class.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createAddAnnotationValueCommand(String json, String restful) {

		Command AddAnnotationValueCommand = gson.fromJson(json, AddAnnotationValueCommand.class);
		return AddAnnotationValueCommand;

	}

	/**
	 * Used to create the command needed to remove annotation fields.
	 *
	 * @param JSON string to initiate class.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createRemoveAnnotationFieldCommand(String json, String restful) {

		return new DeleteAnnotationFieldCommand(restful);

	}

	/**
	 * Used to create the command needed to get annotation privileges.
	 *
	 * @param JSON string to initiate class.
	 * @return the actual command.
	 */
	public Command createGetAnnotationPrivilegesCommand(String json) {

		return new GetAnnotationPrivilegesCommand();

	}

	/**
	 * Used to create the command needed to update annotation privileges.
	 *
	 * @param JSON string to initiate class.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createUpdateAnnotationPrivilegesCommand(String json, String restful) {

		return new UpdateAnnotationPrivilegesCommand();

	}

	/**
	 * Used to create the command needed to add new genome releases.
	 *
	 * @param JSON string to initiate class.
	 * @return the actual command created.
	 */
	public Command createAddGenomeReleaseCommand(String json) {

		final Command addGenomeReleaseCmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
		return addGenomeReleaseCmd;

	}

	/**
	 * Used to create the command needed to rename an existing annotation value.
	 *
	 * @param JSON string to initiate class.
	 * @return the actual command created.
	 */
	public Command creatRenameAnnotationValueCommand(String json) {

		Command command = gson.fromJson(json, EditAnnotationValueCommand.class);
		return command;

	}

	/**
	 * Used to create the command needed to delete an existing genome release.
	 *
	 * @param the specie associated with the genome release.
	 * @param the genome version to delete.
	 * @return the actual command.
	 */
	public Command createDeleteGenomeReleaseCommand(String specie, String genomeVersion) {

		final Command deleteGenomeReleaseCmd = new DeleteGenomeReleaseCommand(specie, genomeVersion);
		return deleteGenomeReleaseCmd;

	}

	/**
	 * Used to create the command needed to delete a annotation value.
	 *
	 * @param JSON string to initiate class.
	 * @param value
	 * @param name
	 * @return the actual command created.
	 */
	public Command createDeleteAnnotationValueCommand(String json,
			String value, String name) {

		return new DeleteAnnotationValueCommand(value, name);

	}

	/**
	 * Used to create the command needed to edit an existing annotation field.
	 *
	 * @param JSON string to initiate class.
	 * @return the actual command.
	 */
	public Command createEditAnnotationFieldCommand(String json) {

		Command command = gson.fromJson(json, EditAnnotationFieldCommand.class);
		return command;

	}

	/**
	 * Used to create the command needed to get all genome releases.
	 *
	 * @return the actual command.
	 */
	public Command createGetAllGenomeReleasesCommand() {

		return new GetGenomeReleaseCommand();

	}

	/**
	 * Used to create the command needed to get all genome releases on
	 * a given species.
	 *
	 * @param species to get.
	 * @return the actual command.
	 */
	public Command createGetGenomeReleasesSpeciesCommand(String species) {

		return new GetGenomeReleaseSpeciesCommand(species);

	}

	/**
	 * Used to create the command needed to get the process status.
	 *
	 * @param a workHandler class object needed.
	 * @return the actual command.
	 */
	public Command createGetProcessStatusCommand(WorkHandler workHandler) {

		return new GetProcessStatusCommand(workHandler);

	}

	/**
	 * Used to check if a token is valid.
	 *
	 * @param a user ID.
	 * @return the actual command.
	 */
	public Command createIsTokenValidCommand(String uuid) {

		return new IsTokenValidCommand(uuid);

	}

}
