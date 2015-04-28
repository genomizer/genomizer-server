package command;

import authentication.Authenticate;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.ProcessResponse;
import response.Response;
import response.StatusCode;
import server.Debug;
import server.ErrorLogger;
import server.WorkPool;

/**
 * Should be used to handle and create different commands using information
 * retrieved from the request.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class CommandHandler {
	private CommandFactory cmdFactory = new CommandFactory();
	private WorkPool workPool;

	public CommandHandler(WorkPool workPool) {
		this.workPool = workPool;
	}

	/**
	 * Starts the processing of the request by receiving parsed parts of it.
	 * @param json the JSON part of the request as a string.
	 * @param uri the URI found in the request line of the request.
	 * @param uuid the uuid of the user logged in on the client.
	 * @param cmdt the assumed command type of the JSON object.
	 * @return a Response object.
	 */
	public Response processNewCommand(String json, String uri,
									  String uuid, CommandType cmdt) {
		Command myCom = createCommand(json, uri, uuid, cmdt);
		if(myCom == null) {

			/*If a command could not be created from the given request, return
			* an error response.*/
			Debug.log("COMMAND IS NULL, COULD NOT CREATE A COMMAND FROM JSON");
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Could not " +
					"create a command from request. Bad format on request.");
		}

		try {
			myCom.validate();
			if (CommandType.PROCESS_COMMAND.equals(cmdt)) {

					/*If the command is a process command, execution of it
					* starts in the heavy work thread, followed by returning a
					* OK status to the client.*/
					Debug.log("Adding processCommand to work queue.");
					workPool.addWork((ProcessCommand)myCom);
					return new ProcessResponse(StatusCode.OK);
				}else {

				/*If the command is of the common kind, execute the command
				* and return the response.*/
				return myCom.execute();
 			}
		} catch(ValidateException e) {
			Debug.log(e.getMessage());
			ErrorLogger.log("ValidateException", e.getMessage());
			return new ErrorResponse(e.getCode(), e.getMessage());
		}
	}

	/**
	 * Method used to create command objects together with CommandFactory.
	 *
	 * @param json the JSON part of the request as a string.
	 * @param uri the URI found in the request line of the request..
	 * @param cmdt the assumed command type of the JSON object.
	 * @return an executable Command object of the correct type.
	 */
	private Command createCommand(String json, String uri, String uuid,
								  CommandType cmdt) {
		if (RestfulLengths.getSize(cmdt) != calculateURISize(uri)) {
			return null;
		}

		String parsedURI = parseRequestURI(uri);
		String username = Authenticate.getUsernameByID(uuid);
		//TODO Add the get user type method
		UserType userType = UserType.ADMIN;
		String[] rest;
		switch (cmdt) {
			case DELETE_ANNOTATION_VALUE_COMMAND:
				rest = uri.split("/");
				return cmdFactory.
						createDeleteAnnotationValueCommand(rest[3], rest[4],
								userType);
			case LOGIN_COMMAND:
				return cmdFactory.createLoginCommand(json);
			case LOGOUT_COMMAND:
				return cmdFactory.createLogoutCommand(username);
			case GET_EXPERIMENT_COMMAND:
				return cmdFactory.createGetExperimentCommand(parsedURI,
						userType);
			case ADD_EXPERIMENT_COMMAND:
				return cmdFactory.createAddExperimentCommand(json, userType);
			case UPDATE_EXPERIMENT_COMMAND:
				return cmdFactory.
						createUpdateExperimentCommand(json, parsedURI,
								userType);
			case DELETE_EXPERIMENT_COMMAND:
				return cmdFactory.createDeleteExperimentCommand(parsedURI,
						userType);
			case GET_FILE_FROM_EXPERIMENT_COMMAND:
				return cmdFactory.createGetFileFromExperimentCommand(parsedURI,
						userType);
			case ADD_FILE_TO_EXPERIMENT_COMMAND:
				return cmdFactory.createAddFileToExperimentCommand(json,
						userType);
			case UPDATE_FILE_IN_EXPERIMENT_COMMAND:
				return cmdFactory.createUpdateFileInExperimentCommand(json,
						parsedURI, userType);
			case DELETE_FILE_FROM_EXPERIMENT_COMMAND:
				return cmdFactory.
                        createDeleteFileFromExperimentCommand(parsedURI,
								userType);
			case SEARCH_FOR_EXPERIMENTS_COMMAND:
				return cmdFactory.createSearchForExperimentCommand(parsedURI,
						userType);
			case DELETE_USER_COMMAND:
				return cmdFactory.createDeleteUserCommand(parsedURI, userType);
			case PROCESS_COMMAND:
				return cmdFactory.createProcessCommand(json, username,
						parsedURI, userType);
			case GET_PROCESS_STATUS_COMMAND:
				return cmdFactory.createGetProcessStatusCommand(workPool,
						userType);
			case GET_ANNOTATION_INFORMATION_COMMAND:
				return cmdFactory.createGetAnnotationInformationCommand(
						userType);
			case ADD_ANNOTATION_FIELD_COMMAND:
				return cmdFactory.createAddAnnotationFieldCommand(json,
						userType);
			case ADD_ANNOTATION_VALUE_COMMAND:
			return cmdFactory.createAddAnnotationValueCommand(json,
						userType);
			case RENAME_ANNOTATION_FIELD_COMMAND:
				return cmdFactory.createEditAnnotationFieldCommand(json,
						userType);
//			case RENAME_ANNOTATION_VALUE_COMMAND:
//				return cmdFactory.createEditAnnotationFieldCommand(json);
			case REMOVE_ANNOTATION_FIELD_COMMAND:
				return cmdFactory.createRemoveAnnotationFieldCommand(parsedURI,
						userType);
			case GET_ANNOTATION_PRIVILEGES_COMMAND:
				return cmdFactory.createGetAnnotationPrivilegesCommand(json,
						userType);
			case UPDATE_ANNOTATION_PRIVILEGES_COMMAND:
				return cmdFactory.createUpdateAnnotationPrivilegesCommand(json,
						parsedURI);
			case ADD_GENOME_RELEASE_COMMAND:
				return cmdFactory.createAddGenomeReleaseCommand(json);
			case DELETE_GENOME_RELEASE_COMMAND:
				rest = uri.split("/");
				return cmdFactory.createDeleteGenomeReleaseCommand(rest[2],
						rest[3]);
			case GET_ALL_GENOME_RELEASE_COMMAND:
				return cmdFactory.createGetAllGenomeReleasesCommand();
			case GET_GENOME_RELEASE_SPECIES_COMMAND:
				return cmdFactory.
						createGetGenomeReleasesSpeciesCommand(parsedURI);
			case CREATE_USER_COMMAND:
				return cmdFactory.createCreateUserCommand(json);
			case IS_TOKEN_VALID_COMMAND:
				return cmdFactory.createIsTokenValidCommand(uuid);
			default:
				return null;
		}
	}

	/**
	 * Used to fetch the relevant part of the request URI, for example, in the
	 * case of a "retrieve experiment" request with the request URI
	 * /experiment/\<experiment-id\> the \<experiment-id\> part would be
	 * returned.
	 * @param requestURI the URI part of the request line.
	 * @return the last part of the request URI.
	 */
	private String parseRequestURI(String requestURI) {
		String[] split = requestURI.split("/");
		return split[split.length -1];
	}

	/**
	 * Returns the so called "size" of the request URI. For example, in the
	 * request URI "/experiment/\<experiment-id\>" the length would be 2
	 * (experiment and \<experiment-id\>).
	 * @param requestURI the URI part of the request line.
	 * @return the "size" of the request URI.
	 */
	private int calculateURISize(String requestURI) {
		return requestURI.split("/").length-1;
	}
}
