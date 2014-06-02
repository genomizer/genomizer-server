package command;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

/**
 * Class that is used to be able to delete annotation info.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class DeleteAnnotationInfo {
	/* All attributes in this class are
	 * serialized with a JSON string.
	 */

	@Expose
	public String name;

	@Expose
	public ArrayList<String> values = new ArrayList<String>();

	/**
	 * Method used to get the name.
	 *
	 * @return the name
	 */
	public String getName() {

		return name;

	}

	/**
	 * Method used to get the values.
	 *
	 * @return ArrayList with all the values.
	 */
	public ArrayList<String> getValues() {

		return values;

	}

}
