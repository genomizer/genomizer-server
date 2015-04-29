package response.test;

import static org.junit.Assert.*;
import java.util.ArrayList;

import database.subClasses.UserMethods.UserType;
import database.test.TestInitializer;
import org.junit.Before;
import org.junit.Test;
import response.AnnotationInformation;
import response.GetAnnotationInformationResponse;
import response.Response;
import command.GetAnnotationInformationCommand;

/**
 * Test used to check that GetAnnotationInformationResponse
 * works properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class GetAnnotationInformationResponseTest {

	public void testAnnotation() {
		//Create the builder.

		ArrayList<AnnotationInformation> arraylist = new ArrayList<AnnotationInformation>();
		ArrayList<String> gender = new ArrayList<String>();
		gender.add("male");
		gender.add("female");
		gender.add("unknown");
		AnnotationInformation ai = new AnnotationInformation("Gender", gender, true);
		arraylist.add(ai);
		ArrayList<String> cellLine = new ArrayList<String>();
		cellLine.add("true");
		cellLine.add("false");
		cellLine.add("unknown");
		AnnotationInformation ai2 = new AnnotationInformation("Cell Line", cellLine, true);
		arraylist.add(ai2);

		GetAnnotationInformationResponse air = new GetAnnotationInformationResponse(200, arraylist);
		System.out.println(air.getBody());
		assertEquals(air.getBody(), "{\"annotations\":[{\"id\":1,\"name\":\"Gender\",\"type\":1,\"values\":[\"male\",\"female\",\"unknown\"],\"forced\":true},{\"id\":2,\"name\":\"Cell Line\",\"type\":1,\"values\":[\"true\",\"false\",\"unknown\"],\"forced\":true}]}");
	}

	@Before
	public void setup() {
		TestInitializer.setupServerSettings();
	}

	@Test
	public void testDatabaseConnection() {
		GetAnnotationInformationCommand cmd = new GetAnnotationInformationCommand();
		cmd.setFields("uri",null ,UserType.ADMIN);
		Response rsp = cmd.execute();
		rsp.getBody();
	}

}
