package response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Class that represents the response when getting the annotation information.
 *
 * @author Business Logic
 * @version 1.0
 */
public class AnnotationListResponse extends Response {

	@Expose
	ArrayList<AnnotationInformation> annotations;

	private JsonArray annotationsArray;

	/**
	 * Creator for the get annotation information response.
	 * @param annotations An ArrayList containing the annotations to return.
	 */
	public AnnotationListResponse(ArrayList<AnnotationInformation> annotations) {
		this.code = HttpStatusCode.OK;
	    annotationsArray = new JsonArray();

	    for (AnnotationInformation annotation: annotations) {
	    	GsonBuilder builder = new GsonBuilder();
	    	Gson gson = builder.disableHtmlEscaping().create();
	    	JsonElement annotationJson = gson.toJsonTree(annotation);
	    	annotationsArray.add(annotationJson);
	    }
	}

	/**
	 * Creates a Json representation of the body
	 * @return The response body as a String
	 */
	@Override
	public String getBody() {

		return annotationsArray.toString();
	}

}
