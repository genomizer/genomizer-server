package command;

import com.google.gson.annotations.Expose;

/**
 * Class used to store annotation.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class Annotation {
	/* All attributes with @Expose are serialized in
	 * with a JSON string.
	 */

	@Expose
	private String name;

	@Expose
	private String value;

	/**
	 * Empty constructor.
	 */
	public Annotation() {

	}

	/**
	 * Method used to get the name.
	 *
	 * @return the name.
	 */
	public String getName() {

		return name;

	}

	/**
	 * Method used to get the value.
	 *
	 * @return the value.
	 */
	public String getValue() {

		return value;

	}

}
