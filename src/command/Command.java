package command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

//TODO: Make interface?
//TODO: Fix comments.

/**
 * Class used to represent a command in the software.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public abstract class Command {

	//Used to get the header for the respons.
	private String header;

	//Used to validate the class object.
	public abstract boolean validate();

	//Method used to run command.
	public abstract void execute();

	//Method used to get the header.
	public String getHeader() {

		return header;

	}

	//Method used to set header.
	public void setHeader(String header) {

		this.header = header;

		//Remove this line later, just for testing.
		tstPrintJSON();

	}

	//Remove this method later. Just for testing.
	public void tstPrintJSON() {

		//Create the builder.
	    final GsonBuilder builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    final Gson gson = builder.create();

	    System.out.println(gson.toJson(this));

	}


}
