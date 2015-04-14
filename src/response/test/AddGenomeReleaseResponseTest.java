package response.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import response.AddGenomeReleaseResponse;
import response.StatusCode;

/**
 * Class used to test the AddGenomeReleaseResponse class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddGenomeReleaseResponseTest {

	public Gson gson = null;

	/**
	 * Setup method to initiate GSON builder.
	 */
	@Before
	public void setUp() {

	    final GsonBuilder builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    gson = builder.create();

	}

	@Test
	@Ignore
	public void testGetBody() {

		ArrayList<String> array = new ArrayList<String>();
		array.add("FIRST");
		array.add("SECOND");
		AddGenomeReleaseResponse rsp = new AddGenomeReleaseResponse(StatusCode.CREATED, array);
		String checker = rsp.getBody();
		assertEquals(checker, "[\"FIRST\",\"SECOND\"]");

	}

}
