package command;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class DeleteAnnotationInfo {

	@Expose
	public String id;

	@Expose
	public ArrayList<String> values = new ArrayList<String>();

	public String getId() {
		return id;
	}

	public ArrayList<String> getValues() {
		return values;
	}
}
