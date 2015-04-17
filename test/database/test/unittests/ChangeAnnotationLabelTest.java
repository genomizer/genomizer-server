package database.test.unittests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.containers.Annotation;
import database.test.TestInitializer;

public class ChangeAnnotationLabelTest {

    private static TestInitializer ti;
    private static DatabaseAccessor dbac;

    @BeforeClass
    public static void setupTestCase() throws Exception {

        ti = new TestInitializer();
        dbac = ti.setup();
    }

    @AfterClass
    public static void teardownAfterClass() throws Exception {

        ti.removeTuples();
    }

    /**
     * Test to check that the label changes.
     *
     * @throws IOException
     */
    @Test
    public void shouldChangeChosenLabel() throws Exception {

        assertEquals(1, dbac.changeAnnotationLabel("Tissue", "Tis"));
        assertNotNull(dbac.getAnnotationObject("Tis"));
        assertNull(dbac.getAnnotationObject("Tissue"));
    }

    @Test(expected = IOException.class)
    public void shouldNotBeAbleToRenameSpeciesAnnotationLabel()
            throws Exception {

        String label = "Species";
        dbac.changeAnnotationLabel(label, "SomeLabel");
    }

    @Test(expected = IOException.class)
    public void shouldNotBeAbleToRemoveSpeciesAnnotationLabel()
            throws Exception {

        String label = "Species";
        dbac.deleteAnnotation(label);
    }


    @Test
    public void testChangeAnnotationRequiredField() throws SQLException,
    		IOException {

    	dbac.addFreeTextAnnotation("Test", "jepp", false);
    	Annotation testAnno = dbac.getAnnotationObject("Test");
    	assertFalse(testAnno.isRequired);

    	dbac.changeAnnotationRequiredField("Test", true);
    	testAnno = dbac.getAnnotationObject("Test");
    	assertTrue(testAnno.isRequired);

    	dbac.changeAnnotationRequiredField("Test", true);
    	testAnno = dbac.getAnnotationObject("Test");
    	assertTrue(testAnno.isRequired);
    }

    @Test(expected = IOException.class)
    public void shouldNotBeAbleToChangeAnnotationToFileAnno()
            throws Exception {
        String label = "Author";
        dbac.changeAnnotationLabel("Sex", label);
        assertNull(dbac.getAnnotationObject(label));
    }

}