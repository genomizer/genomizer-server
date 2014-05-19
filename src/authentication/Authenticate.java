package authentication;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

/**
 * Class used to authenticate users and privileges.
 *
 * @author
 * @version 1.0
 */
public class Authenticate {

	private static HashMap<String, String> activeUsersID = new HashMap<String,String>();

	/**
	 * method used to create a user identification.
	 *
	 * @param username to create the identification for.
	 *
	 * @return String with the identification.
	 */
	static public String createUserID(String username) {

		String uid = UUID.randomUUID().toString();

		return UUID.fromString(uid).toString();

	}

	/**
	 * Method used to handle logged in users.
	 *
	 * @param username to add as logged in.
	 * @param userID to add as logged in.
	 */
	static public void addUser(String username,String userID) {

		activeUsersID.put(userID, username);

	}

	/**
	 * Method used to check if user exists.
	 *
	 * @param username to check for.
	 * @return boolean depending on result.
	 */
	static public boolean userExists(String username) {

		return activeUsersID.containsValue(username);

	}

	/**
	 * Method used to get the id for a user.
	 *
	 * @param username to get the id for.
	 * @return the id representing the user.
	 */
	static public String getID(String username) {

		Iterator<String> keys = activeUsersID.keySet().iterator();
		String key = null;
		String temp_username;

		while(keys.hasNext()) {

			key = keys.next();
			temp_username = activeUsersID.get(key);

			if(temp_username.equals(username)) {

				return key;

			}
		}

		return null;

	}

	/**
	 * Method used to delete a user from the
	 * active list.
	 *
	 * @param id representing the user to remove.
	 */
	static public void deleteUser(String id) {

		activeUsersID.remove(id);

	}

	/**
	 * Method used to check if a specific user id exist.
	 *
	 * @param id to check for.
	 * @return boolean depending on result.
	 */
	static public boolean idExists(String id) {

		return activeUsersID.containsKey(id);

	}

	/**
	 * Method used to get a user name with help from id.
	 *
	 * @param userID to match against an active user.
	 * @return the user name matching the id as a string.
	 */
	static public String getUsername(String userID){

		return activeUsersID.get(userID);

	}

}