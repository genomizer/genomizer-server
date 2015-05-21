package response;

import com.google.gson.*;
import database.containers.Experiment;
import database.containers.FileTuple;

import java.util.List;
import java.util.Map;

/**
 * Class that represents the response for a search.
 *
 * @author
 * @version 1.0
 */
public class SearchResponse extends Response {

	private JsonArray experimentArray;


	/**
	 * Creator for the response.
	 * Always has 200 as return code.
	 * @param searchResult A List of experiments which are added to the
	 *                     response.
	 */
	public SearchResponse(List<Experiment> searchResult) {
		code = 200;

		//Adds each experiment to a json representation
		experimentArray = new JsonArray();
		for (Experiment exp : searchResult) {
			JsonObject expJson = new JsonObject();
			expJson.addProperty("name", exp.getID());

			//The FileTuples for the experiment
			JsonArray files = new JsonArray();
			for (FileTuple tup : exp.getFiles()) {
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.setPrettyPrinting().create();
				FileInformation fileInfo = new FileInformation(tup);
				JsonElement fileJson = gson.toJsonTree(fileInfo);
				files.add(fileJson);

			}
			expJson.add("files", files);

			//The annotations for the experiment
			Map<String,String> annotations = exp.getAnnotations();
			JsonArray annotationJsonArray = new JsonArray();
			int i = 0;
			for (String key : annotations.keySet()) {
				JsonObject annotationJson = new JsonObject();
				annotationJson.addProperty("id", i);
				annotationJson.addProperty("name", key);
				annotationJson.addProperty("value", annotations.get(key));
				annotationJsonArray.add(annotationJson);
				i++;
			}

			expJson.add("annotations", annotationJsonArray);

			experimentArray.add(expJson);
		}


	}

	/**
	 * Creates a Json representation of the body.
	 * @return The response body as a String.
	 */
	@Override
	public String getBody() {

		return toPrettyFormat(experimentArray.toString());
	}

	//Converts the Json to a nice format for sending
    private String toPrettyFormat(String jsonString) {

        JsonParser parser = new JsonParser();
        JsonArray json = parser.parse(jsonString).getAsJsonArray();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

		return gson.toJson(json);
    }

}
