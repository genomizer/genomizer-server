package command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Used to create and return different commands that
 * will be executed later by calling the execute() method.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class CommandFactory {

	private final GsonBuilder builder;
	private final Gson gson;

	/**
	 * Constructor that creates the gson builder.
	 */
	public CommandFactory() {

		//Create the builder.
	    builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    gson = builder.create();

	}

	/**
	 * Used to create the command needed for uploading files.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createUploadCommand(String json, String restful) {

		return null;

	}

	/**
	 * Used to create the command needed for searching the
	 * database.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createSearchCommand(String json, String restful) {

		return null;

	}

	/**
	 * Used to create the command needed for downloading files.
	 * @param json string to initiate class.
	 * @param restful tag to put into class.
	 * @return the actual command.
	 */
	public Command createDownloadCommand(String json, String restful) {

		//Create command with json.
		final Command downloadCmd = gson.fromJson(json, DownloadCommand.class);

		//Get info from restful.
		@SuppressWarnings("unused")
		String[] stuff;							//TODO: Placeholder. Remove later... initiates the command...
		downloadCmd.initiateJson(null);


		return downloadCmd;

	}

}