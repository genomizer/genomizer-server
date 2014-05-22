package database.testSuite.unitTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import unused.FileValidator;

public class TestFileNameValidator {

	private FileValidator validator;

	@Before
	public void setUp() {

		validator = new FileValidator();
	}

	@Test
	public void testGoodChars() {
		boolean isOk = false;
		isOk = validator.isNameOk("aaa");

		assertTrue(isOk);
	}


	@Test
	public void testBadChars() {
		boolean isOk = false;
		isOk = validator.isNameOk("?!//&/*\n");

		assertFalse(isOk);
	}
}