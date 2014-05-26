package command.test;

import static org.junit.Assert.*;

import org.junit.Test;

import command.DeleteAnnotationFieldCommand;

/**
 * Class used to test that the DeleteAnnotationFieldCommand
 * class works properly.
 * 
 * @author tfy09jnn
 * @version 1.0
 */
public class DeleteAnnotationFieldCommandTest {

	/**
	 * Test that checks that creation works and is not null.
	 */
	@Test
	public void testCreationNotNull() {
		
		DeleteAnnotationFieldCommand c = new DeleteAnnotationFieldCommand("a");
		assertNotNull(c);
		
	}
	
	/**
	 * Test that checks that false is returned if the restful
	 * is null.
	 */
	@Test
	public void testValidationRestNull() {
		DeleteAnnotationFieldCommand c = new DeleteAnnotationFieldCommand(null);
		assertFalse(c.validate());
	}

	/**
	 * Test that checks that true is returned when everything
	 * is properly formatted.
	 */
	@Test
	public void testValidationProperlyFormatted() {
		DeleteAnnotationFieldCommand c = new DeleteAnnotationFieldCommand("great");
		assertTrue(c.validate());
	}
	
}
