package JUnitTests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;


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
    public void shouldBeAbleToGetPassword() throws Exception {

        String password = dbac.getPassword(testUser);
        assertEquals(testPassword, password);
    }

    @Test
    public void shouldBeAbleToResetPassword() throws Exception {
        String newPassword = "newPass_fghaiouwgfrib237845";

        String pass = dbac.getPassword(testUser);
        assertEquals(testPassword, pass);

        int res = dbac.resetPassword(testUser, newPassword);
        assertEquals(1, res); // Check one tuple was updated

        pass = dbac.getPassword(testUser);
        assertEquals(newPassword, pass);
    }

    @Test
    public void shouldBeAbleToSetUserPermissions() throws Exception {
        String role = testRole + "new_dhdhdfh";
        dbac.setRole(testUser, role);

        assertEquals(dbac.getRole(testUser), role);
    }


    @Test
    public void shouldBeAbleToDeleteUser() throws Exception {

        dbac.deleteUser(testUser);

        ArrayList<String> users = dbac.getUsers();
        assertFalse(users.contains(testUser));
    }

    @Test
    public void shouldBeAbleToAddFreeTextAnnotaion() throws Exception {

        String label = "freetext_annotation_label";

        dbac.addFreeTextAnnotation(label);

        Map<String, String> annotations = dbac.getAnnotations();
        assertEquals("FreeText", annotations.get(label));
    }

    @Test
    public void shouldBeAbleToDeleteFreeTextAnnotaion() throws Exception {

        String label = "freetext_annotation_label";

        dbac.deleteAnnotation(label);

        Map<String, String> annotations = dbac.getAnnotations();
        assertFalse(annotations.containsKey(label));
    }

    @Test
    public void shouldBeAbleToAddDropDownAnnotation() throws SQLException {

        String label = "drop_down_annotation_label";
        ArrayList<String> choices = new ArrayList<String>();
        choices.add("choice1");
        choices.add("choice2");

        dbac.addDropDownAnnotation(label, choices);
        Map<String, String> annotations = dbac.getAnnotations();
        assertEquals("DropDown", annotations.get(label));
    }

    public void shouldHaveChoicesForDropDownAnnotation() throws Exception {
        String label = "drop_down_annotation_label";
        ArrayList<String> dropDownStrings= dbac.getDropDownAnnotations(label);

        assertTrue(dropDownStrings.contains("choice1"));
        assertTrue(dropDownStrings.contains("choice2"));
        assertEquals(2, dropDownStrings.size());
    }

    @Test
    public void shouldBeAbleToDeleteDropDownAnnotation() throws Exception {

        String label = "drop_down_annotation_label";
        ArrayList<String> choices = new ArrayList<String>();
        choices.add("choice1");
        choices.add("choice2");

        assertEquals(1, dbac.deleteAnnotation(label));
        Map<String, String> annotations = dbac.getAnnotations();
        assertFalse(annotations.containsKey(label));
    }


}























