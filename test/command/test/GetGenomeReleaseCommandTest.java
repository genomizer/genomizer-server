package command.test;

import static org.junit.Assert.*;

import command.Command;
import command.ValidateException;
import database.subClasses.UserMethods.UserType;
import org.junit.Test;

import command.genomerelease.GetGenomeReleaseCommand;

/**
 * Test used to check that GetGenomeReleaseCommand class
 * works properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class GetGenomeReleaseCommandTest {

	/**
	 * Test used to check that creation works and object
	 * is not null.
	 */
	@Test
	public void testCreationNotNull() {

		GetGenomeReleaseCommand c = new GetGenomeReleaseCommand();

		assertNotNull(c);
	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		Command c = new GetGenomeReleaseCommand();
		c.setFields("uri", "", null, UserType.GUEST);
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

		Command c = new GetGenomeReleaseCommand();
		c.setFields("uri", "", null, UserType.UNKNOWN);
		c.validate();
		fail();
	}

}
