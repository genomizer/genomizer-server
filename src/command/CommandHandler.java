package command;

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
	public Response doStuff(String json, String restful, CommandType cmdt) {	//TODO: Rename this method.

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

			//Create cmd
			newCommand = cmdFactory.createLoginCommand(json, restful);


		} else if (cmdt == CommandType.LOGOUT_COMMAND) {

			//Create cmd
			newCommand = cmdFactory.createLogoutCommand(restful);

		} else if (cmdt == CommandType.RETRIEVE_EXPERIMENT_COMMAND) {

			//Create cmd
			newCommand = cmdFactory.createRetrieveExperimentCommand(json, restful);

		} else if (cmdt == CommandType.EXPERIMENT_COMMAND) {

			//Create cmd
			newCommand = cmdFactory.createExperimentCommand(json, restful);

		} else if (cmdt == CommandType.FILE_COMMAND) {

			//Create cmd

		} else if (cmdt == CommandType.SEARCH_COMMAND) {

			//Create cmd
			newCommand = cmdFactory.createSearchCommand(json, restful);

		} else if (cmdt == CommandType.USER_COMMAND) {

			//Create cmd
			newCommand = cmdFactory.createUserCommand(json, restful);

		} else if (cmdt == CommandType.PROCESS_COMMAND) {

			//Create cmd
			newCommand = cmdFactory.createProcessCommand(json, restful);

		} else if (cmdt == CommandType.SYSADM_COMMAND) {

			//Create cmd
			newCommand = cmdFactory.createSysadmCommand(json, restful);

		}

		return newCommand;
	}

}