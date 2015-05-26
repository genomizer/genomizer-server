package response;

import com.google.gson.*;

import java.util.ArrayList;

/**
 * Class that represents the response when adding a genome release.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class FilePathListResponse extends Response {

	private JsonArray jsonArray = null;

	/**
	 * Constructor for the response.
	 *
	 * @param filePaths An ArrayList containing the paths to where the files
	 *                     should be saved
	 */
	public FilePathListResponse(ArrayList<String> filePaths) {
		this.code = HttpStatusCode.OK;
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
	 * Creates a Json representation of the body
	 * @return The response body as a String
	 */
	@Override
	public String getBody(){

		return jsonArray.toString();
	}

}