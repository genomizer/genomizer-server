package command;

import response.MinimalResponse;
import response.ProcessResponse;
import response.Response;
import response.StatusCode;
import server.WorkHandler;

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
	 * @param a JSON string.
	 * @param a RESTful-header.
	 * @param a enumeration that determines command type.
	 */
	public Response processNewCommand(String json, String restful, String username, CommandType cmdt) {

		//Get code from restful
		Command myCom = createCommand(json, restful, username, cmdt);
		System.out.println("rest: " + restful);

		if (myCom.validate()) {
			if(CommandType.PROCESS_COMMAND.equals(cmdt)){
				//add the heavy command to the queue, executed when the
				//command is at the head of the queue, return OK to tell the client
				heavyWorkThread.addWork(myCom);
				return new ProcessResponse(StatusCode.OK);
			}else{
				return myCom.execute();
			}
		} else {
			System.out.println("not valid");
			return new MinimalResponse(StatusCode.BAD_REQUEST);
		}
	}

	/**
	 * Method used to create command objects together with CommandFactory.
	 *
	 * @param a JSON string.
	 * @param a RESTful-header.
	 * @param a enumeration that determines command type.
	 * @return a Command of the correct type.
	 */
	private Command createCommand(String json, String restful, String username, CommandType cmdt) {

		Command newCommand = null;
		String parsedRest = parseRest(restful);

		if(cmdt == CommandType.LOGIN_COMMAND) {

			newCommand = cmdFactory.createLoginCommand(json);

		} else if (cmdt == CommandType.LOGOUT_COMMAND) {

			newCommand = cmdFactory.createLogoutCommand(username);

		} else if (cmdt == CommandType.RETRIEVE_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createRetrieveExperimentCommand(json, parsedRest);

		} else if (cmdt == CommandType.ADD_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createAddExperimentCommand(json);

		} else if (cmdt == CommandType.UPDATE_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createUpdateExperimentCommand(json, parsedRest);

		} else if (cmdt == CommandType.REMOVE_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createRemoveExperimentCommand(json, parsedRest);

		} else if (cmdt == CommandType.GET_FILE_FROM_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createGetFileFromExperimentCommand(json, parsedRest);

		} else if (cmdt == CommandType.ADD_FILE_TO_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createAddFileToExperimentCommand(json);

		} else if (cmdt == CommandType.UPDATE_FILE_IN_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createUpdateFileInExperimentCommand(json, parsedRest);

		} else if (cmdt == CommandType.DELETE_FILE_FROM_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createDeleteFileFromExperimentCommand(json, parsedRest);

		} else if (cmdt == CommandType.SEARCH_FOR_EXPERIMENTS_COMMAND) {

			newCommand = cmdFactory.createSearchForExperimentCommand(parsedRest);

		} else if (cmdt == CommandType.UPDATE_USER_COMMAND) {

			newCommand = cmdFactory.createUpdateUserCommand(json, parsedRest);

		} else if (cmdt == CommandType.DELETE_USER_COMMAND) {

			newCommand = cmdFactory.createDeleteUserCommand(json, parsedRest);

		} else if (cmdt == CommandType.PROCESS_COMMAND) {

			newCommand = cmdFactory.createProcessCommand(json, username);

		} else if (cmdt == CommandType.GET_ANNOTATION_INFORMATION_COMMAND) {

			newCommand = cmdFactory.createGetAnnotationInformationCommand(json);

		} else if (cmdt == CommandType.ADD_ANNOTATION_FIELD_COMMAND) {

			newCommand = cmdFactory.createAddAnnotationFieldCommand(json, parsedRest);

		} else if (cmdt == CommandType.ADD_ANNOTATION_VALUE_COMMAND) {

			newCommand = cmdFactory.createAddAnnotationValueCommand(json, parsedRest);

		} else if (cmdt == CommandType.REMOVE_ANNOTATION_FIELD_COMMAND) {

			newCommand = cmdFactory.createRemoveAnnotationFieldCommand(json, parsedRest);

		} else if (cmdt == CommandType.GET_ANNOTATION_PRIVILEGES_COMMAND) {

			newCommand = cmdFactory.createGetAnnotationPrivilegesCommand(json);

		} else if (cmdt == CommandType.UPDATE_ANNOTATION_PRIVILEGES_COMMAND) {

			newCommand = cmdFactory.createUpdateAnnotationPrivilegesCommand(json, parsedRest);

		}

		return newCommand;

	}

	/**
	 * Method used to split a RESTful-header into smaller parts
	 * and return them in a String array.
	 *
	 * @param RESTful-header
	 * @return a String array with RESTful-header parts.
	 */
	public String parseRest(String restful) {

		String[] split = restful.split("/");
		return split[split.length -1];

//		String[] parsed = new String[split.length - 2];

//		for(int i = 0; i < parsed.length; i++) {
//			parsed[i] = split[i+2];
//		}

//		return parsed[parsed.length - 1];

	}

}