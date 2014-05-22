package authentication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import server.DatabaseSettings;
import database.DatabaseAccessor;

/**
 * Class used to authenticate users and privileges.
 *
 * @author
 * @version 1.0
 */
public class Authenticate {

	private static HashMap<String, String> activeUsersID = new HashMap<String,String>();

	static public LoginAttempt login(String username, String password) {
		DatabaseAccessor db = null;
		try {
			db = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
		} catch (SQLException e) {
			return new LoginAttempt(false, null, "SQLException when initiating database accessor. " + e.getMessage());
		} catch (IOException e) {
			return new LoginAttempt(false, null, "IOException when initiating database accessor. " + e.getMessage());
		}

		String hashed_password = PasswordHash.toHash(password);
		String database_password = null;
		try {
			database_password = db.getPassword(username);
		} catch (SQLException e) {
			return new LoginAttempt(false, null, "SQLException when getting password. " + e.getMessage());
		}

		if(database_password == null) {
			return new LoginAttempt(false, null, "User not found.");
		}

		if(hashed_password == null) {
			return new LoginAttempt(false, null, "Could not hash password.");
		}

		if(database_password.equals(hashed_password)) {

			String user_uuid = addUser(username);
			return new LoginAttempt(true, user_uuid, null);
		}

		return new LoginAttempt(false, null, "Wrong password.");
	}

	/**
	 * Method used to handle logged in users.
	 *
	 * @param username to add as logged in.
	 * @param userID to add as logged in.
	 */
	static public String addUser(String username) {

		if(activeUsersID.containsValue(username)) {
			Iterator<String> uuids = activeUsersID.keySet().iterator();
			String next_uuid = uuids.next();
			while(!activeUsersID.get(next_uuid).equals(username)) {
				next_uuid = uuids.next();
			}

			return next_uuid;
		}

		String uuid = UUID.randomUUID().toString();

		activeUsersID.put(uuid, username);

		return uuid;

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