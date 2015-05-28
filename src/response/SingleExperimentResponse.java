package response;

import com.google.gson.*;
import database.containers.Experiment;
import database.containers.FileTuple;

/**
 * Class that represents the response when returning the experiment information.
 *
 * @author Business Logic 2015
 * @version 1.1
 */
public class SingleExperimentResponse extends Response {

	private JsonObject jsonObj;

	/**
	 * Creator for the response which contains the experiment and all the
	 * associated information and files.
	 * @param experiment containing files and annotations
	 */
	public SingleExperimentResponse(Experiment experiment) {
		this.code = HttpStatusCode.OK;

		//Creates a JsonObject and adds the information as jsonArrays
		jsonObj = new JsonObject();
		jsonObj.addProperty("name", experiment.getID());

		//Creates a jsonArray from the FileTuple list with FileInformation
		// objects
		JsonArray fileArray = new JsonArray();
		for (FileTuple ft: experiment.getFiles()) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();
			FileInformation fileInfo = new FileInformation(ft);
			JsonElement fileJson = gson.toJsonTree(fileInfo);
			fileArray.add(fileJson);
		}
		jsonObj.add("files", fileArray);

		//Creates an jsonArray from the annotations map with name and value
		JsonArray annotationArray = new JsonArray();
		for (String key: experiment.getAnnotations().keySet()) {
			JsonObject annotation = new JsonObject();
			annotation.addProperty("name", key);
			annotation.addProperty("value", experiment.getAnnotations().get(key));
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
