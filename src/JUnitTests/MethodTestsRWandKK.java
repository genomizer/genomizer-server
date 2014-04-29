package JUnitTests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import databaseAccessor.DatabaseAccessor;

public class MethodTestsRWandKK {
    public static String testUser = "testUser_jhasdfv";
    public static String testPassword = "secret";
    public static String testRole = "admin";

    public static DatabaseAccessor dbac;

    @BeforeClass
    public static void setup() throws Exception {

        String username = "c5dv151_vt14";
        String password = "shielohh";
        String host = "postgres";
        String database = "c5dv151_vt14";


        // Ruaridh's DB Info (Comment out when at school)
//        String username = "genomizer_prog";
//        String password = "secret";
//        String host = "localhost";
//        String database = "genomizerdb";

        dbac = new DatabaseAccessor(username, password, host, database);
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException {
        dbac.close();
    }

    @Test
    public void shouldBeAbleToConnectToDB() throws Exception {

        assertTrue(dbac.isConnected());
    }

    @Test
    public void shouldBeAbleToAddANewUser() throws Exception {

        ArrayList<String> users = dbac.getUsers();

        assertFalse(users.contains(testUser));

        dbac.addUser(testUser, testPassword, testRole);

        users = dbac.getUsers();
        assertTrue(users.contains(testUser));
    }

    @Test
    public void shouldBeAbleToDeleteUser() throws Exception {

        dbac.deleteUser(testUser);

        ArrayList<String> users = dbac.getUsers();
        assertFalse(users.contains(testUser));
    }

}
