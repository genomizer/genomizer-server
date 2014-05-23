package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import command.AddAnnotationFieldCommand;
import command.AddAnnotationValueCommand;
import command.AddExperimentCommand;
import command.Command;

public class AddExperimentCommandTest {
	
	public Gson gson = null;

	@Before
	public void setUp() throws Exception {
		final GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		gson = builder.create();
	}

	@Test
	public void testOkExperimentValidation() {
		String json = "{\"name\":\"expID\",\"annotations\":[{\"name\":\"pub\",\"value\":\"ab\"}," +
				"{\"name\":\"pub\",\"value\":\"ab\"},{\"name\":\"pub\",\"value\":\"ab\"}," +
				"{\"name\":\"pub\",\"value\":\"ab\"},{\"name\":\"pub\",\"value\":\"ab\"}," +
				"{\"name\":\"pub\",\"value\":\"ab\"},{\"name\":\"pub\",\"value\":\"ab\"}," +
				"{\"name\":\"pub\",\"value\":\"ab\"}]}";
		
		final Command aefc = gson.fromJson(json,AddExperimentCommand.class);
		   
		assertTrue(aefc.validate());
	}
	
	@Test
	public void testNullExperimentIDValidation() {
		String json = "{\"annotations\":[{\"name\":\"pub\",\"value\":\"ab\"}," +
				"{\"name\":\"pub\",\"value\":\"ab\"},{\"name\":\"pub\",\"value\":\"ab\"}," +
				"{\"name\":\"pub\",\"value\":\"ab\"},{\"name\":\"pub\",\"value\":\"ab\"}," +
				"{\"name\":\"pub\",\"value\":\"ab\"},{\"name\":\"pub\",\"value\":\"ab\"}," +
				"{\"name\":\"pub\",\"value\":\"ab\"}]}";
		
		final Command aefc = gson.fromJson(json,AddExperimentCommand.class);
		   
		assertFalse(aefc.validate());
	}
	
	//TEST NÄR INTE ALLA ANNOTATIONER FINNS!?
	
	/*@Test
	public void testNullAnnotationValidation() {
		//String json = "{\"name\":\"expID\",\"annotations\":[{\"name\":\"pub\",\"value\":\"ab\"}]}";
		String json = "{\"name\":\"expID\",\"annotations\":[{\"value\":\"ab\"}," +
				"{\"name\":\"pub\",\"value\":\"ab\"},{\"name\":\"pub\",\"value\":\"ab\"}," +
				"{\"name\":\"pub\",\"value\":\"ab\"},{\"name\":\"pub\",\"value\":\"ab\"}," +
				"{\"name\":\"pub\",\"value\":\"ab\"},{\"name\":\"pub\",\"value\":\"ab\"}," +
				"{\"name\":\"pub\",\"value\":\"ab\"}]}";
		final Command aefc = gson.fromJson(json,AddExperimentCommand.class);
		   
		assertFalse(aefc.validate());
	}*/

}
