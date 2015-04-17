package response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Class that represents the response for get annotation information.
 */
public class GetAnnotationInformationResponse extends Response {

	@Expose
	ArrayList<AnnotationInformation> annotations;

	JsonArray annotationsArray;

	public GetAnnotationInformationResponse(int code,
			ArrayList<AnnotationInformation> annotations) {

		this.code = code;
	    annotationsArray = new JsonArray();

	    for (AnnotationInformation anno: annotations) {
	    	GsonBuilder builder = new GsonBuilder();
	    	Gson gson = builder.create();
	    	JsonElement annoJson = gson.toJsonTree(anno);
	    	annotationsArray.add(annoJson);
	    	//annotationsArray.toString();
	    }
	}

	@Override
	public String getBody() {
		return annotationsArray.toString();
	}

}
