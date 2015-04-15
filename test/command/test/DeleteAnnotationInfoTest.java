package command.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import command.DeleteAnnotationInfo;

/**
 * Class used to test that the DeleteAnnotationInfo class
 * works properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 *
 */
public class DeleteAnnotationInfoTest {

	public Gson gson = null;

	/**
	 * Setup method to initiate gson builder.
	 */
	@Before
	public void setUp() {

	    final GsonBuilder builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    gson = builder.create();

	}

	/**
	 * Test used to check that creation works and object
	 * is not null.
	 */
	@Test
	public void testCreateNotNull() {

		DeleteAnnotationInfo c = new DeleteAnnotationInfo();

		assertNotNull(c);

	}

	/**
	 * Test used to check that converting to and from JSON works
	 * properly.
	 */
	@Test
	public void testConvertJSON() {

		String json = "{\"name\":\"hello\",\"values\":[\"a\",\"b\",\"c\"]}";
		DeleteAnnotationInfo c = new DeleteAnnotationInfo();
		c = gson.fromJson(json, DeleteAnnotationInfo.class);
		String compare = gson.toJson(c);

		assertEquals(json, compare);

	}

	/**
	 * Test used to check that the getName method works
	 * properly.
	 */
	@Test
	public void testGetName() {

		String json = "{\"name\":\"hello\",\"values\":[\"a\",\"b\",\"c\"]}";
		DeleteAnnotationInfo c = new DeleteAnnotationInfo();
		c = gson.fromJson(json, DeleteAnnotationInfo.class);

		assertEquals("hello", c.getName());

	}

	/**
	 * Test used to check that the getValues method works properly.
	 */
	@Test
	public void testGetValues() {

		ArrayList<String> a = new ArrayList<String>();
		String insert = "";
		a.add("a");
		a.add("b");
		a.add("c");
		for(int i = 0; i < a.size(); i++) {
			insert = insert + "\"" + a.get(i) + "\"";
			if(i != a.size()-1) {
				insert = insert + ",";
			}
		}
		String json = "{\"name\":\"hello\",\"values\":["+ insert +"]}";
		DeleteAnnotationInfo c = new DeleteAnnotationInfo();
		c = gson.fromJson(json, DeleteAnnotationInfo.class);
		ArrayList<String> compare = c.getValues();

		for(int i = 0; i < compare.size(); i++) {
			assertEquals(a.get(i), compare.get(i));
		}

	}

}
