package database.test.unittests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;




import database.DatabaseAccessor;
import database.test.TestInitializer;

public class UserInfoTests {

    private static DatabaseAccessor dbac;

    private static String testUser = "testUser1";
    private static String testPassword = "testPassword1";
    private static String testRole = "testRole1";
    private static String testFullName = "Testis Test";
    private static String testEmail = "test@cs.umu.se";

    @BeforeClass
    public static void setupTestCase() throws Exception {

        dbac = new DatabaseAccessor(TestInitializer.username,
        		TestInitializer.password, TestInitializer.host,
        		TestInitializer.database);
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException {

        dbac.close();
    }

    @Before
    public void setup() throws SQLException, IOException {

        dbac.addUser(testUser, testPassword, testRole, testFullName,
        		testEmail);
    }

    @After
    public void teardown() throws SQLException {

        dbac.deleteUser(testUser);
    }

    @Test
    public void shouldBeAbleToConnectToDB() throws Exception {

        assertTrue(dbac.isConnected());
    }

    @Test
    public void testGetDeleteGetAddUser() throws Exception {

        List<String> users = dbac.getUsers();
        assertTrue(users.contains(testUser));

        dbac.deleteUser(testUser);
        users = dbac.getUsers();
        assertFalse(users.contains(testUser));

        dbac.addUser(testUser, testPassword, testRole, testFullName, testEmail);
        users = dbac.getUsers();
        assertTrue(users.contains(testUser));
    }

    @Test
    public void shouldBeAbleToGetPassword() throws Exception {

        String password = dbac.getPassword(testUser);
        assertEquals(testPassword, password);
    }

    @Test
    public void shouldBeAbleToResetPassword() throws Exception {

        String pass = dbac.getPassword(testUser);
        assertEquals(testPassword, pass);

        String newPass = testPassword + "_new";

        int res = dbac.resetPassword(testUser, newPass);
        assertEquals(1, res);

        pass = dbac.getPassword(testUser);
        assertEquals(newPass, pass);
    }

    @Test
    public void shouldBeAbleToSetUserPermissions() throws Exception {

        assertEquals(testRole, dbac.getRole(testUser));

        String newRole = testRole + "_new";
        dbac.setRole(testUser, newRole);
        assertEquals(newRole, dbac.getRole(testUser));

    }

    @Test(expected = SQLException.class)
    public void testBiggerThanMaxSize() throws SQLException, IOException{
    	dbac.addUser("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
    			"aaaaaa", "passwd", "Admin", "Aaaaaaa bbbbbb", null);
    }
}