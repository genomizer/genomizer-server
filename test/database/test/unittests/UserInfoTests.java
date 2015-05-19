package database.test.unittests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.*;


import database.DatabaseAccessor;
import database.test.TestInitializer;

public class UserInfoTests {

    private static DatabaseAccessor dbac;

    private static String testUser = "testUser1";
    private static String testUser2 = "testJAWSER";

    private static String testHash = "66e863ca7262669e5bd71d21d3eab0356136bd8569c6984430209deb50c55a23";
    private static String testSalt = "5goh146chpisv949ehjjdepparq3a9vga4p114ovedf66k5c78e39vnhvfpbvi43";
    private static String testRole = "testRole1";
    private static String testFullName = "Testis Test";
    private static String testEmail = "test@cs.umu.se";

    private static String otherHash = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

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

        dbac.addUser(testUser, testHash, testSalt, testRole, testFullName,
                testEmail);
        dbac.addUser(testUser2, testHash, testSalt, testRole, testFullName,
                testEmail);
    }

    @After
    public void teardown() throws SQLException {

        dbac.deleteUser(testUser);
        dbac.deleteUser(testUser2);
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

        dbac.addUser(testUser, testHash, testSalt, testRole, testFullName, testEmail);
        users = dbac.getUsers();
        assertTrue(users.contains(testUser));
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
                "aaaaaa", "passwd", "asdsad", "Admin", "Aaaaaaa bbbbbb", "ha@bla.se");
    }

    @Test
    public void shouldBeAbleToGetSalt() throws SQLException, IOException {
        assertEquals(testSalt, dbac.getPasswordSalt(testUser2));
    }

    @Test
    public void shouldBeAbleToGetHash() throws SQLException, IOException {
        assertEquals(testHash,dbac.getPasswordHash(testUser2));
    }

    @Test
    public void shouldBeAbleToChangeHash() throws SQLException, IOException {
        dbac.resetPassword(testUser2, otherHash);
        assertEquals(otherHash,dbac.getPasswordHash(testUser2));
    }

    @Test
    public void shouldBeAbleToGetFullName() throws SQLException, IOException {
        assertEquals(testFullName,dbac.getUserFullName(testUser));
    }

    @Test
    public void shouldBeAbleToGetEmail() throws SQLException,  IOException {
        assertEquals(testEmail,dbac.getUserEmail(testUser));
    }

}