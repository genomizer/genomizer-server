package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import testSuite.TestInitializer;
import database.DatabaseAccessor;

public class AddDropDownValueTest {

	private static DatabaseAccessor dbac;
	private static TestInitializer ti;

	@BeforeClass
	public static void setupTestCase() throws Exception {
    	ti = new TestInitializer();
    	dbac = ti.setup();

	}

	@AfterClass
	public static void undoAllChanges() throws SQLException {
		ti.removeTuples();
		dbac.close();
	}

    @Test
    public void testIfCorrectDataType() throws SQLException, IOException {

    	int ress = 0;
		ress = dbac.addDropDownAnnotationValue("Species", "tjo");
    	assertEquals(ress, 1);
		List<String> list = dbac.getChoices("Species");
		boolean exists = false;

		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).toLowerCase().equals("tjo")) {
				exists = true;
			}
		}
		assertTrue(exists);
    }

    @Test(expected = SQLException.class)
    public void shouldThrowSQLException() throws SQLException, IOException {

		dbac.addDropDownAnnotationValue("Sex", "Male");

    }

    @Test(expected = IOException.class)
    public void shouldThrowIOException() throws SQLException, IOException {
		dbac.addDropDownAnnotationValue("moho", "something");
    }
}
