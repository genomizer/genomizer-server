package command;

import server.Debug;
import server.WorkHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.WorkPool;

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
		return gson.fromJson(json, LoginCommand.class);
	}

	/**
	 * Used to create the command needed to handle logout.
	 * @param username on the person that wants to logout.
	 * @return a logout command.
	 */
	public Command createLogoutCommand(String username) {
		return new LogoutCommand(username);
	}

	/**
	 * Used to create the command needed to retrieve experiments.
	 * @param expID the ID of the experiment.
	 * @return the actual command.
	 */
	public Command createGetExperimentCommand(String expID) {
		return new GetExperimentCommand(expID);
	}

	/**
	 * Used to create the command needed to add experiments.
	 * @param json string to initiate class.
	 * @return the actual command.
	 */
	public Command createAddExperimentCommand(String json) {
		return gson.fromJson(json, AddExperimentCommand.class);
	}

	/**
	 * Used to create the command needed to update experiments.
	 * @param json string to initiate the class.
	 * @param expID the experiment ID.
	 * @return the actual command.
	 */
	public Command createUpdateExperimentCommand(String json, String expID) {
		return new UpdateExperimentCommand(json, expID);
	}

	/**
	 * Used to create the command needed to remove experiments.
	 * @param expID the ID of the experiment.
	 * @return the actual command.
	 */
	public Command createDeleteExperimentCommand(String expID) {
		return new DeleteExperimentCommand(expID);
	}

	/**
	 * Used to create the command needed to retrieve experiment files.
	 * @param fileID the ID of the file.
	 * @return the actual command.
	 */
	public Command createGetFileFromExperimentCommand(String fileID) {
		return new GetFileFromExperimentCommand(fileID);
	}

	/**
	 * Used to create the command needed to add files to experiments.
	 * @param json string to initiate class.
	 * @return the actual command.
	 */
	public Command createAddFileToExperimentCommand(String json) {
		return gson.fromJson(json, AddFileToExperimentCommand.class);
	}

	/**
	 * Used to create the command needed to update files in experiments.
	 * @param json string to initiate class.
	 * @param expID the ID of the experiment.
	 * @return the actual command.
	 */
	public Command createUpdateFileInExperimentCommand(String json,
													   String expID) {
		return new UpdateFileInExperimentCommand(json, expID);
	}

	/**
	 * Used to create the command needed to remove files from experiments.
	 * @param fileID the ID of the file.
	 * @return the actual command.
	 */
	public Command createDeleteFileFromExperimentCommand(String fileID) {
		return new DeleteFileFromExperimentCommand(fileID);
	}

	/**
	 * Used to create the command needed to search for experiments.
	 * @param pubmedQuery the pubmed query.
	 * @return the actual command.
	 */
	public Command createSearchForExperimentCommand(String pubmedQuery) {
		int index = pubmedQuery.indexOf("=");
		return new SearchForExperimentsCommand(pubmedQuery.substring(index+1));
	}

	/**
	 * Used to create the command needed to create a new user.
	 * @param json string to initiate class.
	 * @return the actual command.
	 */
	public Command createCreateUserCommand(String json) {
		return gson.fromJson(json, CreateUserCommand.class);
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
	 * @param processType the type of process.
	 * @return the actual command.
	 */
	public Command createProcessCommand(String json, String username,
										String processType) {
		ProcessCommand processCommand = gson.fromJson(json,
				ProcessCommand.class);
		processCommand.setUsername(username);
		processCommand.setTimestamp(System.currentTimeMillis());
		processCommand.setProcessType(processType);
		Debug.log("Username: " + username + " timestamp: " +
				System.currentTimeMillis() + " parsedRest: " + processType);
		return processCommand;
	}

	/**
	 * Used to create the command needed to get annotation information.
	 * @return the actual command.
	 */
	public Command createGetAnnotationInformationCommand() {
		return new GetAnnotationInformationCommand();
	}

	/**
	 * Used to create a command needed to add annotation fields.
	 * @param json string to initiate class.
	 * @return the actual command.
	 */
	public Command createAddAnnotationFieldCommand(String json) {
		return gson.fromJson(json, AddAnnotationFieldCommand.class);
	}

	/**
	 * Used to create the command needed to add annotation values.
	 * @param json string to initiate class.
	 * @return the actual command.
	 */
	public Command createAddAnnotationValueCommand(String json) {
		return gson.fromJson(json, AddAnnotationValueCommand.class);
	}

	/**
	 * Used to create the command needed to remove annotation fields.
	 * @param fieldName the name of the field.
	 * @return the actual command.
	 */
	public Command createRemoveAnnotationFieldCommand(String fieldName) {
		return new DeleteAnnotationFieldCommand(fieldName);
	}

	/**
	 * Used to create the command needed to get annotation privileges.
	 * @return the actual command.
	 */
	public Command createGetAnnotationPrivilegesCommand(String userName) {
		return new GetAnnotationPrivilegesCommand(userName);
	}

	/**
	 * Used to create the command needed to update annotation privileges.
	 * @return the actual command.
	 */

	/**
	 * Used to create the command needed to update annotation privileges.
	 * @param json string to initiate class.
	 * @param userName the username.
	 * @return the actual command.
	 */
	public Command createUpdateAnnotationPrivilegesCommand(String json,
														   String userName) {
		return new UpdateAnnotationPrivilegesCommand(json, userName);
	}

	/**
	 * Used to create the command needed to add new genome releases.
	 * @param json string to initiate class.
	 * @return the actual command created.
	 */
	public Command createAddGenomeReleaseCommand(String json) {
		return gson.fromJson(json, AddGenomeReleaseCommand.class);
	}

	/**
	 * Used to create the command needed to rename an existing annotation value.
	 * @param json string to initiate class.
	 * @return the actual command created.
	 */
	public Command createRenameAnnotationValueCommand(String json) {
		return gson.fromJson(json, EditAnnotationValueCommand.class);
	}

	/**
	 * Used to create the command needed to delete an existing genome release.
	 * @param species associated with the genome release.
	 * @param genomeVersion to delete.
	 * @return the actual command.
	 */
	public Command createDeleteGenomeReleaseCommand(String species,
													String genomeVersion) {
		return new DeleteGenomeReleaseCommand(species, genomeVersion);
	}

	/**
	 * Used to create the command needed to delete a annotation value.
	 * @param value the value of the annotation.
	 * @param name of the annotation.
	 * @return the actual command created.
	 */
	public Command createDeleteAnnotationValueCommand(String value,
													  String name, Command.UserType userType) {
		return new DeleteAnnotationValueCommand(value, name, userType);
	}

	/**
	 * Used to create the command needed to edit an existing annotation field.
	 * @param json string to initiate class.
	 * @return the actual command.
	 */
	public Command createEditAnnotationFieldCommand(String json) {
		return gson.fromJson(json, EditAnnotationFieldCommand.class);
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
	public Command createGetProcessStatusCommand(WorkPool workPool) {
		return new GetProcessStatusCommand(workPool);
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
