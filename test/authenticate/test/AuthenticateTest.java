/**
 * @author dv12rrt
 */
package authenticate.test;
import authentication.Authenticate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
/**
 * A test class made for testing of the functions in Authenticate.
 */
public class AuthenticateTest {
    @Before
    public void setup() {

    }
    /**
     * @return If something is retrieved.
     */
    @Test
    public void getLatestRequestsMapTest() {
        assertNotNull(Authenticate.getLatestRequestsMap());
    }
    /**
     * @return If the latest UUID added get retrieved.
     */
    @Test
    public void addUserTest() {
        Authenticate.updateActiveUser("TEST");
        assertNotNull(Authenticate.updateActiveUser("TEST"));
    }
    /**
     * @return If the assigned user excists or not.
     */
    @Test
    public void userExistsTest(){
        String uuid = Authenticate.updateActiveUser("TEST");
        assertTrue(Authenticate.idExists(uuid));
    }
    /**
     * @return If a ID get retrieved.
     */
    @Test
    public void getID(){
        Authenticate.updateActiveUser("TEST");
        assertNotNull(Authenticate.getID("TEST"));
    }
    /**
     * @return If a ID exists.
     */
    @Test
    public void idExists(){
        String uuid = Authenticate.updateActiveUser("TEST");
        assertTrue(Authenticate.idExists(uuid));
    }
    /**
     * @return If a user can get retrieved.
     */
    @Test
    public void getUsernameTest(){
        String uuid = Authenticate.updateActiveUser("TEST");
        assertNotNull(Authenticate.getUsernameByID(uuid));
    }
}
