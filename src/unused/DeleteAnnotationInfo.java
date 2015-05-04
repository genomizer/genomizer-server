package unused;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.subClasses.UserMethods;
import response.Response;

/**
 * Class that is used to be able to delete annotation info.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */

public class DeleteAnnotationInfo extends Command{
	@Expose
	public String name;

	@Expose
	public ArrayList<String> values = new ArrayList<String>();

	/**
	 * Returns the name of the annotation.
	 * @return the name of the annotation.
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

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
	}

	@Override
	public Response execute() {
		return null;
	}

	@Override
	public void setFields(String uri, String username, UserMethods.UserType userType) {

	}
}
