/**
 * @author dv12rrt
 */

package authentication.test;

import authentication.PasswordHash;
import org.junit.Before;
import org.junit.Test;
import org.junit.*;

import java.security.MessageDigest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

/**
 * A test class made for testing of the functions in PasswordHash
 *
 */
public class PasswordHashTest {

    private PasswordHash hasher = new PasswordHash();

    @Before
    public void setup() {

        this.hasher = new PasswordHash();
    }

    /**
     * @return If the hashing is the same for the same string.
     */
    @Test
    public void toSaltedSHA256HashTestEquals() {

        assertEquals(hasher.toSaltedSHA256Hash("TEST"), (hasher.toSaltedSHA256Hash("TEST")));
    }

    /**
     * @return If the hashing is different for different strings.
     */
    @Test
    public void toSaltedSHA256HashTestNotEquals() {

        assertNotEquals(hasher.toSaltedSHA256Hash("TEST"), (hasher.toSaltedSHA256Hash("TEST2")));
    }

    /**
     * @return If the hashing returns null if a empty string is given.
     */
    @Test
    public void toSaltedSHA256HashTestEmpty() {

        assertNull(hasher.toSaltedSHA256Hash(""));
    }
}
