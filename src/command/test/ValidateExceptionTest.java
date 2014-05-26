package command.test;

import static org.junit.Assert.*;
import org.junit.Test;
import command.ValidateException;

/**
 * Class used to test that the validateException class
 * works properly.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class ValidateExceptionTest {

	/**
	 * Test used to check that the exception is created successfully
	 * if there's no parameters passed and that it's not null.
	 */
	@Test
	public void testCreateEmptyNotNull() {

		ValidateException e = new ValidateException();
		assertNotNull(e);

	}

	/**
	 * Test used to check that the exception is created successfully
	 * if the constructor with two parameters is used.
	 */
	@Test
	public void testCreateParamConstructorNotNull() {

		ValidateException e = new ValidateException(401, "hello");
		assertNotNull(e);

	}

	/**
	 * Test used to check that the code can be set properly, and that it's
	 * retrievable with the getCode method.
	 */
	@Test
	public void testGetCodeMethod() {

		ValidateException e = new ValidateException(401, "hello");
		assertTrue(e.getCode() == 401);

	}

	/**
	 * Test used to check that the message can be set properly, and that it's
	 * retrievable with the getMessage method.
	 */
	@Test
	public void testGetMessageMethod() {

		ValidateException e = new ValidateException(401, "hello");
		assertEquals(e.getMessage(), "hello");

	}

}
