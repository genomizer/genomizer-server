/**
 * @author dv12rrt
 */

package authenticate.test;

import authentication.LoginAttempt;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * A test class made for testing of the functions in LoginAttempt.
 */
public class LoginAttemptTest {

    private LoginAttempt attempt;

    @Before
    public void setup() {

        this.attempt = new LoginAttempt(true, "8888", "Error");
    }


    /**
     * @return If wasSuccesful returns the expected value.
     */
    @Test
    public void  wasSuccessful(){

        assertEquals(attempt.wasSuccessful(), true);
    }

    /**
     * @return If getUUID returns the expected value.
     */
    @Test
    public void  getUUIDTest(){

        assertEquals(attempt.getUUID(), "8888");
    }

    /**
     * @return If getErrorMessage returns the expected value.
     */
    @Test
    public void  getErrorMessageTest(){

        assertEquals(attempt.getErrorMessage(), "Error");
    }

}

