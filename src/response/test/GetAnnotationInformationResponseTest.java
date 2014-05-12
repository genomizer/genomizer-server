package response.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import response.AnnotationInformation;
import response.GetAnnotationInformationResponse;
import response.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.AddAnnotationFieldCommand;
import command.Command;
import command.GetAnnotationInformationCommand;

public class GetAnnotationInformationResponseTest {

	public void testAnnotation() {
		//Create the builder.

		ArrayList<AnnotationInformation> arraylist = new ArrayList<AnnotationInformation>();
		ArrayList<String> gender = new ArrayList<String>();
		gender.add("male");
		gender.add("female");
		gender.add("unknown");
		AnnotationInformation ai = new AnnotationInformation(1, "Gender", AnnotationInformation.TYPE_DROP_DOWN, gender, true);
		arraylist.add(ai);
		ArrayList<String> cellLine = new ArrayList<String>();
		cellLine.add("true");
		cellLine.add("false");
		cellLine.add("unknown");
		AnnotationInformation ai2 = new AnnotationInformation(2, "Cell Line", AnnotationInformation.TYPE_DROP_DOWN, cellLine, true);
		arraylist.add(ai2);

		GetAnnotationInformationResponse air = new GetAnnotationInformationResponse(200, arraylist);
		System.out.println(air.getBody());
		assertEquals(air.getBody(), "{\"annotations\":[{\"id\":1,\"name\":\"Gender\",\"type\":1,\"values\":[\"male\",\"female\",\"unknown\"],\"forced\":true},{\"id\":2,\"name\":\"Cell Line\",\"type\":1,\"values\":[\"true\",\"false\",\"unknown\"],\"forced\":true}]}");
	}

	@Test
	public void testDatabaseConnection() {
		GetAnnotationInformationCommand cmd = new GetAnnotationInformationCommand();
		Response rsp = cmd.execute();
		rsp.getBody();
	}

}
