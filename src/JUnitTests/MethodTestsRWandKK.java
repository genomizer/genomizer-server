package JUnitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import databaseAccessor.DatabaseAccessor;
import databaseAccessor.Experiment;
import databaseAccessor.FileTuple;

public class MethodTestsRWandKK {

    // test User
    public static String testUser = "testUser_jhasdfv";
    public static String testPassword = "secret";
    public static String testNewPassword = "secret2";
    public static String testRole = "admin";
    public static String testNewRole = "admin2";

    // test Annotation
    public static String testAnnotationLabel = "annotation_label_1234rwt";
    public static ArrayList<String> testChoices = new ArrayList<String>();
    public static String testChoice = "test_choice_1234rwt";
    public static String testFreeTextValue = "test_free_text_annotation_value";

    // test Experiment
    public static String testExpId = "test_experiment_id_hrhgqerg";

    // test File
    public static String path = "/TestPath/gkdbfalkfnvlankfl";
    public static String type = "raw";
    public static String metaData = "/TestPath/inputfile.fastq";
    public static String author = "Ruaridh";
    public static boolean isPrivate = false;


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

        dbac.deleteUser(testUser);
        dbac.deleteAnnotation(testAnnotationLabel);
        dbac.deleteFile(path);
        dbac.deleteExperiment(testExpId);
        dbac.deleteTag(testExpId, testAnnotationLabel);
    }

    @After
    public void tearDown() throws Exception {

        dbac.deleteUser(testUser);
        dbac.deleteAnnotation(testAnnotationLabel);
        dbac.deleteFile(path);
        dbac.deleteExperiment(testExpId);
        dbac.deleteTag(testExpId, testAnnotationLabel);
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

        Map<String, Integer> annotations = dbac.getAnnotations();
        assertEquals(DatabaseAccessor.FREETEXT, annotations.get(testAnnotationLabel));

        dbac.deleteAnnotation(testAnnotationLabel);
    }

    @Test
    public void shouldBeAbleToDeleteFreeTextAnnotaion() throws Exception {

        dbac.addFreeTextAnnotation(testAnnotationLabel);
        Map<String, Integer> annotations = dbac.getAnnotations();
        assertTrue(annotations.containsKey(testAnnotationLabel));

        dbac.deleteAnnotation(testAnnotationLabel);

        annotations = dbac.getAnnotations();
        assertFalse(annotations.containsKey(testAnnotationLabel));
    }

    @Test
    public void shouldBeAbleToAddDropDownAnnotation() throws Exception {

        dbac.addDropDownAnnotation(testAnnotationLabel, testChoices);
        Map<String, Integer> annotations = dbac.getAnnotations();
        assertEquals(DatabaseAccessor.DROPDOWN, annotations.get(testAnnotationLabel));
        dbac.deleteAnnotation(testAnnotationLabel);
    }

    @Test
    public void shouldBeAbleToDeleteDropDownAnnotation() throws Exception {

        dbac.addDropDownAnnotation(testAnnotationLabel, testChoices);
        Map<String, Integer> annotations = dbac.getAnnotations();
        assertEquals(DatabaseAccessor.DROPDOWN, annotations.get(testAnnotationLabel));

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

    @Test
    public void shouldBeAbleToGetChoicesForADropDownAttribute() throws Exception {
        dbac.addDropDownAnnotation(testAnnotationLabel, testChoices);

        List<String> choices = dbac.getChoices(testAnnotationLabel);
        assertTrue(choices.contains(testChoices.get(0)));
        assertTrue(choices.contains(testChoices.get(1)));
        assertEquals(2, choices.size());

        dbac.deleteAnnotation(testAnnotationLabel);
    }

    @Test
    public void shouldHandleGettingChoicesForFreeTextAnnotation() throws Exception {
        dbac.addFreeTextAnnotation(testAnnotationLabel);
        List<String> choices = dbac.getChoices(testAnnotationLabel);
        assertTrue(choices.isEmpty());

        dbac.deleteAnnotation(testAnnotationLabel);
    }

    @Test
    public void shouldBeAbleToAddExperiment() throws Exception {
        dbac.addExperiment(testExpId);
        assertTrue(dbac.hasExperiment(testExpId));
        dbac.deleteExperiment(testExpId);
    }

    @Test
    public void shouldBeAbleToDeleteExperiment() throws Exception {
        dbac.addExperiment(testExpId);
        assertTrue(dbac.hasExperiment(testExpId));
        dbac.deleteExperiment(testExpId);
        assertFalse(dbac.hasExperiment(testExpId));
    }

    @Test
    public void shouldBeAbleToTagExperimentFreeText() throws Exception {
        dbac.addExperiment(testExpId);
        dbac.addFreeTextAnnotation(testAnnotationLabel);
        int res = dbac.tagExperiment(testExpId, testAnnotationLabel, testFreeTextValue);
        assertEquals(1, res);
        dbac.deleteExperiment(testExpId);
        dbac.deleteAnnotation(testAnnotationLabel);
    }

    @Test
    public void shouldBeAbleToDeleteExperimentTagFreeText() throws Exception {
        dbac.addExperiment(testExpId);
        dbac.addFreeTextAnnotation(testAnnotationLabel);
        dbac.tagExperiment(testExpId, testAnnotationLabel, testFreeTextValue);

        int res = dbac.deleteTag(testExpId, testAnnotationLabel);
        assertEquals(1, res);
        dbac.deleteExperiment(testExpId);
    }

    @Test
    public void shouldBeAbleToTagExperimentDropDown() throws Exception {
        dbac.addExperiment(testExpId);
        dbac.addDropDownAnnotation(testAnnotationLabel, testChoices);
        int res = dbac.tagExperiment(testExpId, testAnnotationLabel, testChoice);
        assertEquals(1, res);
        dbac.deleteExperiment(testExpId);
        dbac.deleteAnnotation(testAnnotationLabel);
    }

    @Test(expected = IOException.class)
    public void shouldNotBeAbleToTagExperimentWithInvalidDropdownChoice() throws Exception {

        dbac.addExperiment(testExpId);
        dbac.addDropDownAnnotation(testAnnotationLabel, testChoices);
        dbac.tagExperiment(testExpId, testAnnotationLabel, testFreeTextValue);
    }

    /* Should addFile take a JSONObject as a parameter?*/
    @Test
    public void shouldBeAbleToAddAFile() throws Exception {
        dbac.addUser(testUser, testPassword, testRole);
        int res = dbac.addFile(path, type, metaData, author, testUser, isPrivate, null, null);
        assertEquals(1, res);
    }

    @Test
    public void shouldBeAbleToDeleteFile() throws Exception {
        dbac.addUser(testUser, testPassword, testRole);
        dbac.addFile(path, type, metaData, author, testUser, isPrivate, null, null);
        int res = dbac.deleteFile(path);
        assertEquals(1, res);
    }

    @Test(expected = SQLException.class)
    public void shouldNotBeAbleToDeleteAnExperimentIfThereExistsAFileReferencingIt() throws Exception {
        dbac.addExperiment(testExpId);
        dbac.addUser(testUser, testPassword, testRole);
        dbac.addFile(path, type, metaData, author, testUser, isPrivate, testExpId, null);
        dbac.deleteExperiment(testExpId);
        assertTrue(dbac.hasExperiment(testExpId));
    }

    @Test
    public void shouldBeAbleToSearchUsingExperimentID() throws Exception {
        dbac.addExperiment(testExpId);
        Experiment e = dbac.getExperiment(testExpId);
        assertEquals(e.getID(), testExpId);
    }

    @Test
    public void shouldReturnExperimentObjectContainingAnnotationsOnSearch() throws Exception {
        dbac.addExperiment(testExpId);
        dbac.addDropDownAnnotation(testAnnotationLabel, testChoices);
        dbac.tagExperiment(testExpId, testAnnotationLabel, testChoice);
        Experiment e = dbac.getExperiment(testExpId);
        assertTrue(e.getAnnotations().containsKey(testAnnotationLabel));
    }

    @Test
    public void shouldReturnExperimentObjectContainingFileTuplesOnSearch() throws Exception {
        dbac.addExperiment(testExpId);
        dbac.addFile(path, type, metaData, author, null, isPrivate, testExpId, null);
        Experiment e = dbac.getExperiment(testExpId);
        List<FileTuple> files = e.getFiles();
        assertEquals(1, files.size());
        assertEquals(path, files.get(0).path);
    }



}






















