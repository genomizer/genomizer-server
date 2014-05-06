package command.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import command.Command;
import command.LoginCommand;

public class LoginCommandTest {

	/**
	 * Test login command creation.
	 */
	@Test
	public void testCreateLoginCommand() {

		LoginCommand lcmd = new LoginCommand();
		assertNotNull(lcmd);

	}

	/**
	 * Test login command creation of object with
	 * JSON string.
	 */
	@Test
	public void testLoginCommandJSON() {

		//Create the builder.
	    final GsonBuilder builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    final Gson gson = builder.create();

	    //Create input
	    String json = "{\"username\": \"uname\", \"password\": \"pw\"}";
	    String restful = "";

		//Create command with json.
		final Command lcmd = gson.fromJson(json, LoginCommand.class);

		//Set headers
		lcmd.setHeader(restful);

		assertNotNull(lcmd);

	}

}
