package authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class PasswordHash
{

    public static String toSaltedSHA256Hash(String password) {
    	String salt = getSalt();
    	String salted_password = password + salt;
    	try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //Add password bytes to digest
            md.update(salted_password.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            System.err.println("ERROR WHEN CREATING HASH.\n" + e.getMessage());
        }
    	return null;
    }

	private static String getSalt() {
		return "genomizer";
	}
	
	public static void main(String args[]) {
	    String s = PasswordHash.toSaltedSHA256Hash("kalle");
	    System.out.println(s);
	}

}
