package response;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Class that represents the response when adding a genome release.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddGenomeReleaseResponse extends Response {

	private JsonArray jsonArray = null;

	/**
	 * Constructor used to create the response.
	 *
	 * @param code to send as a response code.
	 * @param filePaths An ArrayList containing the paths to where the files
	 *                     should be saved
	 */
	public AddGenomeReleaseResponse(int code, ArrayList<String> filePaths) {

		this.code = code;
		Gson gson = new GsonBuilder().create();

		jsonArray = new JsonArray();
		for (String filePath : filePaths) {
			JsonElement element = gson.toJsonTree(filePath);

			JsonObject obj = new JsonObject();
			obj.add("URLupload", element);

			jsonArray.add(obj);
		}

	}

	/**
	 * Creates a Json representation of the response
	 */
	@Override
	public String getBody(){

		return jsonArray.toString();
	}

}