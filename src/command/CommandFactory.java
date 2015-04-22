package command;

import server.Debug;
import server.WorkHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class is used to create and return different commands that
 * will be executed later by calling their execute() method.
 *
 * @author Business Logic 2015.
 * @version 1.1
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
	 * @param json string to initiate class.
	 * @return the actual command.
	 */
	public Command createLoginCommand(String json) {
		final Command loginCmd = gson.fromJson(json, LoginCommand.class);
		return loginCmd;
	}

	/**
	 * Used to create the command needed to handle logout.
	 * @param username on the person that wants to logout.
	 * @return a logout command.
	 */
	public Command createLogoutCommand(String username) {
		final Command logoutCmd = new LogoutCommand(username);
		return logoutCmd;
	}

	/**
	 * Used to create the command needed to retrieve experiments.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createGetExperimentCommand(String restful) {
		return new GetExperimentCommand(restful);
	}

	/**
	 * Used to create the command needed to add experiments.
	 * @param json string to initiate class.
	 * @return the actual command.
	 */
	public Command createAddExperimentCommand(String json) {
		final Command addExperimentCmd = gson.fromJson(json,
				AddExperimentCommand.class);
		return addExperimentCmd;
	}

	/**
	 * Used to create the command needed to update experiments.
	 * @return the actual command.
	 */
	public Command createUpdateExperimentCommand(String json, String restful) {
		//TODO Make sure the json and restful are used.
		return new UpdateExperimentCommand();
	}

	/**
	 * Used to create the command needed to remove experiments.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createDeleteExperimentCommand(String restful) {
		return new DeleteExperimentCommand(restful);
	}

	/**
	 * Used to create the command needed to retrieve experiment files.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createGetFileFromExperimentCommand(String restful) {
		return new GetFileFromExperimentCommand(restful);
	}

	/**
	 * Used to create the command needed to add files to experiments.
	 * @param json string to initiate class.
	 * @return the actual command.
	 */
	public Command createAddFileToExperimentCommand(String json) {
		final Command addFileToExperimentCmd = gson.fromJson(json,
				AddFileToExperimentCommand.class);
		return addFileToExperimentCmd;
	}

	/**
	 * Used to create the command needed to update files in experiments.
	 * @return the actual command.
	 */
	public Command createUpdateFileInExperimentCommand(String json,
													   String restful) {
		//TODO Make sure the json and restful are used.
		return new UpdateFileInExperimentCommand();
	}

	/**
	 * Used to create the command needed to remove files from experiments.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createDeleteFileFromExperimentCommand(String restful) {
		return new DeleteFileFromExperimentCommand(restful);
	}

	/**
	 * Used to create the command needed to search for experiments.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createSearchForExperimentCommand(String restful) {

		int index = restful.indexOf("=");
		return new SearchForExperimentsCommand(restful.substring(index+1));

	}

	/**
	 * Used to create the command needed to create a new user.
	 * @param json string to initiate class.
	 * @return the actual command.
	 */
	public Command createCreateUserCommand(String json) {
		final Command createUserCmd = gson.fromJson(json,
				CreateUserCommand.class);
		return createUserCmd;
	}

	/**
	 * Used to create the command needed to delete a user.
	 * @param username to delete.
	 * @return the actual command.
	 */
	public Command createDeleteUserCommand(String username) {
		return new DeleteUserCommand(username);
	}

	/**
	 * Used to create the command needed to convert data from raw to profile.
	 * @param json string to initiate class.
	 * @param username that executed the command.
	 * @param parsedRest restful information needed.
	 * @return the actual command.
	 */
	public Command createProcessCommand(String json, String username,
										String parsedRest) {
		//Unsure if the out-commented lines in this function carry any
		//relevance (c12mkn 2015-04-16)

		ProcessCommand processCommand = gson.fromJson(json,
				ProcessCommand.class);
		//processCommand.setProcessType(restful[2]);
		//processCommand.setFileID(restful[3]);
		processCommand.setUsername(username);
		processCommand.setTimestamp(System.currentTimeMillis());
		processCommand.setProcessType(parsedRest);
		Debug.log("Username: " + username + " timestamp: " +
				System.currentTimeMillis() + " parsedRest: " + parsedRest);
		//Create from json
		//set userID
		//set fileID
		//set processType
		return processCommand;
	}

	/**
	 * Used to create the command needed to get annotation information.
	 * @return the actual command.
	 */
	public Command createGetAnnotationInformationCommand(String json) {
		//TODO Make sure the json is used.
		return new GetAnnotationInformationCommand();
	}

	/**
	 * Used to create a command needed to add annotation fields.
	 * @param json string to initiate class.
	 * @return the actual command.
	 */
	public Command createAddAnnotationFieldCommand(String json) {
		final Command AddAnnotationFieldCmd = gson.fromJson(json,
				AddAnnotationFieldCommand.class);
		return AddAnnotationFieldCmd;
	}

	/**
	 * Used to create the command needed to add annotation values.
	 * @param json string to initiate class.
	 * @return the actual command.
	 */
	public Command createAddAnnotationValueCommand(String json) {
		Command AddAnnotationValueCommand = gson.fromJson(json,
				AddAnnotationValueCommand.class);
		return AddAnnotationValueCommand;
	}

	/**
	 * Used to create the command needed to remove annotation fields.
	 * @param restful information needed.
	 * @return the actual command.
	 */
	public Command createRemoveAnnotationFieldCommand(String restful) {
		return new DeleteAnnotationFieldCommand(restful);
	}

	/**
	 * Used to create the command needed to get annotation privileges.
	 * @return the actual command.
	 */
	public Command createGetAnnotationPrivilegesCommand(String json) {
		//TODO Make sure the json is used.
		return new GetAnnotationPrivilegesCommand();
	}

	/**
	 * Used to create the command needed to update annotation privileges.
	 * @return the actual command.
	 */
	public Command createUpdateAnnotationPrivilegesCommand(String json,
														   String restful) {
		//TODO Make sure the json and restful are used.
		return new UpdateAnnotationPrivilegesCommand();
	}

	/**
	 * Used to create the command needed to add new genome releases.
	 * @param json string to initiate class.
	 * @return the actual command created.
	 */
	public Command createAddGenomeReleaseCommand(String json) {
		final Command addGenomeReleaseCmd = gson.fromJson(json,
				AddGenomeReleaseCommand.class);
		return addGenomeReleaseCmd;
	}

	/**
	 * Used to create the command needed to rename an existing annotation value.
	 * @param json string to initiate class.
	 * @return the actual command created.
	 */
	public Command creatRenameAnnotationValueCommand(String json) {
		Command command = gson.fromJson(json, EditAnnotationValueCommand.class);
		return command;
	}

	/**
	 * Used to create the command needed to delete an existing genome release.
	 * @param specie associated with the genome release.
	 * @param genomeVersion to delete.
	 * @return the actual command.
	 */
	public Command createDeleteGenomeReleaseCommand(String specie,
													String genomeVersion) {
		final Command deleteGenomeReleaseCmd = new DeleteGenomeReleaseCommand(
				specie, genomeVersion);
		return deleteGenomeReleaseCmd;
	}

	/**
	 * Used to create the command needed to delete a annotation value.
	 * @param value
	 * @param name
	 * @return the actual command created.
	 */
	public Command createDeleteAnnotationValueCommand(String value,
													  String name) {
		return new DeleteAnnotationValueCommand(value, name);
	}

	/**
	 * Used to create the command needed to edit an existing annotation field.
	 * @param json string to initiate class.
	 * @return the actual command.
	 */
	public Command createEditAnnotationFieldCommand(String json) {
		Command command = gson.fromJson(json, EditAnnotationFieldCommand.class);
		return command;
	}

	/**
	 * Used to create the command needed to get all genome releases.
	 * @return the actual command.
	 */
	public Command createGetAllGenomeReleasesCommand() {
		return new GetGenomeReleaseCommand();
	}

	/**
	 * Used to create the command needed to get all genome releases on
	 * a given species.
	 * @param species to get.
	 * @return the actual command.
	 */
	public Command createGetGenomeReleasesSpeciesCommand(String species) {
		return new GetGenomeReleaseSpeciesCommand(species);
	}

	/**
	 * Used to create the command needed to get the process status.
	 * @param workHandler class object needed.
	 * @return the actual command.
	 */
	public Command createGetProcessStatusCommand(WorkHandler workHandler) {
		return new GetProcessStatusCommand(workHandler);
	}

	/**
	 * Used to check if a token is valid.
	 * @param uuid user ID.
	 * @return a command.
	 */
	public Command createIsTokenValidCommand(String uuid) {
		return new IsTokenValidCommand(uuid);
	}

}
