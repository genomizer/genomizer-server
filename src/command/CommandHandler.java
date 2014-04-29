package command;

public class CommandHandler {

	//Threads

	public CommandHandler() {

	}

	public static final int LOGIN_COMMAND = 1;
	public void doStuff(String json, String restful, int code) {

		//Get code from restful //TODO: add parser code....
		int msgCode = 0;

		@SuppressWarnings("unused")
		Command myCom = createCommand(msgCode, json, restful);

		//Initiate command

		//Create respond

		//Return respond.

	}

	private Command createCommand(int msgCode, String json, String restful) {


		if(msgCode == 0) {

			//Create cmd 0

		} else if (msgCode == 1) {

			//Create cmd 1

		}

		return null;
	}

}