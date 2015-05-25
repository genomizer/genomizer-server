package authentication;


import com.sun.net.httpserver.HttpExchange;
import server.Debug;
import transfer.Util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;



/**
 * Class used to authenticate users and privileges.
 *
 * @author dv13jen
 * @version 1.0
 */
public class Authenticate {

	// These can be modified from the InteractiveUuidsRemover thread.
	private static ConcurrentHashMap<String, String> activeUsersID = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, Date> latestRequests = new ConcurrentHashMap<>();


	static public LoginAttempt login(String username, String password, String dbHash) {

		if(BCrypt.checkpw(password,dbHash))
			return new LoginAttempt(true, updateActiveUser(username), null);

		else
			return new LoginAttempt(false, null, "Incorrect username or password.");
	}
	
	/**
	 * Method used to update active users. The user is added to the list of
	 * active users or the user is updated.
	 *
	 * @param username to add or update.
	 */
	static public String updateActiveUser(String username) {

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
		latestRequests.put(uuid, new Date());

		return uuid;
	}

	/**
	 * Updates the date for which the user did the most recent request
	 * @param username The username of the user
	 */
	static public void updateLatestRequest(String username) {

		if(latestRequests.containsKey(username)) {
			latestRequests.put(username, new Date());
		}
	}


	/**
	 * Method which returns the uuid for an active user.
	 *
	 * @param username Username to get the uuid for.
	 * @return The uuid representing the user or null if that user could not be
	 * 			found.
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
	 * Returns whether the user is currently logged in.
	 * @param username Username to check
	 * @return true if the user is logged in, otherwise false.
	 */
	static public boolean isUserLoggedIn(String username) {

		return (getID(username) != null);
	}

	/**
	 * Method used to delete a user from the
	 * active list.
	 *
	 * @param id representing the user to remove.
	 */
	static public void deleteActiveUser(String id) {

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
	static public String getUsernameByID(String userID){

		return (userID == null ? "" : activeUsersID.get(userID));
	}

	/**
	 * Getter for the map containing the latest request times
	 * @return The latest request times as a concurrentHashMap
	 */
	public static ConcurrentHashMap<String, Date> getLatestRequestsMap() {

		return latestRequests;
	}

	/**
	 * Performs the authentication of the http request.
	 * @param exchange The http request to authenticate.
	 * @return The uuid of the caller or null if it could not be authenticated.
	 */
 	public static String performAuthentication(HttpExchange exchange) {
		String uuid = null;

		/* Get the value of the 'Authorization' header. */
		List<String> authHeader = exchange.getRequestHeaders().
				get("Authorization");
		if (authHeader != null)
			uuid = authHeader.get(0);

		/* Used for commands that send token in header instead*/
		if(uuid == null){

			// Get the value of the 'token' parameter.
			String uuid2;
			HashMap<String, String> reqParams = new HashMap<>();
			Util.parseURI(exchange.getRequestURI(), reqParams);

			if (reqParams.containsKey("token")) {
				uuid2 = reqParams.get("token");
				if (uuid2 != null) {
						uuid = uuid2;
				} else {
					Debug.log("Authentication header and token parameter "
							+ "values differ!");
					return null;
				}
			}
		}

		Debug.log("Trying to authenticate token " + uuid + "...");
		if (uuid != null && Authenticate.idExists(uuid)) {
			Debug.log("User " + Authenticate.getUsernameByID(uuid)
					+ " authenticated successfully.");
			Authenticate.updateLatestRequest(uuid);
			return uuid;
		}
		else {
			Debug.log("User could not be authenticated");
			return null;
		}
	}

}
