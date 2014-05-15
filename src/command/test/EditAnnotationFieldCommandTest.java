package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import response.Response;

import com.google.gson.JsonObject;

import command.Command;
import command.CommandFactory;
import command.EditAnnotationFieldCommand;

public class EditAnnotationFieldCommandTest {

	private Command command;

	@Before
	public void setup() {
		CommandFactory fac = new CommandFactory();
		JsonObject obj = new JsonObject();
		obj.addProperty("oldName", "field123");
		obj.addProperty("newName", "Field123");
		command = fac.createEditAnnotationFieldCommand(obj.toString());
	}

	@Test
	public void shouldCreateCommand() {
		assertNotNull(command);
	}

	@Test
	public void shouldValidate() throws Exception {
		assertEquals(true,command.validate());
	}

	@Test
	public void shouldExecute() throws Exception {
		Response resp = command.execute();
		System.out.println(resp.getCode());
		System.out.println(resp.getBody());
	}

}
