package command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class used to represent a command in the software.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public abstract class Command {

	//Used to validate the class object.
	public abstract boolean validate();

	//Initiate JSON. (Remove later)
	public abstract void initiateJson(String[] values);

	//Method used to run command.
	public abstract void execute();

	//Creates a JSON String.
	/*
	public String createJson() {

		//Create the builder.
	    final GsonBuilder builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    final Gson gson = builder.create();

		return gson.toJson(this);
	}
	*/
}
