package command.test;

import static org.junit.Assert.*;

import database.constants.MaxLength;
import org.junit.Test;
import command.GetFileFromExperimentCommand;
import command.ValidateException;

/**
 * Class used to test that the GetFileFromExperiment class
 * works properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class GetFileFromExperimentCommandTest {

	/**
	 * Test used to check that creation works and
	 * object is not null.
	 */
	@Test
	public void testCreateNotNull() {

		GetFileFromExperimentCommand c = new GetFileFromExperimentCommand("Hello");

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when FileExpId is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileExpIdEmptyString() throws ValidateException {

		GetFileFromExperimentCommand c = new GetFileFromExperimentCommand("");
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when FileExpId is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileExpIdNotNull() throws ValidateException {

		GetFileFromExperimentCommand c = new GetFileFromExperimentCommand(null);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when FileExpId length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileExpIdLength() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxLength.FILE_EXPID + 1; i++) {
			big = big + "a";
		}
		GetFileFromExperimentCommand c = new GetFileFromExperimentCommand(big);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when everything is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {

		GetFileFromExperimentCommand c = new GetFileFromExperimentCommand("properly");
		c.validate();

		assertTrue(true);

	}

}
