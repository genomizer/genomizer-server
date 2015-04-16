package response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class that represents the parent for the responses
 */
public abstract class Response {
	protected int code;

	public String getBody() {

		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().create();
		String jsonString = gson.toJson(this);
		return jsonString + "\n";
	}

	public int getCode() {
		return code;
	}

	public String toString(){
		return code + getBody();
	}

}
