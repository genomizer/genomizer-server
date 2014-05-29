package command;

import com.google.gson.annotations.Expose;

/**
 * Class used to store annotation.
 *
 * @author tfy09jnn, Hugo Källström
 * @version 1.1
 */
public class Annotation {

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
