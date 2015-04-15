package database.testSuite.unitTests;

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
import database.testSuite.TestInitializer;

public class AddAnnotationPossibilitiesTests {

    private static DatabaseAccessor dbac;

    private static String testLabelFT = "testLabel1";
    private static String testLabelDD = "testLabelDD1";
    private static String testChoice = "testchoice";
    private static List<String> testChoices;

    @BeforeClass
    public static void setupTestCase() throws Exception {
        dbac = new DatabaseAccessor(TestInitializer.username,
        		TestInitializer.password, TestInitializer.host,
        		TestInitializer.database);

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
    public void teardown() throws Exception {

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

    @Test(expected=IOException.class)
    public void shouldThrowAnExceptionWhenAddingADropDownAnnotationThatAlreadyExists()
    		throws SQLException, IOException {

        ArrayList<String> otherChoices = new ArrayList<String>();
        otherChoices.add(testChoice);
        otherChoices.add(testChoice + "2");
    	dbac.addDropDownAnnotation(testLabelDD, otherChoices, 0, false);
    }

    @Test
    public void shouldBeAbleToAddADropDownAnnotationChoice() throws Exception {

    	String newChoice = "newChoice";
    	dbac.addDropDownAnnotationValue(testLabelDD, newChoice);
    	ArrayList<String> choices = (ArrayList<String>)
    			dbac.getAnnotationObject(testLabelDD).getPossibleValues();

    	assertTrue(choices.contains(newChoice));
    	assertEquals(3, choices.size());
	}

    @Test(expected=IOException.class)
    public void shouldNotBeAbleToAddADropDownChoiceThatAlreadyExist()
    		throws Exception {

    	dbac.addDropDownAnnotation(testLabelDD, testChoices, 0, false);
    	dbac.addDropDownAnnotationValue(testLabelDD, testChoice);
	}

    @Test(expected = IOException.class)
    public void shouldNotBeAbleToAddAChoiceForANoneDropDownAnnotation()
    		throws Exception {

    	String newChoice = "newChoice";
    	dbac.addDropDownAnnotationValue(testLabelFT, newChoice);
	}


    @Test(expected = IOException.class)
    public void shouldNotBeAbleToAddDate() throws SQLException, IOException {

    	dbac.addFreeTextAnnotation("Date", "2014-11-11", false);
    }

	@Test
	public void shouldRemoveAnnotationWithWeirdChars()
			throws Exception {

       	String annotation = "@@@@@@2$???";
    	dbac.addFreeTextAnnotation(annotation, null, false);

        Map<String, Integer> annotations = dbac.getAnnotations();
        assertTrue(annotations.containsKey(annotation));

        int i = dbac.deleteAnnotation(annotation);
        assertEquals(1,i);

        annotations = dbac.getAnnotations();
        assertFalse(annotations.containsKey(annotation));
    }

    @Test
    public void shouldRemoveAnotherAnnotationWithWeirdChars()
    		throws Exception {

    	String annotation = "�!";
    	dbac.addFreeTextAnnotation(annotation, null, false);

        Map<String, Integer> annotations = dbac.getAnnotations();
        assertTrue(annotations.containsKey(annotation));

        int i = dbac.deleteAnnotation(annotation);
        assertEquals(1,i);

        annotations = dbac.getAnnotations();
        assertFalse(annotations.containsKey(annotation));
    }
}
