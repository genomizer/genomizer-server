package command;

import com.google.gson.annotations.Expose;

//TODO: Add set/getters.

/**
 * Class used to store annotation.
 * @author tfy09jnn, Hugo Källström
 * @version 1.1
 */
public class Annotation {

	@Expose
	private String id;

	@Expose
	private String name;

	@Expose
	private String value;

	/**
	 * Empty constructor.
	 */
	public Annotation() {

	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
