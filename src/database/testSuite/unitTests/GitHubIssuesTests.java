package database.testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.testSuite.TestInitializer;

public class GitHubIssuesTests {
    private static DatabaseAccessor dbac;
    private static TestInitializer ti;
    private static List<String> choices;

    @BeforeClass
    public static void setupBeforeClass() throws Exception {

    	ti = new TestInitializer();
    	dbac = ti.setup();

    	choices = new ArrayList<String>();
    	choices.add("mumbojumbo");
    	choices.add("mumbodumbo");
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException {

    	ti.removeTuples();
    }

    @Test
    public void testRemoveAnnotationWithSlash() throws SQLException,
    		IOException {

    	dbac.addFreeTextAnnotation("hej", null, false);
    	dbac.changeAnnotationLabel("hej", "/hej/");

    	assertNotNull(dbac.getAnnotationObject("/hej/"));
    	assertEquals(1, dbac.deleteAnnotation("/hej/"));
    	assertNull(dbac.getAnnotationObject("/hej/"));
    }

    @Test
    public void testChangeAnnotationSwagName() throws SQLException,
    		IOException {

    	dbac.addDropDownAnnotation("amountOfSwag", choices, 0, false);

    	assertEquals(1, dbac.changeAnnotationLabel("amountOfSwag",
    			"amountOfSwaggerness"));
    	assertNotNull(dbac.getAnnotationObject("amountOfSwaggerness"));
    	assertNull(dbac.getAnnotationObject("amountOfSwag"));
    }

    @Test
    public void testChangeAnnotationLabelCereal() throws SQLException,
    		IOException {

    	dbac.addFreeTextAnnotation("SuperCereal", null, false);

    	assertEquals(1, dbac.changeAnnotationLabel("SuperCereal",
    			"SuperCerea"));
    	assertNotNull(dbac.getAnnotationObject("SuperCerea"));
    	assertNull(dbac.getAnnotationObject("SuperCereal"));
    }
}