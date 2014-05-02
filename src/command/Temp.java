package command;

import response.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


//TODO: Remove class.
public class Temp {

	public static void main(String args[]) {

			System.out.println("Makeing JSON.");

			//Create the builder.
		    final GsonBuilder builder = new GsonBuilder();
		    builder.excludeFieldsWithoutExposeAnnotation();
		    final Gson gson = builder.create();

		    //Create the class to convert to JSON and fill it.
		    CommandHandler cmdh = new CommandHandler();
		    CommandType cmdt = CommandType.LOGIN_COMMAND;

		    //Create JSON to initiate class with.
		    String json = "{\"username\": \"uname\", \"password\": \"pw\"}";

		    //Create restful.
		    String restful = "";

		    String uuid = "123";

		    //Create the COMMAND.
		    Response rsp = cmdh.doStuff(json, restful, uuid, cmdt);

		    if(rsp == null) {

		    	System.out.println("WORKING.");

		    } else {

		    	System.out.println("NOT WORKING");

		    }


		    //Create the JSON STRING from object and print it.

		    /*
		    final String json = dCom.createJson();
		    System.out.printf("Serialised: %s%n", json);
			*/

		    //Create a class from a JSON STRING then check if same as other class.
		    /*
		    final Command secondBCom = gson.fromJson(json, DownloadCommand.class);
		    System.out.printf("Same BookCommand: %s%n", dCom.equals(secondBCom));
		    System.out.printf("Serialised: %s%n", secondBCom.createJson());
		    */

		}


}
