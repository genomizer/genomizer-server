package command.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import command.AddAnnotationFieldCommand;
import command.Command;

public class AddAnnotationFieldCommandTest {


	/**
	 * Test creating and not null.
	 */
	@Test
	public void testCreateAddAnnotationFieldCommand() {

		AddAnnotationFieldCommand aafc = new AddAnnotationFieldCommand();
		assertNotNull(aafc);

	}

	/**
	 * Test AddAnnotationField command creation of object with
	 * JSON string and then check that they are equal.
	 */
	@Test
	public void testAddAnnotationFieldCommandJSON() {

		//Create the builder.
	    final GsonBuilder builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    final Gson gson = builder.create();

	    //Create input
	    String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
	    String restful = "";

		//Create command with json.
		final Command aafc = gson.fromJson(json, AddAnnotationFieldCommand.class);

		String json2 = gson.toJson(aafc);

		System.out.println(json2);

		//Set header
		aafc.setHeader(restful);

		assertEquals(json2, json);

	}


}




