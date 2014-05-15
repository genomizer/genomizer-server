package testSuite.unitTests;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;

public class GenomeObjectTest {


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
	public void test(){

	}
}
