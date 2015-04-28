package authentication;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import server.ErrorLogger;
import server.ServerSettings;
import java.security.SecureRandom;

/**
 * Class hashes a string password and returns the hash.
 *
 * @author BusinessLogic 2015
 *
 */
public class PasswordHash
{
    private static SecureRandom random = new SecureRandom();


    /**
     * Hash a given string and return the result
     *
     * @param salted_password string password with given salt
     *
     * @return string hash result of the hashing
     */
    public static String hashString(String salted_password) {
        if (salted_password.equals(""))
            return null;
        try {

            /** Create MessageDigest instance for SHA-256 */
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            /** update digest with password bytes */
            md.update(salted_password.getBytes());
            /** Digest password */
            byte[] bytes = md.digest();
            /** Convert it to hexadecimal format */
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            /** Get complete hashed password in hex format (64 characters long string) */
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e)

        {
            ErrorLogger.log("SERVER", "ERROR WHEN CREATING HASH.\n" + e.getMessage());
            System.err.println("ERROR WHEN CREATING HASH.\n" + e.getMessage());
        }
        return null;
    }



    /**
     * Method generates a random salt to use on passwords
     *
     * @return 64 character long string salt (chars and integers mixed)
     */
    public static String getNewSalt() {

        String salt = new BigInteger(320, random).toString(32);

        return salt;
    }


}
