package command;

public class CommandHandler {

	//Threads

	public CommandHandler() {

	}

	public void doStuff(String json, String restful) {

		//Get code from restful
		int msgCode = 0;

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
