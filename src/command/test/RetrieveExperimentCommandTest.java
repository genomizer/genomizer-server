package command.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import command.Command;
import command.RetrieveExperimentCommand;

/**
 * Test class used to test RetrieveExperimentCommand
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class RetrieveExperimentCommandTest {

	/**
	 * Test creation of RetrieveExperimentCommand object.
	 */
	@Test
	public void testCreateRetrieveExperimentCommand() {

		RetrieveExperimentCommand recmd = new RetrieveExperimentCommand();
		assertNotNull(recmd);

	}

	/**
	 * Test retrieve experiment command creation of object with
	 * JSON string.
	 */
	@Test
	public void testRetrieveExperimentCommandJSON() {

		//Create the builder.
	    final GsonBuilder builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    final Gson gson = builder.create();

	    //Create input
	    String json = "{\"name\": \"experimentName\",\"created by\": \"user\",\"annotations\": {\"pubmedId\": \"ex23\",\"type\": \"raw\"," +
	    		"\"specie\": \"human\",\"genoRelease\": \"v1.23\",\"cellLine\": \"yes\",\"devStage\": \"larva\",\"sex\": \"male\",\"tissue\": \"eye\"}}";
	    String restful = "";

		//Create command with json.
		final Command recmd = gson.fromJson(json, RetrieveExperimentCommand.class);
		recmd.setHeader(restful);

		assertNotNull(recmd);

	}



}



