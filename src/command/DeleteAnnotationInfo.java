package command;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class DeleteAnnotationInfo {

	@Expose
	public String name;

	@Expose
	public ArrayList<String> values = new ArrayList<String>();


	public String getName() {
		return name;
	}

	public ArrayList<String> getValues() {
		return values;
	}
}
