package response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class that represents the basic framework for the responses
 * @author
 */
public abstract class Response {

	protected int code;

	/**
	 * Creates a Json representation of this object
	 * @return The Json representation
	 */
	public String getBody() {

		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().create();
		String jsonString = gson.toJson(this);
		return jsonString + "\n";
	}

	/**
	 * Getter for the return code
	 * @return The return code of the response
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Returns the return code followed by the body of the response
	 * @return The response's content as a String
	 */
	public String toString(){
		return code + getBody();
	}

}
