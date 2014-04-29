package command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Temp {

	public static void main(String args[]) {

			System.out.println("Makeing JSON.");

			//Create the builder.
		    final GsonBuilder builder = new GsonBuilder();
		    builder.excludeFieldsWithoutExposeAnnotation();
		    final Gson gson = builder.create();

		    //Create the class to convert to JSON and fill it.
		    final Command dCom = new DownloadCommand();
		    String [] values = new String[4];
		    values[0] = "A";
		    values[1] = "B";
		    values[2] = "C";
		    values[3] = "D";

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
