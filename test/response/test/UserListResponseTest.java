package response.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import response.UserListResponse;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by dv13thg on 5/27/15.
 */
public class UserListResponseTest {

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
        UserListResponse rsp = new UserListResponse(array);
        String checker = rsp.getBody();
        assertEquals("[{\"username\":\"FIRST\"},{\"username\":\"SECOND\"}]", checker);
    }
}
