package database.testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.testSuite.TestInitializer;

public class PubMedIsValidTest {

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
	public void isValid() throws IOException {
		assertTrue(dbac.isPubMedStringValid("Exp1[ExpID]"));
	}

	@Test
	public void isValid2() throws IOException {
		assertTrue(dbac.isPubMedStringValid("((asd[Author]) AND bbb[Book]) AND 548[ISBN]"));
	}

	@Test(expected = IOException.class)
	public void isNotValid() throws IOException {
		dbac.isPubMedStringValid("Exp1[ExpID");
	}

	@Test(expected = IOException.class)
	public void isNotValid2() throws IOException {
		dbac.isPubMedStringValid("Exp1[ExpID AND Exp1[ExpID]]");
	}
}
