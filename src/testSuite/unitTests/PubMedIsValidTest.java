package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;

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

	/**
	 *
	 * @param pubMedString
	 * @return
	 * @throws IOException
	 */
	public boolean isPubMedStringValid(String pubMedString) throws IOException {

		int squareBracketsStart = 0, squareBracketsStop = 0;
		char last = 0;

		for (int i = 0; i < pubMedString.length(); i++) {

			if (squareBracketsStart + squareBracketsStop != 0) {
				if (last == pubMedString.charAt(i)) {
					throw new IOException("Missformed PubMed String");
				}
			}
			if (pubMedString.charAt(i) == '[') {
				squareBracketsStart++;
				last = pubMedString.charAt(i);

			} else if (pubMedString.charAt(i) == ']') {
				squareBracketsStop++;
				last = pubMedString.charAt(i);
			}
		}

		if (squareBracketsStart == squareBracketsStop) {
			return true;
		} else {
			throw new IOException("Missformed PubMed String");
		}
	}

	@Test
	public void isValid() throws IOException {
		assertTrue(isPubMedStringValid("Exp1[ExpID]"));
	}

	@Test
	public void isValid2() throws IOException {
		assertTrue(isPubMedStringValid("((asd[Author]) AND bbb[Book]) AND 548[ISBN]"));
	}

	@Test(expected = IOException.class)
	public void isNotValid() throws IOException {
		isPubMedStringValid("Exp1[ExpID");
	}

	@Test(expected = IOException.class)
	public void isNotValid2() throws IOException {
		isPubMedStringValid("Exp1[ExpID AND Exp1[ExpID]]");
	}
}
