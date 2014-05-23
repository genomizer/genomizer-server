package database.testSuite.unitTests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.testSuite.TestInitializer;

public class RemoveAnnotationValueTest {

    private static TestInitializer ti;
	private static DatabaseAccessor dbac;

	@BeforeClass
	public static void setupTestCase() throws Exception {

        ti = new TestInitializer();
        dbac = ti.setupWithoutAddingTuples();
	}

	@AfterClass
	public static void undoAllChanges() throws SQLException {
		ti.removeTuples();
	}

	@Before
	public void setup() throws SQLException {
	    ti.addTuples();
	}

	@After
	public void teardown() throws SQLException {
	    ti.removeTuplesKeepConnection();
	}

    @Test (expected = IOException.class)
    public void testRemoveDefaultValue() throws Exception {
        dbac.removeDropDownAnnotationValue("Sex", "Unknown");
    }

    @Test
    public void testRemoveAnnotationValue() throws Exception {
    	int res = dbac.removeDropDownAnnotationValue("Species", "Fly");
    	assertEquals(1, res);
    }

    @Test
    public void testRemoveNonExistentValue() throws Exception {
    	int res = dbac.removeDropDownAnnotationValue("Sex", "nonexistatnt");
    	assertEquals(0, res);
    }

    @Test (expected = IOException.class)
    public void shouldNotRemoveUsedValue() throws SQLException, IOException {
    	dbac.removeDropDownAnnotationValue("Species", "Human");
    }

}
