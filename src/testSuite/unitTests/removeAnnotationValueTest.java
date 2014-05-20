package testSuite.unitTests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;

public class removeAnnotationValueTest {

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
    	int res = dbac.removeDropDownAnnotationValue("Sex", "Does not matter");
    	assertEquals(1, res);
    	List<String> choices = dbac.getChoices("Sex");
    	assertEquals(3, choices.size());
    }

    @Test
    public void testRemoveNonExistentValue() throws Exception {
    	int res = dbac.removeDropDownAnnotationValue("Sex", "nonexistatnt");
    	assertEquals(0, res);
    }

}
