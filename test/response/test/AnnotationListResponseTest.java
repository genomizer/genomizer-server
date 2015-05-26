package response.test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;

import command.annotation.GetAnnotationCommand;
import database.subClasses.UserMethods.UserType;
import database.test.TestInitializer;
import org.junit.Before;
import org.junit.Test;
import response.AnnotationInformation;
import response.AnnotationListResponse;
import response.Response;

/**
 * Test used to check that GetAnnotationInformationResponse
 * works properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class AnnotationListResponseTest {

	@Before
	public void setup() {
		TestInitializer.setupServerSettings();
	}

	@Test
	public void testAnnotation() {
		//Create the builder.

		ArrayList<AnnotationInformation> arraylist = new ArrayList<>();
		ArrayList<String> gender = new ArrayList<>();
		gender.add("male");
		gender.add("female");
		gender.add("unknown");
		AnnotationInformation ai = new AnnotationInformation("Gender", gender, true);
		arraylist.add(ai);
		ArrayList<String> cellLine = new ArrayList<>();
		cellLine.add("true");
		cellLine.add("false");
		cellLine.add("unknown");
		AnnotationInformation ai2 = new AnnotationInformation("Cell Line", cellLine, true);
		arraylist.add(ai2);

		AnnotationListResponse air = new AnnotationListResponse(arraylist);
		assertEquals(air.getBody(), "[{\"name\":\"Gender\",\"values\":[\"male\",\"female\",\"unknown\"],\"forced\":true},{\"name\":\"Cell Line\",\"values\":[\"true\",\"false\",\"unknown\"],\"forced\":true}]");
	}

	@Test
	public void testDatabaseConnection() {
		GetAnnotationCommand cmd = new GetAnnotationCommand();
		HashMap<String, String> query = new HashMap<>();
		query.put("key", "val");
		cmd.setFields("uri", query, null, UserType.ADMIN);
		Response rsp = cmd.execute();
		rsp.getBody();
	}

}
