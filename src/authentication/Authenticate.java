package authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Authenticate {

	static private HashMap<String, String> activeUsersID = new HashMap<String,String>();

	public Authenticate(){
	//	activeUsersID=new ArrayList<String>();
	}

	static public String createUserID(String username){
		String uid =UUID.randomUUID().toString();
		return UUID.fromString(uid).toString();

	}

	static public void addUser(String username,String userID){
		activeUsersID.put(username, userID);
	}

	static public boolean userExists(String username){
		System.out.println(activeUsersID);
		return activeUsersID.containsKey(username);
		
	}
}