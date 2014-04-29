package authentication;

import java.util.ArrayList;
import java.util.UUID;

public class Authenticate {

	private ArrayList<String> activeUsers;

	public Authenticate(){
		activeUsers=new ArrayList<String>();
	}

	static public String createUserID(String username){
		String uid =UUID.randomUUID().toString();
		return UUID.fromString(uid).toString();

	}

	public void addUser(String userID){
		activeUsers.add(userID);
	}

	public boolean checkUser(String userID){
		return activeUsers.contains(userID);
	}
}
