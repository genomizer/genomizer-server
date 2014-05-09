package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import database.DatabaseAccessor;

public class AddAnnotationPossibilitiesTests {
    
    private static DatabaseAccessor dbac;
    
    private static String testLabelFT = "testLabel1";
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
        dbac.addFreeTextAnnotation(testLabelFT, null, true);
        dbac.addDropDownAnnotation(testLabelDD, testChoices, 0, false);
    }
    
    @After
    public void teardown() throws SQLException {
        dbac.deleteAnnotation(testLabelFT);
        dbac.deleteAnnotation(testLabelDD);
    }
    
    @Test
    public void shouldBeAbleToConnectToDB() throws Exception {
        assertTrue(dbac.isConnected());
    }
    
    @Test
    public void testGetDeleteGetAddFTAnnotation() throws Exception {

        Map<String, Integer> annotations = dbac.getAnnotations();
        assertTrue(annotations.containsKey(testLabelFT));
        assertEquals(DatabaseAccessor.FREETEXT, annotations.get(testLabelFT));
        
        dbac.deleteAnnotation(testLabelFT);
        annotations = dbac.getAnnotations();
        assertFalse(annotations.containsKey(testLabelFT));
        
        dbac.addFreeTextAnnotation(testLabelFT, null, true);
        annotations = dbac.getAnnotations();
        assertTrue(annotations.containsKey(testLabelFT));
        assertEquals(DatabaseAccessor.FREETEXT, annotations.get(testLabelFT));
    }
    
    @Test
    public void testGetDeleteGetAddDDAnnotation()
            throws Exception {

        Map<String, Integer> annotations = dbac.getAnnotations();
        assertTrue(annotations.containsKey(testLabelDD));
        assertEquals(DatabaseAccessor.DROPDOWN, annotations.get(testLabelDD));
        
        dbac.deleteAnnotation(testLabelDD);
        annotations = dbac.getAnnotations();
        assertFalse(annotations.containsKey(testLabelDD));
        
        dbac.addDropDownAnnotation(testLabelDD, testChoices, 0, false);
        annotations = dbac.getAnnotations();
        assertTrue(annotations.containsKey(testLabelDD));
        assertEquals(DatabaseAccessor.DROPDOWN, annotations.get(testLabelDD));
    }

    @Test(expected = IOException.class)
    public void shouldNotAddDropDownAnnotationWithNoChoices()
            throws Exception {

        ArrayList<String> choices = new ArrayList<String>();
        String label = "should not be added";
        
        try {
            dbac.addDropDownAnnotation(label, choices, 0, false);
        } catch (Exception e) {
            throw e;
        } finally {
            Map<String, Integer> annotations = dbac.getAnnotations();
            assertFalse(annotations.containsKey(label));
        }
    }

    @Test
    public void shouldHaveChoicesForDropDownAnnotation()
            throws Exception {
        List<String> dropDownStrings = dbac.getChoices(testLabelDD);

        assertTrue(dropDownStrings.contains(testChoices.get(0)));
        assertTrue(dropDownStrings.contains(testChoices.get(1)));
        assertEquals(2, dropDownStrings.size());
    }

    @Test
    public void shouldHaveEmptyChoicesForFreeTextAnnotation()
            throws Exception {
        List<String> choices = dbac.getChoices(testLabelFT);
        assertTrue(choices.isEmpty());

    }

}
