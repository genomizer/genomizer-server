package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.AddExperimentCommand;
import command.AddFileToExperimentCommand;
import command.Command;

public class AddFileToExperimentCommandTest {
	private Gson gson;
	
	@Before
	public void setUp() throws Exception {
		final GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		gson = builder.create();
	}

	@Test
	public void testCorrectAnnotationValidation() {
		String json = "{\"experimentID\":\"ID\",\"fileName\":\"name\",\"type\":\"raw\"," +
				"\"metaData\":\"meta\",\"author\":\"name\",\"uploader\":\"user\",\"isPrivate\":\"true\"," +
				"\"grVersion\":\"release\"}";
				
		
		final Command aefc = gson.fromJson(json,AddFileToExperimentCommand.class);
		   
		assertTrue(aefc.validate());
	}
	
	@Test
	public void testMissingAnnotationValidation() {
		String json = "{\"experimentID\":\"ID\",\"type\":\"raw\"," +
				"\"metaData\":\"meta\",\"author\":\"name\",\"uploader\":\"user\",\"isPrivate\":\"true\"," +
				"\"grVersion\":\"release\"}";
				
		
		final Command aefc = gson.fromJson(json,AddFileToExperimentCommand.class);
		   
		assertFalse(aefc.validate());
	}
	
	@Test
	public void testIncorrectAnnotationValidationType() {
		String json = "{\"experimentID\":\"ID\",\"type\":\"\"," +
				"\"metaData\":\"meta\",\"author\":\"name\",\"uploader\":\"user\",\"isPrivate\":\"true\"," +
				"\"grVersion\":\"release\"}";
				
		
		final Command aefc = gson.fromJson(json,AddFileToExperimentCommand.class);
		   
		assertFalse(aefc.validate());
	}

}
