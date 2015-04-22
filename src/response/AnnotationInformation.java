package response;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

/**
 * Response containing the annotation information
 */
public class AnnotationInformation {

	@Expose
	private String name;

	@Expose
	private ArrayList<String> values;

	@Expose
	private boolean forced;

	/**
	 * Creates a response containing the information
	 * @param name Name of the annotation
	 * @param values Value of the annotation
	 * @param forced Whether the annotation is forced or not
	 */
	public AnnotationInformation(String name,
			ArrayList<String> values, boolean forced) {

		this.name = name;
		this.values = values;
		this.forced = forced;
	}

	/**
	 * Getter for the annotation name
	 * @return The name as a String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for the annotation value
	 * @return The value as a String
	 */
	public ArrayList<String> getValues() {
		return values;
	}

	/**
	 * Returns whether a annotation is forced or not
	 * @return true if forced, otherwise false
	 */
	public boolean isForced() {

		return forced;
	}

}
