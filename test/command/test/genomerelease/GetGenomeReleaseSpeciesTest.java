package command.test.genomerelease;

import static org.junit.Assert.*;

import command.genomerelease.GetGenomeReleaseCommand;
import command.Command;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Ignore;
import org.junit.Test;
import command.ValidateException;

/**
 * Test used to check that the GetGenomeReleaseSpeciesCommand
 * class works properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class GetGenomeReleaseSpeciesTest {


	/**
	 * Test used to check that ValidateException is not thrown when
	 * everything is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {

		Command c = new GetGenomeReleaseCommand();
		c.setFields("/genomeRelease/properly", null, null, UserType.ADMIN);
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

		Command c = new GetGenomeReleaseCommand();
		c.setFields("/genomeRelease/properly", null, null, UserType.GUEST);
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
		c.setFields("/genomeRelease/properly", null, null, UserType.UNKNOWN);
		c.validate();
		fail();
	}

}
