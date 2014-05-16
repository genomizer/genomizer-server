package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import authentication.Authenticate;

import response.Response;

public class ResponseLogger {

	private static HashMap<String, ArrayList<Response>> usermap = new HashMap<String,ArrayList<Response>>();

	public ResponseLogger(){

	}

	/**
	 * Method for logging a response for a specific user.
	 *
	 * @param username - The username of the user calling the process.
	 * @param response - The response to be logged.
	 * @return Returns true if the
	 */
	public static void log(String username, Response response) {

		if(!usermap.containsKey(username)){
			System.err.println("usermap did not contain: " + username);
			usermap.put(username, new ArrayList<Response>());
		}

		ArrayList<Response> tmp = usermap.get(username);
		tmp.add(response);
		System.err.println("put: " + username + ", tmpsize:" + tmp.size());
		usermap.put(username,tmp);


	}

	/**
	 * Method for getting a specific users log.
	 *
	 * @param username - The username of the user which log is to be received
	 * @return Returns an ArrayList<Response>  or null if the user has no log.
	 */
	public static ArrayList<Response> getUserLog(String username) {
		return usermap.get(username);
	}

	/**
	 * Method for printing a specific users log.
	 *
	 * @param username - The username of the user which log is to be printed
	 */
	public static void printUserLog(String username){
		System.err.println("printuserlog:" + username);
		if(usermap.containsKey(username)){
			System.err.println("---Printing " + username + " log---");

			int i = 0;
			for(Response r : usermap.get(username)){
				System.err.print("Log " + i + ": " + r);
				i++;
			}

		}else{
			System.err.println("User " + username + " does not exist in log");
		}

	}

	/**
	 * Method for printing the complete log.
	 */
	public static void printLog(){
		System.err.println("---Printing log--");
		for(String user : usermap.keySet()){
			System.err.println("User: " + user);
			for(Response r : usermap.get(user)){
				System.err.println(r);
			}
		}
	}

	/**
	 * Method for resetting the log.
	 */
	public static void reset() {
		usermap.clear();

	}

}
