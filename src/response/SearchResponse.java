package response;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import database.containers.Experiment;
import database.containers.FileTuple;

public class SearchResponse extends Response {

	JsonArray experimentArray;

	public SearchResponse(List<Experiment> searchResult) {
		code = 200;

		experimentArray = new JsonArray();
		for (Experiment exp : searchResult) {
			JsonObject expJson = new JsonObject();
			expJson.addProperty("name", exp.getID());

			JsonArray files = new JsonArray();
			for (FileTuple tup : exp.getFiles()) {
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.setPrettyPrinting().create();
				FileInformation fileInfo = new FileInformation(tup);
				JsonElement fileJson = gson.toJsonTree(fileInfo);
				files.add(fileJson);

			}
			expJson.add("files", files);

			Map<String,String> annotations = exp.getAnnotations();
			JsonArray annotJsonArray = new JsonArray();
			int i = 0;
			for (String key : annotations.keySet()) {
				JsonObject annotJson = new JsonObject();
				annotJson.addProperty("id", i);
				annotJson.addProperty("name", key);
				annotJson.addProperty("value", annotations.get(key));
				annotJsonArray.add(annotJson);
				i++;
			}

			expJson.add("annotations", annotJsonArray);

			experimentArray.add(expJson);
		}


	}

	@Override
	public String getBody() {
		return toPrettyFormat(experimentArray.toString());
	}

    public static String toPrettyFormat(String jsonString)
    {
        JsonParser parser = new JsonParser();
        JsonArray json = parser.parse(jsonString).getAsJsonArray();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }

}
