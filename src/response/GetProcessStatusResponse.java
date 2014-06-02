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

public class GetProcessStatusResponse extends Response {

	private Collection<ProcessStatus> procStats;

	public GetProcessStatusResponse(Collection<ProcessStatus> procStats) {
		this.procStats = procStats;
		code = 200;
	}

	@Override
	public String getBody() {
		List<ProcessStatus> list = new ArrayList<ProcessStatus>( procStats);

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
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }

}
