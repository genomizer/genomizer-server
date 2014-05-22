package response;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * Class that represents an actual response.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddGenomeReleaseResponse extends Response {

	private JsonArray jsonArray = null;

	/**
	 * Constructor used to initiate the command.
	 *
	 * @param code to send as a responsecode.
	 */
	public AddGenomeReleaseResponse(int code, ArrayList<String> filePaths) {

		this.code = code;
		Gson gson = new GsonBuilder().create();

		jsonArray = new JsonArray();
		for(int i = 0; i < filePaths.size(); i++) {
			JsonElement element = gson.toJsonTree(filePaths.get(i));
			jsonArray.add(element);
		}

	}

	/**
	 * Method used to get the JSON body.
	 */
	@Override
	public String getBody(){
		return jsonArray.toString();
	}

}