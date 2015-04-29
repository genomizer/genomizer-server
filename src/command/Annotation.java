package command;

import com.google.gson.annotations.Expose;

/**
 * Class used to store annotation.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class Annotation {
	@Expose
	private String name = null;

	@Expose
	private String value = null;

	/**
	 * Empty constructor. Suggested when using Gson.
	 */
	public Annotation() {

	}

	/**
	 * Returns the name of the annotation.
	 * @return the name of the annotation.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the value of the annotation.
	 * @return the value of the annotation.
	 */
	public String getValue() {
		return value;
	}

}
