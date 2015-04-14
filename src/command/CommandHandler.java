package command;

import java.util.ArrayList;
import java.util.Arrays;
import authentication.Authenticate;
import response.ErrorResponse;
import response.ProcessResponse;
import response.Response;
import response.StatusCode;
import server.Debug;
import server.WorkHandler;

/**
 * Should be used to handle and create different commands with
 * JSON and RESTful-header.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class CommandHandler {

	private CommandFactory cmdFactory = new CommandFactory();
	
	//used to execute heavy work such as process commands execute
	private WorkHandler heavyWorkThread = new WorkHandler();
	/**
	 * Empty constructor.
	 */
	public CommandHandler() {
		heavyWorkThread.start();
	}

	/**
	 * Method that starts the actual handling of JSON together
	 * with the RESTful-header and converts them into commands
	 * and runs them.
	 *
	 * @param json string.
	 * @param restful header.
	 * @param cmdt - enumeration that determines command type.
	 */
	public Response processNewCommand(String json, String restful, String uuid, CommandType cmdt) {

		Command myCom = createCommand(json, restful, uuid, cmdt);

		if(myCom == null) {
			Debug.log("COMMAND IS NULL, COULD NOT CREATE A COMMAND FROM JSON AND RESTFUL.");
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Could not create a command from request. Bad format on restful.");
		}

		try {

			if (myCom.validate()) {
				if(CommandType.PROCESS_COMMAND.equals(cmdt)){
					//add the heavy command to the queue, executed when the
					//command is at the head of the queue, return OK to tell the client
					Debug.log("Adding processCommand to workqueue");
					heavyWorkThread.addWork((ProcessCommand)myCom);
					return new ProcessResponse(StatusCode.OK);
				}else{
					return myCom.execute();
				}
			} else {
				Debug.log("Command not valid");
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The command was invalid. Check the input! Valid characters are A-Z, a-z, 0-9 and space");
			}

		} catch(ValidateException e) {
			return new ErrorResponse(e.getCode(), e.getMessage());
		}
	}

	/**
	 * Method used to create command objects together with CommandFactory.
	 *
	 * @param json string.
	 * @param restful header.
	 * @param cmdt enumeration that determines command type.
	 * @return Command of the correct type.
	 */
	private Command createCommand(String json, String restful, String uuid, CommandType cmdt) {

		String username = Authenticate.getUsername(uuid);
		Command newCommand = null;

		ArrayList<String> restPieces = splittRestful(restful, cmdt);

		int nrOfRestfuls = restPieces.size();

		if (cmdt == CommandType.DELETE_ANNOTATION_VALUE_COMMAND) {
			String[] rest = restful.split("/");
			newCommand = cmdFactory.createDeleteAnnotationValueCommand(json, rest[3], rest[4]);
		}
		String parsedRest = parseRest(restful);

		if(cmdt == CommandType.LOGIN_COMMAND && nrOfRestfuls == RestfulSizes.LOGIN_COMMAND) {
			newCommand = cmdFactory.createLoginCommand(json);
		} else if (cmdt == CommandType.LOGOUT_COMMAND && nrOfRestfuls == RestfulSizes.LOGOUT_COMMAND) {
			newCommand = cmdFactory.createLogoutCommand(username);
		} else if (cmdt == CommandType.GET_EXPERIMENT_COMMAND && nrOfRestfuls == RestfulSizes.GET_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createGetExperimentCommand(parsedRest);
		} else if (cmdt == CommandType.ADD_EXPERIMENT_COMMAND && nrOfRestfuls == RestfulSizes.ADD_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createAddExperimentCommand(json);
		} else if (cmdt == CommandType.UPDATE_EXPERIMENT_COMMAND && nrOfRestfuls == RestfulSizes.UPDATE_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createUpdateExperimentCommand(json, parsedRest);
		} else if (cmdt == CommandType.DELETE_EXPERIMENT_COMMAND && nrOfRestfuls == RestfulSizes.DELETE_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createDeleteExperimentCommand(json, parsedRest);
		} else if (cmdt == CommandType.GET_FILE_FROM_EXPERIMENT_COMMAND && nrOfRestfuls == RestfulSizes.GET_FILE_FROM_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createGetFileFromExperimentCommand(json, parsedRest);
		} else if (cmdt == CommandType.ADD_FILE_TO_EXPERIMENT_COMMAND && nrOfRestfuls == RestfulSizes.ADD_FILE_TO_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createAddFileToExperimentCommand(json);
		} else if (cmdt == CommandType.UPDATE_FILE_IN_EXPERIMENT_COMMAND && nrOfRestfuls == RestfulSizes.UPDATE_FILE_IN_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createUpdateFileInExperimentCommand(json, parsedRest);
		} else if (cmdt == CommandType.DELETE_FILE_FROM_EXPERIMENT_COMMAND && nrOfRestfuls == RestfulSizes.DELETE_FILE_FROM_EXPERIMENT_COMMAND) {
			newCommand = cmdFactory.createDeleteFileFromExperimentCommand(json, parsedRest);
		} else if (cmdt == CommandType.SEARCH_FOR_EXPERIMENTS_COMMAND && nrOfRestfuls == RestfulSizes.SEARCH_FOR_EXPERIMENTS_COMMAND) {
			newCommand = cmdFactory.createSearchForExperimentCommand(parsedRest);
		} else if (cmdt == CommandType.DELETE_USER_COMMAND && nrOfRestfuls == RestfulSizes.DELETE_USER_COMMAND) {
			newCommand = cmdFactory.createDeleteUserCommand(parsedRest);
		} else if (cmdt == CommandType.PROCESS_COMMAND && nrOfRestfuls == RestfulSizes.PROCESS_COMMAND) {
			newCommand = cmdFactory.createProcessCommand(json, username, parsedRest);
		} else if (cmdt == CommandType.GET_PROCESS_STATUS_COMMAND && nrOfRestfuls == RestfulSizes.GET_PROCESS_STATUS_COMMAND) {
			newCommand = cmdFactory.createGetProcessStatusCommand(heavyWorkThread);
		} else if (cmdt == CommandType.GET_ANNOTATION_INFORMATION_COMMAND && nrOfRestfuls == RestfulSizes.GET_ANNOTATION_INFORMATION_COMMAND) {
			newCommand = cmdFactory.createGetAnnotationInformationCommand(json);
		} else if (cmdt == CommandType.ADD_ANNOTATION_FIELD_COMMAND && nrOfRestfuls == RestfulSizes.ADD_ANNOTATION_FIELD_COMMAND) {
			newCommand = cmdFactory.createAddAnnotationFieldCommand(json, parsedRest);
		} else if (cmdt == CommandType.ADD_ANNOTATION_VALUE_COMMAND && nrOfRestfuls == RestfulSizes.ADD_ANNOTATION_VALUE_COMMAND) {
			newCommand = cmdFactory.createAddAnnotationValueCommand(json, parsedRest);
		} else if (cmdt == CommandType.RENAME_ANNOTATION_VALUE_COMMAND && nrOfRestfuls == RestfulSizes.RENAME_ANNOTATION_VALUE_COMMAND) {
			newCommand = cmdFactory.creatRenameAnnotationValueCommand(json);
		} else if (cmdt == CommandType.RENAME_ANNOTATION_FIELD_COMMAND && nrOfRestfuls == RestfulSizes.RENAME_ANNOTATION_FIELD_COMMAND) {
			newCommand = cmdFactory.createEditAnnotationFieldCommand(json);
		} else if (cmdt == CommandType.REMOVE_ANNOTATION_FIELD_COMMAND && nrOfRestfuls == RestfulSizes.REMOVE_ANNOTATION_FIELD_COMMAND) {
			newCommand = cmdFactory.createRemoveAnnotationFieldCommand(json, parsedRest);
		} else if (cmdt == CommandType.GET_ANNOTATION_PRIVILEGES_COMMAND && nrOfRestfuls == RestfulSizes.GET_ANNOTATION_PRIVILEGES_COMMAND) {
			newCommand = cmdFactory.createGetAnnotationPrivilegesCommand(json);
		} else if (cmdt == CommandType.UPDATE_ANNOTATION_PRIVILEGES_COMMAND && nrOfRestfuls == RestfulSizes.UPDATE_ANNOTATION_PRIVILEGES_COMMAND) {
			newCommand = cmdFactory.createUpdateAnnotationPrivilegesCommand(json, parsedRest);
		} else if (cmdt == CommandType.ADD_GENOME_RELEASE_COMMAND && nrOfRestfuls == RestfulSizes.ADD_GENOME_RELEASE_COMMAND) {
			newCommand = cmdFactory.createAddGenomeReleaseCommand(json);
		} else if (cmdt == CommandType.DELETE_GENOME_RELEASE_COMMAND && nrOfRestfuls == RestfulSizes.DELETE_GENOME_RELEASE_COMMAND) {
			String[] rest = restful.split("/");
			newCommand = cmdFactory.createDeleteGenomeReleaseCommand(rest[2], rest[3]);
		} else if(cmdt==CommandType.GET_ALL_GENOME_RELEASE_COMMAND && nrOfRestfuls == RestfulSizes.GET_ALL_GENOME_RELEASE_COMMAND) {
			newCommand=cmdFactory.createGetAllGenomeReleasesCommand();
		} else if(cmdt==CommandType.GET_GENOME_RELEASE_SPECIES_COMMAND && nrOfRestfuls == RestfulSizes.GET_GENOME_RELEASE_SPECIES_COMMAND) {
			newCommand=cmdFactory.createGetGenomeReleasesSpeciesCommand(parsedRest);
		} else if(cmdt==CommandType.CREATE_USER_COMMAND && nrOfRestfuls == RestfulSizes.CREATE_USER_COMMAND) {
			newCommand=cmdFactory.createCreateUserCommand(json);
		} else if(cmdt==CommandType.IS_TOKEN_VALID_COMMAND) {
			newCommand=cmdFactory.createIsTokenValidCommand(uuid);
		}
		return newCommand;
	}

	/**
	 * Method used to split a RESTful-header into smaller parts
	 * and return them in a String array.
	 *
	 * @param restful header
	 * @return String array with RESTful-header parts.
	 */
	public String parseRest(String restful) {

		String[] split = restful.split("/");
		return split[split.length -1];

	}

	/**
	 * Method used to split restful headers.
	 *
	 * @param restful to split.
	 * @return ArrayList with the splitted header.
	 */
	private ArrayList<String> splittRestful(String restful, CommandType cmdt) {

		ArrayList<String> rest = null;

		if(restful != null) {
			rest = new ArrayList<String>(Arrays.asList(restful.split("/")));;
			rest.remove(0);
		}

		return rest;
	}

}
