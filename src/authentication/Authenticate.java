package authentication;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import server.Debug;
import server.ErrorLogger;
import server.ServerSettings;

/**
 * Class used to authenticate users and privileges.
 *
 * @author dv13jen
 * @version 1.0
 */
public class Authenticate {

	private static HashMap<String, String> activeUsersID = new HashMap<String, String>();
	private static HashMap<String, Date> latestRequests = new HashMap<String, Date>();

	// TODO : Check user and user password instead of server password
	// Get user salt and hash from DB (manage user not found), need DB group to implement table and get method
	/*
	DatabaseAccessor db = null;
		try {
			db = initDB();
		} catch (SQLException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when initiating databaseAccessor. " + e.getMessage());
		} catch (IOException e)  {
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		}
		try {
			String[] salt&hash = db.getUserSaltAndHash(username);
		}catch (SQLException | IOException e) {
		return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when requesting user from database, user probably don't exist " + e.getMessage());
		}

	 */
	// if user not found -> return new LoginAttempt(false, null, "Incorrect user.")
	// Apply salt to password. String hash = PasswordHash.hashString(password + salt&hash[0]);
	// Check if new hash matches DB hash.
	// if hash.equals(salt&hash[1]) -> return new LoginAttempt(true, addUser(username), null);)
	// else -> return new LoginAttempt(false, null, "Wrong password.");

	static public LoginAttempt login(String username, String password) {
	    if(PasswordHash.toSaltedSHA256Hash(password).equals(ServerSettings.passwordHash)) {
			return new LoginAttempt(true, addUser(username), null);
	    }
		return new LoginAttempt(false, null, "Wrong password.");
	}

	public static HashMap<String, Date> getLatestRequestsMap() {
		return latestRequests;
	}

	/**
	 * Method used to handle logged in users.
	 *
	 * @param username to add as logged in.
	 */
	static public String addUser(String username) {

		if(activeUsersID.containsValue(username)) {
			Iterator<String> uuids = activeUsersID.keySet().iterator();
			String next_uuid = uuids.next();
			while(!activeUsersID.get(next_uuid).equals(username)) {
				next_uuid = uuids.next();
			}

			updateLatestRequest(next_uuid);
			return next_uuid;
		}

		String uuid = UUID.randomUUID().toString();

		activeUsersID.put(uuid, username);
		//updateLatestRequest(uuid);
		latestRequests.put(uuid, new Date());

		return uuid;

	}

	static public void updateLatestRequest(String uuid) {
		if(latestRequests.containsKey(uuid)) {
			latestRequests.put(uuid, new Date());
		}
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
		for (String key : activeUsersID.keySet()) {
			if(username.equals(activeUsersID.get(key))) {
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
		latestRequests.remove(id);
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
