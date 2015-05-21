package response;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import command.ProcessStatus;
import server.ErrorLogger;

/**
 * Class that represents the response for the get process status.
 *
 * @author
 * @version 1.0
 */
public class GetProcessStatusResponse extends Response {

	private LinkedList<ProcessStatus> processStatuses;


	/**
	 * Creator for the response. Always returns 200 as return code.
	 * @param processStatus The process status to return.
	 */
	public GetProcessStatusResponse(LinkedList<ProcessStatus> processStatus) {

		this.processStatuses = processStatus;
		code = 200;
	}

	/**
	 * Creates a Json representation of the body
	 * @return The response body as a String
	 */
	@Override
	public String getBody() {

		if (processStatuses.size() > 0) {
			ErrorLogger.log("SYSTEM", "GetProcessStatusResponse.getBody(): " +
					"Error getting process status");
			return "";
		}

		Collections.sort( processStatuses );
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		JsonArray arr = new JsonArray();
		for (ProcessStatus p : processStatuses) {
			JsonElement elem = gson.toJsonTree(p, ProcessStatus.class);
			arr.add(elem);
		}

		return toPrettyFormat(arr.toString());
	}

	//Parses the json string to a nice format for sending
    private String toPrettyFormat(String jsonString) {

        JsonParser parser = new JsonParser();
        JsonArray json = parser.parse(jsonString).getAsJsonArray();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

		return gson.toJson(json);
    }

}
