package command.test;

import static org.junit.Assert.*;

import command.Command;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Test;
import command.file.DeleteFileCommand;
import command.ValidateException;

/**
 * Class used to test that DelteFileFromExperimentCommand
 * class works properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class DeleteFileCommandTest {

	/**
	 * Test used to check that ValidateException is thrown if the
	 * file experiment id length is to long.
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileExpIdLength() throws ValidateException {

		String uri = "/file/";
		for(int i = 0; i < MaxLength.FILE_EXPID + 1; i++) {
			uri  += "a";
		}

		Command c = new DeleteFileCommand();
		c.setFields(uri, "", null, UserType.ADMIN);
		c.validate();
		fail("Expected ValidateException.");
	}

	/**
	 * Test used to check that no validateException is thrown and
	 * that everything works properly if everything is correctly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void textValidateProperlyFormatted() throws ValidateException {
		Command c = new DeleteFileCommand();
		c.setFields("/file/Hello", "", null, UserType.ADMIN);
		c.validate();
		assertTrue(true);
	}
	/**
	 * Test used to check that ValidateException is thrown
	 * when invalid characters are used
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateIncorrectlyFormatted() throws ValidateException {

		Command c = new DeleteFileCommand();
		c.setFields("/file/��", "", null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		Command c = new DeleteFileCommand();
		c.setFields("/genomeRelease/Specie/GRelease", "", null, UserType.USER);
		c.validate();
	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when the user doesn't have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testNotHavingRights() throws ValidateException {

		Command c = new DeleteFileCommand();
		c.setFields("/genomeRelease/Specie/GRelease", "", null, UserType.GUEST);
		c.validate();
		fail();
	}
}
