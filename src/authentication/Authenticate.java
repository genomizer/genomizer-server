package authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
		activeUsersID.put(userID, username);
	}

	static public boolean userExists(String username){
		System.out.println(activeUsersID);
		return activeUsersID.containsValue(username);

	}

	static public String getID(String username) {
		Iterator<String> keys = activeUsersID.keySet().iterator();
		String key;
		while(keys.hasNext()) {
			key = activeUsersID.get(keys.next());
			if(key.equals(username)) {
				return key;
			}
		}
		return null;
	}

	static public void deleteUser(String id) {
		activeUsersID.remove(id);
	}

	static public boolean idExists(String id) {
		return activeUsersID.containsKey(id);
	}

	static public String getUsername(String userID){
		return activeUsersID.get(userID);
	}
}