package authentication;


import com.sun.net.httpserver.HttpExchange;
import server.Debug;
import util.Util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Class used to authenticate users and privileges.
 *
 * @author dv13jen
 * @version 1.0
 */
public class Authenticate {
	// These can be modified from the InactiveUuidsRemover thread.
	private static ConcurrentHashMap<String, String> activeUsersID = new ConcurrentHashMap<>();

	private static ConcurrentHashMap<String, Date> latestRequests = new ConcurrentHashMap<>();

	private static final int MAXIMUM_LOGINS = 10;


	/**
	 * Creates a login attempt from the username and password and returns the
	 * result as a LoginAttempt.
	 * @param uuid The uuid for the user.
	 * @param username The username for the user.
	 * @param password The password for the user.
	 * @param dbHash The hash used in encrypting the password.
	 * @return A loginAttempt containing the information about the login.
	 */
	static public LoginAttempt login(String uuid, String username, String password, String dbHash) {

		if(BCrypt.checkpw(password,dbHash))
			return new LoginAttempt(true, updateActiveUser(uuid, username), null);

		else
			return new LoginAttempt(false, null, "Incorrect username or password.");
	}


	/**
	 * Method to update the active user. If the user isn't currently logged in,
	 * the user is added to the active users list.
	 * @param uuid The uuid of the requester.
	 * @param username The username of the requester.
	 * @return The correct uuid for the requester.
	 */
	static public String updateActiveUser(String uuid, String username) {

		if(!(uuid==null) && activeUsersID.containsValue(uuid)) {

			updateLatestRequest(uuid);
			return uuid;
		}

		uuid = UUID.randomUUID().toString();

		activeUsersID.put(uuid, username);
		latestRequests.put(uuid, new Date());

		cleanUpLogIns(username);

		return uuid;
	}


	/* Checks if there are too many logins for the username and removes the
	oldest one.*/
	static private void cleanUpLogIns(String username){

		int currentLogIns = 0;
		String uuid = null;
		Date date = null;

		for(String s : activeUsersID.keySet()){
			if (activeUsersID.get(s).equals(username)){
				currentLogIns ++;
				Date newDate = latestRequests.get(s);
				if (date == null || date.compareTo(newDate) < 0){
					date = newDate;
					uuid = s;
				}
			}
		}

		if (currentLogIns > MAXIMUM_LOGINS){
			deleteActiveUser(uuid);
		}

	}


	/**
	 * Updates the date for which the user did the most recent request
	 * @param uuid The uuid of the user
	 */
	static public void updateLatestRequest(String uuid) {

		if(latestRequests.containsKey(uuid)) {
			latestRequests.put(uuid, new Date());
		}
	}


	/**
	 * Method used to delete a user from the active list.
	 *
	 * @param uuid uuid for the user to remove.
	 */
	static public void deleteActiveUser(String uuid) {

		activeUsersID.remove(uuid);
		latestRequests.remove(uuid);
	}

	/**
	 * Method used to check if a specific user uuid exist.
	 *
	 * @param uuid Uuid to check for.
	 * @return true if the uuid exists, otherwise false.
	 */
	static public boolean idExists(String uuid) {

		return activeUsersID.containsKey(uuid);
	}

	/**
	 * Method used to get a user name with help from id.
	 *
	 * @param uuid to match against an active user.
	 * @return the user name matching the id as a string.
	 */
	static public String getUsernameByID(String uuid){

		String username = activeUsersID.get(uuid);
		return (username == null ? "" : username);
	}

	/**
	 * Getter for the map containing the latest request times
	 * @return The latest request times of the users as a concurrentHashMap
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
