package database.testSuite.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.testSuite.TestInitializer;

public class ChangeAnnotationLabelTest {

    private static TestInitializer ti;
    private static DatabaseAccessor dbac;

    @BeforeClass
    public static void setupTestCase() throws Exception {
        ti = new TestInitializer();
        dbac = ti.setup();
    }

    @AfterClass
    public static void teardownAfterClass() throws SQLException, Exception {
        ti.removeTuples();
    }

    /**
     * Test to check that the label changes.
     *
     * @throws IOException
     *
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

    @Test(expected = IOException.class)
    public void shouldNotBeAbleToChangeAnnotationToFileAnno()
            throws Exception {
        String label = "Author";
        dbac.changeAnnotationLabel("Sex", label);
        assertNull(dbac.getAnnotationObject(label));
    }

}
