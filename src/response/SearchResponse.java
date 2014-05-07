package response;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import database.Experiment;
import database.FileTuple;

public class SearchResponse extends Response {


	public SearchResponse(List<Experiment> searchResult) {
		code = 200;

		JsonArray experimentArray = new JsonArray();
		for (Experiment exp : searchResult) {
			JsonObject expJson = new JsonObject();
			expJson.addProperty("name", exp.getID());
			expJson.addProperty("created by", "");


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
			JsonArray annotJson = new JsonArray();
			for (String header : annotations.keySet()) {

			}


			experimentArray.add(expJson);
			System.out.println(toPrettyFormat(experimentArray.toString()));
		}


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
