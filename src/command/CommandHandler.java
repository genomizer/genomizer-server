package command;

import response.Response;

/**
 * Should be used to handle and create different commands with
 * JSON and restful.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class CommandHandler {

	//TODO: Add threads?

	private CommandFactory cmdFactory = new CommandFactory();

	/**
	 * Empty constructor.
	 */
	public CommandHandler() {

	}

	/**
	 * Method that starts the actual handling of JSON together
	 * with the RESTful-header and converts them into commands
	 * and runs them.
	 * @param a json string.
	 * @param a RESTful-header.
	 * @param a enum that determines command type.
	 */
	public Response processNewCommand(String json, String restful, String uuid, CommandType cmdt) {	//TODO: Rename this method.

		//Get code from restful //TODO: add parser code....
		Command myCom = createCommand(json, restful, uuid, cmdt);

		//TODO: Find out what type of work that needs to be done. (Slow? Fast?)
		//Create a response to return.
		Response rsp = myCom.execute();

		return rsp;

	}

	/**
	 * Method used to create command objects together with CommandFactory.
	 * @param a json string.
	 * @param a RESTful-header.
	 * @param a enum that determines command type.
	 * @return
	 */
	private Command createCommand(String json, String restful, String uuid, CommandType cmdt) {

		Command newCommand = null;
		String[] parsedRest = parseRest(restful);

		//TODO: Change to switch statement.
		if(cmdt == CommandType.LOGIN_COMMAND) {

			newCommand = cmdFactory.createLoginCommand(json, parsedRest);

		} else if (cmdt == CommandType.LOGOUT_COMMAND) {

			newCommand = cmdFactory.createLogoutCommand(parsedRest);

		} else if (cmdt == CommandType.RETRIEVE_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createRetrieveExperimentCommand(json, parsedRest);

		} else if (cmdt == CommandType.ADD_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createAddExperimentCommand(json, parsedRest);

		} else if (cmdt == CommandType.UPDATE_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createUpdateExperimentCommand(json, parsedRest);

		} else if (cmdt == CommandType.REMOVE_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createRemoveExperimentCommand(json, parsedRest);

		} else if (cmdt == CommandType.GET_FILE_FROM_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createGetFileFromExperimentCommand(json, parsedRest);

		} else if (cmdt == CommandType.ADD_FILE_TO_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createAddFileToExperimentCommand(json, parsedRest);

		} else if (cmdt == CommandType.UPDATE_FILE_IN_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createUpdateFileInExperimentCommand(json, parsedRest);

		} else if (cmdt == CommandType.DELETE_FILE_FROM_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createDeleteFileFromExperimentCommand(json, parsedRest);

		} else if (cmdt == CommandType.SEARCH_FOR_EXPERIMENTS_COMMAND) {

			newCommand = cmdFactory.createSearchForExperimentCommand(json, parsedRest);

		} else if (cmdt == CommandType.UPDATE_USER_COMMAND) {

			newCommand = cmdFactory.createUpdateUserCommand(json, parsedRest);

		} else if (cmdt == CommandType.DELETE_USER_COMMAND) {

			newCommand = cmdFactory.createDeleteUserCommand(json, parsedRest);

		} else if (cmdt == CommandType.PROCESS_COMMAND) {

			newCommand = cmdFactory.createConvertRawToProfileCommand(json, parsedRest, uuid);

		} else if (cmdt == CommandType.GET_ANNOTATION_INFORMATION_COMMAND) {

			newCommand = cmdFactory.createGetAnnotationInformationCommand(json, parsedRest);

		} else if (cmdt == CommandType.ADD_ANNOTATION_FIELD_COMMAND) {

			newCommand = cmdFactory.createAddAnnotationFieldCommand(json, parsedRest);

		} else if (cmdt == CommandType.ADD_ANNOTATION_VALUE_COMMAND) {

			newCommand = cmdFactory.createAddAnnotationValueCommand(json, parsedRest);

		} else if (cmdt == CommandType.REMOVE_ANNOTATION_FIELD_COMMAND) {

			newCommand = cmdFactory.createRemoveAnnotationFieldCommand(json, parsedRest);

		} else if (cmdt == CommandType.GET_ANNOTATION_PRIVILEGES_COMMAND) {

			newCommand = cmdFactory.createGetAnnotationPrivilegesCommand(json, parsedRest);

		} else if (cmdt == CommandType.UPDATE_ANNOTATION_PRIVILEGES_COMMAND) {

			newCommand = cmdFactory.createUpdateAnnotationPrivilegesCommand(json, parsedRest);

		}

		return newCommand;

	}

	/**
	 * Method used to split a RESTful-header into smaller parts
	 * and return them in a String array.
	 * @param RESTful-header
	 * @return a String array with RESTful-header parts.
	 */
	public String[] parseRest(String restful) {

		String[] split = restful.split("/");
		String[] parsed = new String[split.length];

		for(int i = 0; i < split.length-1; i++) {

			parsed[i] = split[i+1];

		}

		return parsed;

	}

}


