package response;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

/**
 * Response to the annotation information
 */
public class AnnotationInformation {

	@Expose
	private String name;

	@Expose
	private ArrayList<String> values;

	@Expose
	private boolean forced;

	public AnnotationInformation(String name,
			ArrayList<String> values, boolean forced) {

		this.name = name;
		this.values = values;
		this.forced = forced;
	}

	public String getName() {
		return name;
	}

//	public int getType() {
//		return type;
//	}

	public ArrayList<String> getValues() {
		return values;
	}

	public boolean isForced() {

		return forced;
	}

	public String toString() {
		String returnString = "NAME: " + name + "\nFORCED: " + forced + "\nVALUES:\n";

		if(values != null) {
			for (String value : values) {
				returnString = returnString + "     " + value + "\n";
			}
		}


		return returnString;

	}


}
