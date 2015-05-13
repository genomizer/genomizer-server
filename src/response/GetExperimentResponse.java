package response;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import database.containers.FileTuple;

/**
 * Class that represents the response when returning the experiment information.
 *
 * @author Business Logic 2015
 * @version 1.1
 */
public class GetExperimentResponse extends Response {

	private JsonObject jsonObj;


	/**
	 * Creator for the response which contains the experiment and all the
	 * associated information and files.
	 * @param code The return code for the response.
	 * @param name A String containing the name of the experiment.
	 * @param annotations A Map containing the annotations for the experiment .
	 * @param list A list containing the files associated with the experiment.
	 */
	public GetExperimentResponse(int code, String name, Map<String, String> annotations, List<FileTuple> list) {

		this.code = code;

		//Creates a JsonObject and adds the information as jsonArrays
		jsonObj = new JsonObject();
		jsonObj.addProperty("name", name);

		//Creates a jsonArray from the FileTuple list with FileInformation
		// objects
		JsonArray fileArray = new JsonArray();
		for (FileTuple ft: list) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.setPrettyPrinting().create();
			FileInformation fileInfo = new FileInformation(ft);
			JsonElement fileJson = gson.toJsonTree(fileInfo);
			fileArray.add(fileJson);
		}
		jsonObj.add("files", fileArray);

		//Creates an jsonArray from the annotations map with name and value
		JsonArray annotationArray = new JsonArray();
		for (String key: annotations.keySet()) {
			JsonObject annotation = new JsonObject();
			annotation.addProperty("name", key);
			annotation.addProperty("value", annotations.get(key));
			annotationArray.add(annotation);
		}
		jsonObj.add("annotations", annotationArray);

	}

	/**
	 * Creates a Json representation of the body
	 * @return The response body as a String
	 */
	@Override
	public String getBody() {

		return jsonObj.toString();
	}
}
