package command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public abstract class Response {
	protected int code;

	public String getBody() {

		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
		String jsonString = gson.toJson(this);
		return jsonString + "\n";
	}

	public int getCode() {
		return code;
	}

}
