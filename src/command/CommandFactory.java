package command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class CommandFactory {

	private final GsonBuilder builder;
	private final Gson gson;

	public CommandFactory() {

		//Create the builder.
	    builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    gson = builder.create();

	}

	//Method to create commands.
	public Command createDownloadCommand(String json, String restful) {

		//Create command with json.
		final Command downloadCmd = gson.fromJson(json, DownloadCommand.class);

		//Get info from restful.
		String[] stuff;
		downloadCmd.initiateJson(null);


		return downloadCmd;
	}
}
