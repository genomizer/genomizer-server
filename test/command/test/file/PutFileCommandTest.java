package command.test.file;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.Command;
import command.ValidateException;
import database.subClasses.UserMethods.UserType;
import org.junit.Before;
import org.junit.Test;
import command.file.PutFileCommand;

/**
 * Class used to test that UpdateFileInExperiment works
 * properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class PutFileCommandTest {
////TODO Implement tests later

	private Gson gson;

	/**
	 * Setup method used to create the gson builder.
	 */
	@Before
	public void setUp() {

		final GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		gson = builder.create();

	}

//	/**
//	 * Test that creation works and object is not null.
//	 */
//	@Test
//	public void testCreateNotNull() {
//		PutFileCommand c = new PutFileCommand("","");
//		assertNotNull(c);
//
//	}
//
//	/**
//	 * Test used to check that validate always returns true.
//	 */
//	@Test
//	public void testValidateAlwaysTrue() {
//		PutFileCommand c = new PutFileCommand("","");
//		c.validate();
//	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {
		String json = jsonBuilder("filename","type","metadata","author","version");
		Command c = gson.fromJson(json, PutFileCommand.class);
		c.setFields("uri", null, null, UserType.USER);
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
		String json = jsonBuilder("filename","type","metadata","author","version");
		Command c = gson.fromJson(json, PutFileCommand.class);
		c.setFields("uri", null, null, UserType.GUEST);
		c.validate();
	}

	/**
	 * Method used to build a JSON and return it as a string.
	 *
	 * @return JSON formatted string.
	 */
	private String jsonBuilder(String fileN, String type, String metaD, String ath, String grV) {

		JsonObject j = new JsonObject();
		j.addProperty("fileName", fileN);
		j.addProperty("type", type);
		j.addProperty("metaData", metaD);
		j.addProperty("author", ath);
		j.addProperty("grVersion", grV);

		return j.toString();
	}

}
