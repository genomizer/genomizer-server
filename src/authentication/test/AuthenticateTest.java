/**
 * @author dv12rrt
 */

package authentication.test;

import authentication.Authenticate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * A test class made for testing of the functions in Authenticate.
 */
public class AuthenticateTest {

    private Authenticate authenticate;

    @Before
    public void setup() {

        this.authenticate = new Authenticate();
    }

    /**
     * @return If something is retrieved.
     */
    @Test
    public void getLatestRequestsMapTest() {

        assertNotNull(authenticate.getLatestRequestsMap());
    }

    /**
     * @return If the latest UUID added get retrieved.
     */
    @Test
    public void addUserTest() {

        authenticate.addUser("TEST");

        assertNotNull(authenticate.addUser("TEST"));
    }

    /**
     * @return If the assigned user excists or not.
     */
    @Test
    public void userExistsTest(){

        assertTrue(authenticate.userExists("TEST"));
    }

    /**
     * @return If a ID get retrieved.
     */
    @Test
    public void getID(){

        assertNotNull(authenticate.getID("TEST"));
    }

    /**
     * @return If a ID exists.
     */
    @Test
    public void idExists(){

        assertTrue(authenticate.idExists("TEST"));
    }

    /**
     * @return If a user can get retrieved.
     */
    @Test
    public void getUsernameTest(){

        assertNotNull(authenticate.getUsername("TEST"));
    }
}