package response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import command.ProcessStatus;
import server.ErrorLogger;

/**
 * Class that represents the response for the get process status
 */
public class GetProcessStatusResponse extends Response {

	private Collection<ProcessStatus> procStats;

	public GetProcessStatusResponse(Collection<ProcessStatus> procStats) {
		this.procStats = procStats;
		code = 200;
	}

	@Override
	public String getBody() {

		if (procStats == null) {
			ErrorLogger.log("SYSTEM", "GetProcessStatusResponse.getBody(): " +
					"Error getting process status");
			return "";
		}

		List<ProcessStatus> list = new ArrayList<>( procStats);

		Collections.sort( list );
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		JsonArray arr = new JsonArray();
		for (ProcessStatus p : list) {
			JsonElement elem = gson.toJsonTree(p, ProcessStatus.class);
			arr.add(elem);
		}

		return toPrettyFormat(arr.toString());
	}

    private static String toPrettyFormat(String jsonString)
    {
        JsonParser parser = new JsonParser();
        JsonArray json = parser.parse(jsonString).getAsJsonArray();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

		return gson.toJson(json);
    }

}
