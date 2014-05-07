package response;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.Map.Entry;

import database.FileTuple;

public class getExperimentResponse extends Response {

	public getExperimentResponse(ArrayList<String> info, HashMap<String, String> annotations, ArrayList<FileTuple> files, int code) {
		this.code = code;

		JsonObject obj = new JsonObject();
		obj.addProperty("name", "testExp");
		obj.addProperty("createdBy", "hugga");
		JsonArray fileArray = new JsonArray();
		for (FileTuple ft: files) {
			
			fileArray.add(file);
		}
	}
}
