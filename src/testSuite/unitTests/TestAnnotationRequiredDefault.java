package testSuite.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;

public class TestAnnotationRequiredDefault {

    private static DatabaseAccessor dbac;
    private static TestInitializer ti;

    @BeforeClass
    public static void setupBeforeClass() throws Exception {
    	ti = new TestInitializer();
    	dbac = ti.setup();
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException {
    	ti.removeTuples();
    }

	@Test
	public void testIsRequierdTrue(){
		try {
			assertTrue(dbac.isAnnotationRequiered("Species"));
			assertTrue(dbac.isAnnotationRequiered("Tissue"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testIsRequierdFalse(){
		try {
			assertFalse(dbac.isAnnotationRequiered("Sex"));
			assertFalse(dbac.isAnnotationRequiered("Development Stage"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetDefaultValue(){

		try {
			assertEquals("Unknown",dbac.getDefaultAnnotationValue("Sex"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testShouldGetNull(){

		try {
			assertNull(dbac.getDefaultAnnotationValue("Tissue"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

