package util;

import java.util.ArrayList;

/**
 * Class containing a list containing the users.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class UserList {

    public ArrayList<String> username;

    /**
     * Creates the arrayList for the user list.
     */
    public UserList() {
        username = new ArrayList<>();
    }

    /**
     * Checks if the specified user exists.
     * @param username Username to check for.
     * @return true if the user exist, otherwise false.
     */
    public boolean userExist(String username){
        return this.username.contains(username);
    }

    /**
     * Getter for the user list size.
     * @return The user list size as an int.
     */
    public int getSize(){
        return username.size();
    }

    /**
     * Returns the list containing the usernames.
     * @return The list containing the usernames.
     */
    public ArrayList<String> getUserList(){
        return username;
    }

    /**
     * Test method for printing the contents of the username list.
     */
    public void print(){

        System.out.print("Size: " + username.size() + "; ");
        for(String s : username){
            System.out.print(s + ", ");
        }

        System.out.print("\n");
    }

}
