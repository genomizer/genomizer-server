package response;

import com.google.gson.*;
import command.Process;
import server.ErrorLogger;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Class that represents the response for the get process status.
 *
 * @author Business Logic
 * @version 1.0
 */
public class ProcessStatusResponse extends Response {

	private LinkedList<command.Process> getProcessStatuses;


	/**
	 * Creator for the response. Always returns 200 as return code.
	 * @param getProcessStatus The process status to return.
	 */
	public ProcessStatusResponse(LinkedList<Process> getProcessStatus) {
		this.code = HttpStatusCode.OK;
		this.getProcessStatuses = getProcessStatus;
	}

	/**
	 * Creates a Json representation of the body
	 * @return The response body as a String
	 */
	@Override
	public String getBody() {

		if (getProcessStatuses.size() == 0) {
			ErrorLogger.log("SYSTEM", "There are no processes to retrieve.");
			return "[]";
		}
		Collections.sort(getProcessStatuses);
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

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

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

		return gson.toJson(json);
    }

}
