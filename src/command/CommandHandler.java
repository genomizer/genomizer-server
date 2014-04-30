package command;

import response.Response;

//TODO: Fix comments.
//TODO: Check command types etc...

/**
 * Should be used to handle and create different commands with
 * JSON and restful.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class CommandHandler {

	//Threads


	private CommandFactory cmdFactory = new CommandFactory();

	/**
	 * Empty constructor.
	 */
	public CommandHandler() {

	}

	/**
	 * Method that starts the actual handling of JSON and restful
	 * and converts them into commands and runs them.
	 * @param json
	 * @param restful
	 * @param cmdt
	 */
	public Response doStuff(String json, String restful, String uuid, CommandType cmdt) {	//TODO: Rename this method.

		//Get code from restful //TODO: add parser code....
		Command myCom = createCommand(cmdt, json, restful);

		//TODO: What kind of work? returns respons.
		Response rsp = myCom.execute();

		//Return respons.
		return rsp;
	}

	/**
	 * Method used to create commands together with CommandFactory.
	 * @param cmdt
	 * @param json
	 * @param restful
	 * @return
	 */
	private Command createCommand(CommandType cmdt, String json, String restful) {

		Command newCommand = null;

		if(cmdt == CommandType.LOGIN_COMMAND) {

			newCommand = cmdFactory.createLoginCommand(json, restful);

		} else if (cmdt == CommandType.LOGOUT_COMMAND) {

			newCommand = cmdFactory.createLogoutCommand(restful);

		} else if (cmdt == CommandType.RETRIEVE_EXPERIMENT_COMMAND) {

			newCommand = cmdFactory.createRetrieveExperimentCommand(json, restful);

		} else if (cmdt == CommandType.ADD_EXPERIMENT_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.UPDATE_EXPERIMENT_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.REMOVE_EXPERIMENT_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.GET_FILE_FROM_EXPERIMENT_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.ADD_FILE_TO_EXPERIMENT_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.UPDATE_FILE_IN_EXPERIMENT_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.DELETE_FILE_FROM_EXPERIMENT_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.SEARCH_FOR_EXPERIMENT_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.CREATE_USER_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.UPDATE_USER_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.DELETE_USER_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.CONVERT_RAW_TO_PROFILE_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.GET_ANNOTATION_INFORMATION_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.ADD_ANNOTATION_FIELD_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.ADD_ANNOTATION_VALUE_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.REMOVE_ANNOTATION_FIELD_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.GET_ANNOTATION_PRIVILEGES_COMMAND) {

			newCommand = null;

		} else if (cmdt == CommandType.UPDATE_ANNOTATION_PRIVILEGES_COMMAND) {

			newCommand = null;

		}

		return newCommand;
	}

}


