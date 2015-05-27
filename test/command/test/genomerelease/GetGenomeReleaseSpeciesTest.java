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

//	/**
//	 * Test used to check that creation works and object
//	 * is not null.
//	 */
//	@Test
//	public void testCreationNotNull() {
//
//		GetGenomeReleaseSpeciesCommand c = new GetGenomeReleaseSpeciesCommand("testing");
//
//		assertNotNull(c);
//
//	}
//
//	/**
//	 * Test used to check that ValidateException is thrown when
//	 * species is an empty string.
//	 *
//	 * @throws ValidateException
//	 */
//	@Test(expected = ValidateException.class)
//	public void testValidateSpeciesEmptyString() throws ValidateException {
//
//		GetGenomeReleaseSpeciesCommand c = new GetGenomeReleaseSpeciesCommand("");
//		c.validate();
//
//		fail("Expected ValidateException to be thrown.");
//
//	}
//
//	/**
//	 * Test used to check that ValidateException is thrown when
//	 * species is null.
//	 *
//	 * @throws ValidateException
//	 */
//	@Test(expected = ValidateException.class)
//	public void testValidateSpeciesNotNull() throws ValidateException {
//
//		GetGenomeReleaseSpeciesCommand c = new GetGenomeReleaseSpeciesCommand(null);
//		c.validate();
//
//		fail("Expected ValidateException to be thrown.");
//
//	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * species length is to long.
	 *
	 * @throws ValidateException
	 */
	@Ignore
	@Test(expected = ValidateException.class)
	public void testValidateSpeciesLength() throws ValidateException {

		String uri = "/genomeRelease/";
		for(int i = 0; i < MaxLength.GENOME_SPECIES + 1; i++) {
			uri = uri + "a";
		}
		Command c = new GetGenomeReleaseCommand();
		c.setFields(uri, null, null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

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
