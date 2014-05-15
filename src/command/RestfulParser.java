package command;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class used to pass a restful header and
 * split it up into proper parts.
 *
 * @author tfy09jnn
 * @version 1.0
 *
 */
public class RestfulParser {

	public static ArrayList<String> addRestful(String restful, int expectedLength) {

		ArrayList<String> rest = null;

		if(restful != null) {
			rest = new ArrayList<String>(Arrays.asList(restful.split("/")));;
			rest.remove(0);
			if(rest.size() != expectedLength) {
				rest = null;
			}

		}

		return rest;
	}

}



