package JUnitTests;

import static org.junit.Assert.*;

import java.io.IOException;
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
    public static String testNewPassword = "secret2";
    public static String testRole = "admin";
    public static String testNewRole = "admin2";

    public static String testAnnotationLabel = "annotation_label_1234rwt";
    public static ArrayList<String> testChoices = new ArrayList<String>();
    public static String testChoice = "test_choice_1234rwt";


    public static DatabaseAccessor dbac;

    @BeforeClass
    public static void setup() throws Exception {

        String username = "c5dv151_vt14";
        String password = "shielohh";
        String host = "postgres";
        String database = "c5dv151_vt14";

        testChoices.add(testChoice);
        testChoices.add(testChoice + "2");


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
        dbac.deleteUser(testUser);
    }

    @Test
    public void shouldBeAbleToDeleteUser() throws Exception {

        dbac.addUser(testUser, testPassword, testRole);

        ArrayList<String> users = dbac.getUsers();
        assertTrue(users.contains(testUser));

        dbac.deleteUser(testUser);

        users = dbac.getUsers();
        assertFalse(users.contains(testUser));
    }

    @Test
    public void shouldBeAbleToGetPassword() throws Exception {

        dbac.addUser(testUser, testPassword, testRole);

        String password = dbac.getPassword(testUser);
        assertEquals(testPassword, password);
        dbac.deleteUser(testUser);
    }

    @Test
    public void shouldBeAbleToResetPassword() throws Exception {

        dbac.addUser(testUser, testPassword, testRole);

        String pass = dbac.getPassword(testUser);
        assertEquals(testPassword, pass);

        int res = dbac.resetPassword(testUser, testNewPassword);
        assertEquals(1, res); // Check one tuple was updated

        pass = dbac.getPassword(testUser);
        assertEquals(testNewPassword, pass);
        dbac.deleteUser(testUser);
    }

    @Test
    public void shouldBeAbleToSetUserPermissions() throws Exception {

        dbac.addUser(testUser, testPassword, testRole);

        assertEquals(testRole, dbac.getRole(testUser));

        dbac.setRole(testUser, testNewRole);

        assertEquals(testNewRole, dbac.getRole(testUser));

        dbac.deleteUser(testUser);
    }

    @Test
    public void shouldBeAbleToAddFreeTextAnnotaion() throws Exception {

        dbac.addFreeTextAnnotation(testAnnotationLabel);

        Map<String, String> annotations = dbac.getAnnotations();
        assertEquals("FreeText", annotations.get(testAnnotationLabel));

        dbac.deleteAnnotation(testAnnotationLabel);
    }

    @Test
    public void shouldBeAbleToDeleteFreeTextAnnotaion() throws Exception {

        dbac.addFreeTextAnnotation(testAnnotationLabel);
        Map<String, String> annotations = dbac.getAnnotations();
        assertTrue(annotations.containsKey(testAnnotationLabel));

        dbac.deleteAnnotation(testAnnotationLabel);

        annotations = dbac.getAnnotations();
        assertFalse(annotations.containsKey(testAnnotationLabel));
    }

    @Test
    public void shouldBeAbleToAddDropDownAnnotation() throws Exception {

        dbac.addDropDownAnnotation(testAnnotationLabel, testChoices);
        Map<String, String> annotations = dbac.getAnnotations();
        assertEquals("DropDown", annotations.get(testAnnotationLabel));
        dbac.deleteAnnotation(testAnnotationLabel);
    }

    @Test
    public void shouldBeAbleToDeleteDropDownAnnotation() throws Exception {

        dbac.addDropDownAnnotation(testAnnotationLabel, testChoices);
        Map<String, String> annotations = dbac.getAnnotations();
        assertEquals("DropDown", annotations.get(testAnnotationLabel));

        assertEquals(1, dbac.deleteAnnotation(testAnnotationLabel));
        annotations = dbac.getAnnotations();
        assertFalse(annotations.containsKey(testAnnotationLabel));
    }

    @Test(expected = IOException.class)
    public void shouldNotAddDropDownAnnotationWithNoChoices() throws Exception {

        String label = "drop_down_annotation_label";
        ArrayList<String> choices = new ArrayList<String>();

        dbac.addDropDownAnnotation(label, choices);
    }

    @Test
    public void shouldHandleDropDownAnnotationWithDuplicateChoices() throws Exception {

    }

    @Test
    public void shouldHaveChoicesForDropDownAnnotation() throws Exception {
        dbac.addDropDownAnnotation(testAnnotationLabel, testChoices);
        ArrayList<String> dropDownStrings= dbac.getDropDownAnnotations(testAnnotationLabel);

        assertTrue(dropDownStrings.contains(testChoices.get(0)));
        assertTrue(dropDownStrings.contains(testChoices.get(1)));
        assertEquals(2, dropDownStrings.size());
        dbac.deleteAnnotation(testAnnotationLabel);
    }




}






















