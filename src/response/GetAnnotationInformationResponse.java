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
 * @author
 * @version 1.0
 */
public class GetAnnotationInformationResponse extends Response {

	@Expose
	private ArrayList<AnnotationInformation> annotations;

	private JsonArray annotationsArray;

	/**
	 * Creator for the get annotation information response.
	 * @param code The return code of the response.
	 * @param annotations An ArrayList containing the annotations to return.
	 */
	public GetAnnotationInformationResponse(int code,
			ArrayList<AnnotationInformation> annotations) {

		this.code = code;
	    annotationsArray = new JsonArray();

	    for (AnnotationInformation annotation: annotations) {
	    	GsonBuilder builder = new GsonBuilder();
	    	Gson gson = builder.create();
	    	JsonElement annotationJson = gson.toJsonTree(annotation);
	    	annotationsArray.add(annotationJson);
	    }
	}

	/**
	 * Returns the return code and body of the response.
	 * @return The return code and body as a String.
	 */
	@Override
	public String getBody() {

		return annotationsArray.toString();
	}

}
