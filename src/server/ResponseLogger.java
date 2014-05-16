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

	public static boolean log(String username, Response r) {

		if(!usermap.containsKey(username)){
			System.err.println("usermap did not contain: " + username);
			usermap.put(username, new ArrayList<Response>());
		}

		ArrayList<Response> tmp = usermap.get(username);
		tmp.add(r);
		System.err.println("put: " + username + ", tmpsize:" + tmp.size());
		usermap.put(username,tmp);
		return true;

	}

	public static ArrayList<Response> getUserLog(String username) {
		return usermap.get(username);
	}

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

	public static void printLog(){
		System.err.println("---Printing log--");
		for(String user : usermap.keySet()){
			System.err.println("User: " + user);
			for(Response r : usermap.get(user)){
				System.err.println(r);
			}
		}
	}

	public static void reset() {
		usermap.clear();

	}

}
