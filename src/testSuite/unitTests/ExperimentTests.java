package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import database.DatabaseAccessor;
import database.Experiment;

public class ExperimentTests {

    private static DatabaseAccessor dbac;

    private static String testExpId = "testExpId1";

    private static String testLabelFT = "testLabelFT1";
    private static String testValueFT = "testValueFT1";
    private static String testLabelDD = "testLabelDD1";
    private static String testChoice = "testchoice";
    private static List<String> testChoices;

    @BeforeClass
    public static void setupTestCase() throws Exception {
        dbac = new DatabaseAccessor(SearchDatabaseTests.username, SearchDatabaseTests.password, SearchDatabaseTests.host,
                SearchDatabaseTests.database);
        testChoices = new ArrayList<String>();
        testChoices.add(testChoice);
        testChoices.add(testChoice + "2");
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException {
        dbac.close();
    }

    @Before
    public void setup() throws SQLException, IOException {
        dbac.addExperiment(testExpId);
        dbac.addFreeTextAnnotation(testLabelFT);
        dbac.addDropDownAnnotation(testLabelDD, testChoices);
    }

    @After
    public void teardown() throws SQLException {
        dbac.deleteExperiment(testExpId);
        dbac.deleteAnnotation(testLabelFT);
        dbac.deleteAnnotation(testLabelDD);
    }

    @Test
    public void shouldBeAbleToConnectToDB() throws Exception {
        assertTrue(dbac.isConnected());
    }

    @Test
    public void testGetDeleteGetAddExperiment() throws Exception {

        assertTrue(dbac.hasExperiment(testExpId));
        Experiment e = dbac.getExperiment(testExpId);
        assertEquals(testExpId, e.getID());

        dbac.deleteExperiment(testExpId);
        assertFalse(dbac.hasExperiment(testExpId));
        e = dbac.getExperiment(testExpId);
        assertNull(e);

        dbac.addExperiment(testExpId);
        assertTrue(dbac.hasExperiment(testExpId));
        e = dbac.getExperiment(testExpId);
        assertEquals(testExpId, e.getID());
    }

    @Test
    public void shouldBeAbleToAnnotateExperimentFreeText()
            throws Exception {

        int res = dbac.annotateExperiment(testExpId,
                testLabelFT, testValueFT);
        assertEquals(1, res);
        Experiment e = dbac.getExperiment(testExpId);
        assertEquals(testValueFT, e.getAnnotations().get(testLabelFT));

    }

    @Test
    public void shouldBeAbleToDeleteExperimentAnnotationFreeText()
            throws Exception {

        dbac.annotateExperiment(testExpId, testLabelFT,
                testValueFT);

        int res = dbac.removeExperimentAnnotation(testExpId,
                testLabelFT);
        assertEquals(1, res);
        Experiment e = dbac.getExperiment(testExpId);
        assertFalse(e.getAnnotations().containsKey(testLabelFT));
    }

    @Test
    public void shouldBeAbleToTagExperimentDropDown()
            throws Exception {

        int res = dbac.annotateExperiment(testExpId,
                testLabelDD, testChoice);
        assertEquals(1, res);
        Experiment e = dbac.getExperiment(testExpId);
        assertEquals(testChoice, e.getAnnotations().get(testLabelDD));
    }

    @Test(expected = IOException.class)
    public void shouldNotBeAbleToTagExperimentWithInvalidDropdownChoice()
            throws Exception {

        String invalidValue = testChoice + "_invalid";
        try {
            dbac.annotateExperiment(testExpId, testLabelDD, invalidValue);
        } catch (Exception e) {
            throw e;
        } finally {
            Experiment e = dbac.getExperiment(testExpId);
            assertFalse(e.getAnnotations().containsKey(testLabelDD));
        }
    }

    @Test
    public void shouldBeAbleToSearchUsingExperimentID()
            throws Exception {
        Experiment e = dbac.getExperiment(testExpId);
        assertEquals(testExpId, e.getID());
    }

    @Test
    public void shouldReturnExperimentObjectContainingAnnotationOnSearch()
            throws Exception {

        Experiment e = dbac.getExperiment(testExpId);
        assertFalse(e.getAnnotations().containsKey(testLabelFT));
        dbac.annotateExperiment(testExpId, testLabelFT, testValueFT);
        e = dbac.getExperiment(testExpId);
        assertTrue(e.getAnnotations().containsKey(testLabelFT));
    }

    @Test
    public void shouldReturnExperimentObjectContainingMultipleAnnotationsOnSearch()
            throws Exception {

        dbac.annotateExperiment(testExpId, testLabelDD, testChoice);

        dbac.annotateExperiment(testExpId, testLabelFT, testValueFT);

        Experiment e = dbac.getExperiment(testExpId);
        assertEquals(2, e.getAnnotations().size());
        assertTrue(e.getAnnotations().containsKey(testLabelFT));
        assertTrue(e.getAnnotations().containsKey(testLabelDD));

    }

    @Test
    public void changeFromOldLabelToNewLabel()
    		throws Exception{

    	String oldLabel = "AntiName";
    	String newLabel = "AName";

    	boolean changeSucceed = dbac.changeAnnotationLabel(oldLabel, newLabel);
    	assertTrue(changeSucceed);
    }

}
