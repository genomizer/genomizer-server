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
        Authenticate.updateActiveUser("uuid", "username");
        assertNotNull(Authenticate.updateActiveUser("uuid", "username"));
    }
    /**
     * @return If the assigned user excists or not.
     */
    @Test
    public void userExistsTest(){
        String uuid = Authenticate.updateActiveUser("uuid", "username");
        assertTrue(Authenticate.idExists(uuid));
    }
    /**
     * @return If a ID get retrieved.
     */
    @Test
    public void getID(){
        String uuid = Authenticate.updateActiveUser("uuid", "username");
        assertTrue(Authenticate.idExists(uuid));
    }
    /**
     * @return If a ID exists.
     */
    @Test
    public void idExists(){
        String uuid = Authenticate.updateActiveUser("uuid", "username");
        assertTrue(Authenticate.idExists(uuid));
    }
    /**
     * @return If a user can get retrieved for null.
     */
    @Test
    public void getUsernameTest(){
        String uuid = Authenticate.updateActiveUser(null, "username");
        assertNotNull(Authenticate.getUsernameByID(uuid));
    }
    /**
     * @return If a user can get retrieved for a specific uuid.
     */
    @Test
    public void getUsernameTest2(){
        Authenticate.updateActiveUser("uuid", "username");
        assertNotNull(Authenticate.getUsernameByID("uuid"));
    }
}
