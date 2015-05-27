package response.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import response.FilePathListResponse;

/**
 * Class used to test the AddGenomeReleaseResponse class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class FilePathListResponseTest {

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
	public void testGetBody() {
		ArrayList<String> array = new ArrayList<>();
		array.add("FIRST");
		array.add("SECOND");
		FilePathListResponse rsp = new FilePathListResponse(array);
		String checker = rsp.getBody();
		assertEquals("[{\"URLupload\":\"FIRST\"},{\"URLupload\":\"SECOND\"}]", checker);
	}
}
