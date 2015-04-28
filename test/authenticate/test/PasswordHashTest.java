/**
 * @author dv12rrt
 */

package authenticate.test;

import authentication.PasswordHash;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * A test class made for testing of the functions in PasswordHash.
 */
public class PasswordHashTest {

    /**
     * @return If the hashing is the same for the same string.
     */
    @Test
    public void hashStringTestEquals() {

        assertEquals(PasswordHash.hashString("TEST"), (PasswordHash.hashString("TEST")));
    }

    /**
     * @return If the hashing is different for different strings.
     */
    @Test
    public void hashStringTestNotEquals() {

        assertNotEquals(PasswordHash.hashString("TEST"), (PasswordHash.hashString("TEST2")));
    }

    /**
     * @return If the hashing returns null if a empty string is given.
     */
    @Test
    public void hashStringTestEmpty() {

        assertNull(PasswordHash.hashString(""));
    }
}
