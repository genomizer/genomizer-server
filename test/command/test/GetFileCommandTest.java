package command.test;

import static org.junit.Assert.*;

import command.Command;
import command.file.GetFileCommand;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Test;
import command.ValidateException;

/**
 * Class used to test that the GetFileFromExperiment class
 * works properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class GetFileCommandTest {
	
	/**
	 * Test used to check that ValidateException is thrown
	 * when FileExpId length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileExpIdMaxLength() throws ValidateException {
		String uri = "/file/";
		for(int i = 0; i < MaxLength.FILE_EXPID + 1; i++) {
			uri += "a";
		}
		Command c = new GetFileCommand();
		c.setFields(uri, "", null, UserType.ADMIN);
		c.validate();
	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when FileExpId has invalidcharacters
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileExpIdInvalidCharacters() throws ValidateException {
		Command c = new GetFileCommand();
		c.setFields( "/file/��!?,:;[]{}", "", null, UserType.ADMIN);
		c.validate();
	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when everything is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {

		Command c = new GetFileCommand();
		c.setFields("/file/properly", "", null, UserType.ADMIN);
		c.validate();

		assertTrue(true);

	}
	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		Command c = new GetFileCommand();
		c.setFields("/file/properly", "", null, UserType.USER);
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

		Command c = new GetFileCommand();
		c.setFields("/file/properly", "", null, UserType.GUEST);
		c.validate();
		fail();
	}

}
