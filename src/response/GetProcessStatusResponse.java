package response;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import command.Process;
import server.ErrorLogger;

/**
 * Class that represents the response for the get process status.
 *
 * @author
 * @version 1.0
 */
public class GetProcessStatusResponse extends Response {

	private LinkedList<command.Process> getProcessStatuses;


	/**
	 * Creator for the response. Always returns 200 as return code.
	 * @param getProcessStatus The process status to return.
	 */
	public GetProcessStatusResponse(LinkedList<Process> getProcessStatus) {

		this.getProcessStatuses = getProcessStatus;
		code = 200;
	}

	/**
	 * Creates a Json representation of the body
	 * @return The response body as a String
	 */
	@Override
	public String getBody() {

		if (getProcessStatuses.size() == 0) {
			ErrorLogger.log("SYSTEM", "GetProcessStatusResponse.getBody(): " +
					"Error getting process status");
			return "";
		}

		Collections.sort(getProcessStatuses);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		JsonArray arr = new JsonArray();
		for (Process p : getProcessStatuses) {
			JsonElement elem = gson.toJsonTree(p, Process.class);
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
