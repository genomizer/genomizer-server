package command;

import authentication.Authenticate;
import response.ErrorResponse;
import response.ProcessResponse;
import response.Response;
import response.StatusCode;
import server.Debug;
import server.WorkHandler;

/**
 * Should be used to handle and create different commands using information
 * retrieved from the request.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class CommandHandler {
	private CommandFactory cmdFactory = new CommandFactory();
	
	/*Used to execute heavy work such as process commands execute*/
	private WorkHandler heavyWorkThread = new WorkHandler();

	public CommandHandler() {
		heavyWorkThread.start();
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
			if (myCom.validate()) {
				if(CommandType.PROCESS_COMMAND.equals(cmdt)){

					/*If the command is a process command, execution of it
					* starts in the heavy work thread, followed by returning a
					* OK status to the client.*/
					Debug.log("Adding processCommand to work queue.");
					heavyWorkThread.addWork((ProcessCommand)myCom);
					return new ProcessResponse(StatusCode.OK);
				}else {

					/*If the command is of the common kind, execute the
					* command and return the response.*/
					return myCom.execute();
				}
			} else {

				/*If the command is not valid, return an error response.*/
				Debug.log("Command not valid");
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The " +
						"command was invalid. Check the input! Valid " +
						"characters are A-Z, a-z, 0-9 and space");
			}

		} catch(ValidateException e) {
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

		String username = Authenticate.getUsername(uuid);
		Command newCommand = null;

		int uriLength = calculateURILength(uri);
		String parsedURI = parseRequestURI(uri);

		if (cmdt == CommandType.DELETE_ANNOTATION_VALUE_COMMAND &&
				uriLength == RestfulSizes.DELETE_ANNOTATION_VALUE_COMMAND) {
			String[] rest = uri.split("/");
			newCommand = cmdFactory.createDeleteAnnotationValueCommand(json,
					rest[3], rest[4]);
		} else if(cmdt == CommandType.LOGIN_COMMAND &&
				uriLength == RestfulSizes.LOGIN_COMMAND) {
			newCommand = cmdFactory.createLoginCommand(json);
		} else if (cmdt == CommandType.LOGOUT_COMMAND &&
				uriLength == RestfulSizes.LOGOUT_COMMAND) {
			newCommand = cmdFactory.createLogoutCommand(username);
		} else if (cmdt == CommandType.GET_EXPERIMENT_COMMAND &&
				uriLength == RestfulSizes.GET_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createGetExperimentCommand(parsedURI);
		} else if (cmdt == CommandType.ADD_EXPERIMENT_COMMAND &&
				uriLength == RestfulSizes.ADD_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createAddExperimentCommand(json);
		} else if (cmdt == CommandType.UPDATE_EXPERIMENT_COMMAND &&
				uriLength == RestfulSizes.UPDATE_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createUpdateExperimentCommand(json,
					parsedURI);
		} else if (cmdt == CommandType.DELETE_EXPERIMENT_COMMAND &&
				uriLength == RestfulSizes.DELETE_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createDeleteExperimentCommand(json,
					parsedURI);
		} else if (cmdt == CommandType.GET_FILE_FROM_EXPERIMENT_COMMAND &&
				uriLength == RestfulSizes.GET_FILE_FROM_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createGetFileFromExperimentCommand(json,
					parsedURI);
		} else if (cmdt == CommandType.ADD_FILE_TO_EXPERIMENT_COMMAND &&
				uriLength == RestfulSizes.ADD_FILE_TO_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createAddFileToExperimentCommand(json);
		} else if (cmdt == CommandType.UPDATE_FILE_IN_EXPERIMENT_COMMAND &&
				uriLength == RestfulSizes.UPDATE_FILE_IN_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createUpdateFileInExperimentCommand(json,
					parsedURI);
		} else if (cmdt == CommandType.DELETE_FILE_FROM_EXPERIMENT_COMMAND &&
				uriLength == RestfulSizes.DELETE_FILE_FROM_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createDeleteFileFromExperimentCommand(json,
					parsedURI);
		} else if (cmdt == CommandType.SEARCH_FOR_EXPERIMENTS_COMMAND &&
				uriLength == RestfulSizes.SEARCH_FOR_EXPERIMENTS_COMMAND) {
			newCommand = cmdFactory.createSearchForExperimentCommand(parsedURI);
		} else if (cmdt == CommandType.DELETE_USER_COMMAND &&
				uriLength == RestfulSizes.DELETE_USER_COMMAND) {
			newCommand = cmdFactory.createDeleteUserCommand(parsedURI);
		} else if (cmdt == CommandType.PROCESS_COMMAND &&
				uriLength == RestfulSizes.PROCESS_COMMAND) {
			newCommand = cmdFactory.createProcessCommand(json, username,
					parsedURI);
		} else if (cmdt == CommandType.GET_PROCESS_STATUS_COMMAND &&
				uriLength == RestfulSizes.GET_PROCESS_STATUS_COMMAND) {
			newCommand = cmdFactory.createGetProcessStatusCommand(
					heavyWorkThread);
		} else if (cmdt == CommandType.GET_ANNOTATION_INFORMATION_COMMAND &&
				uriLength == RestfulSizes.GET_ANNOTATION_INFORMATION_COMMAND) {
			newCommand = cmdFactory.createGetAnnotationInformationCommand(json);
		} else if (cmdt == CommandType.ADD_ANNOTATION_FIELD_COMMAND &&
				uriLength == RestfulSizes.ADD_ANNOTATION_FIELD_COMMAND) {
			newCommand = cmdFactory.createAddAnnotationFieldCommand(json,
					parsedURI);
		} else if (cmdt == CommandType.ADD_ANNOTATION_VALUE_COMMAND &&
				uriLength == RestfulSizes.ADD_ANNOTATION_VALUE_COMMAND) {
			newCommand = cmdFactory.createAddAnnotationValueCommand(json,
					parsedURI);
		} else if (cmdt == CommandType.RENAME_ANNOTATION_VALUE_COMMAND &&
				uriLength == RestfulSizes.RENAME_ANNOTATION_VALUE_COMMAND) {
			newCommand = cmdFactory.creatRenameAnnotationValueCommand(json);
		} else if (cmdt == CommandType.RENAME_ANNOTATION_FIELD_COMMAND &&
				uriLength == RestfulSizes.RENAME_ANNOTATION_FIELD_COMMAND) {
			newCommand = cmdFactory.createEditAnnotationFieldCommand(json);
		} else if (cmdt == CommandType.REMOVE_ANNOTATION_FIELD_COMMAND &&
				uriLength == RestfulSizes.REMOVE_ANNOTATION_FIELD_COMMAND) {
			newCommand = cmdFactory.createRemoveAnnotationFieldCommand(json,
					parsedURI);
		} else if (cmdt == CommandType.GET_ANNOTATION_PRIVILEGES_COMMAND &&
				uriLength == RestfulSizes.GET_ANNOTATION_PRIVILEGES_COMMAND) {
			newCommand = cmdFactory.createGetAnnotationPrivilegesCommand(json);
		} else if (cmdt == CommandType.UPDATE_ANNOTATION_PRIVILEGES_COMMAND &&
				uriLength == RestfulSizes.
						UPDATE_ANNOTATION_PRIVILEGES_COMMAND) {
			newCommand = cmdFactory.createUpdateAnnotationPrivilegesCommand(
					json, parsedURI);
		} else if (cmdt == CommandType.ADD_GENOME_RELEASE_COMMAND &&
				uriLength == RestfulSizes.ADD_GENOME_RELEASE_COMMAND) {
			newCommand = cmdFactory.createAddGenomeReleaseCommand(json);
		} else if (cmdt == CommandType.DELETE_GENOME_RELEASE_COMMAND &&
				uriLength == RestfulSizes.DELETE_GENOME_RELEASE_COMMAND) {
			String[] rest = uri.split("/");
			newCommand = cmdFactory.createDeleteGenomeReleaseCommand(rest[2],
					rest[3]);
		} else if(cmdt==CommandType.GET_ALL_GENOME_RELEASE_COMMAND &&
				uriLength == RestfulSizes.GET_ALL_GENOME_RELEASE_COMMAND) {
			newCommand=cmdFactory.createGetAllGenomeReleasesCommand();
		} else if(cmdt==CommandType.GET_GENOME_RELEASE_SPECIES_COMMAND &&
				uriLength == RestfulSizes.GET_GENOME_RELEASE_SPECIES_COMMAND) {
			newCommand=cmdFactory.createGetGenomeReleasesSpeciesCommand(
					parsedURI);
		} else if(cmdt==CommandType.CREATE_USER_COMMAND &&
				uriLength == RestfulSizes.CREATE_USER_COMMAND) {
			newCommand=cmdFactory.createCreateUserCommand(json);
		} else if(cmdt==CommandType.IS_TOKEN_VALID_COMMAND) {
			newCommand=cmdFactory.createIsTokenValidCommand(uuid);
		}

		return newCommand;
	}

	/**
	 * Used to fetch the relevant part of the request URI, for example, in the
	 * case of a "retrieve experiment" request with the request URI
	 * /experiment/\<experiment-id\> the \<experiment-id\> part would be
	 * returned.
	 * @param requestURI the URI part of the request line.
	 * @return the last part of the request URI.
	 */
	public String parseRequestURI(String requestURI) {
		String[] split = requestURI.split("/");
		return split[split.length -1];
	}

	/**
	 * Returns the so called "length" of the request URI. For example, in the
	 * request URI "/experiment/\<experiment-id\>" the length would be 2
	 * (experiment and \<experiment-id\>).
	 * @param requestURI the URI part of the request line.
	 * @return the "length" of the request URI.
	 */
	private int calculateURILength(String requestURI) {
		return requestURI.split("/").length-1;
	}

}
