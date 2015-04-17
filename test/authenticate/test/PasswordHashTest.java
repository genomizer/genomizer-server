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

    @Before
    public void setup() {
    }

    /**
     * @return If the hashing is the same for the same string.
     */
    @Test
    public void toSaltedSHA256HashTestEquals() {

        assertEquals(PasswordHash.toSaltedSHA256Hash("TEST"), (PasswordHash.toSaltedSHA256Hash("TEST")));
    }

    /**
     * @return If the hashing is different for different strings.
     */
    @Test
    public void toSaltedSHA256HashTestNotEquals() {

        assertNotEquals(PasswordHash.toSaltedSHA256Hash("TEST"), (PasswordHash.toSaltedSHA256Hash("TEST2")));
    }

    /**
     * @return If the hashing returns null if a empty string is given.
     */
    @Test
    public void toSaltedSHA256HashTestEmpty() {

        assertNull(PasswordHash.toSaltedSHA256Hash(""));
    }
}
