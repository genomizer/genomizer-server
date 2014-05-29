package command.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.JsonObject;

import command.ProcessCommand;
import command.ValidateException;

/**
 * Class used to test that the ProcessCommand class
 * works properly. The execute method is not tested here.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class ProcessCommandTest {

	/**
	 * Test used to check that creation works and
	 * object is not null.
	 */
	@Test
	public void testCreationNotNull() {
		
		ProcessCommand c = new ProcessCommand();
		
		assertNotNull(c);
		
	}

	
	/**
	 * Test used to check that ValidateException is not thrown
	 * when everything is properly formatted.
	 */
	@Test
	public void testValidateProperlyFormatted() {
		
		ProcessCommand c = new ProcessCommand();
		
	}
	
	
	
	
	/**
	 * Method used to build a JSON and return it as a string.
	 *
	 * @param strings to insert into JSON object.
	 * @return JSON formatted string.
	 */
	private String jsonBuilder(String expId, String ) {

		
		
		
		{"expid":"Exp1","parameters":["-a -m 1 --best -p 10 -v 2 -q -S", "","y","n","10 1 5 0 0","y 10","single 4 0","150 1 7 0 0"],"metadata":"astringofmetadata","genomeVersion":"hg38"}
		
		
	}
	
}
















