package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import command.Annotation;

/**
 * Class used to test that the Annotation class works
 * properly.
 * 
 * @author tfy09jnn
 * @version 1.0
 */
public class AnnotationTest {

	private Gson gson = null;
	private Annotation a = null;
	private String json = null;
	private String name = null;
	private String value = null;

	/**
	 * Setup method to initiate GSON builder and
	 * initiate a Annotation class with JSON.
	 */
	@Before
	public void setUp() {

	    final GsonBuilder builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    gson = builder.create();
	    a = new Annotation();
	    name = "b";
	    value = "c";
		json = "{\"id\":\"a\",\"name\":\"" + name +
				"\",\"value\":\"" + value +"\"}";
		a = gson.fromJson(json, Annotation.class);

	}
	
	/**
	 * Test used to check that a created object is not
	 * null.
	 */
	@Test
	public void testCreationNotNull() {
		assertNotNull(a);
	}
	
	/**
	 * Test used to check that converting to and from JSON
	 * works properly.
	 */
	@Test
	public void testConvertJSON() {
		String compare = gson.toJson(a);
		assertEquals(json, compare);
	}
	
	/**
	 * Test used to check that the get name method works.
	 */
	@Test
	public void testGetName() {
		
		String name = a.getName();
		assertEquals(this.name, name);
		
	}
	
	/**
	 * Test used to check that the get value method works.
	 */
	@Test
	public void testGetValue() {
		String value = a.getValue();
		assertEquals(this.value, value);
	}
	
}
