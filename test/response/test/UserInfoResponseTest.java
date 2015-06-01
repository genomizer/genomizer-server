package response.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import response.UserInfoResponse;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Tests userinforesponse for correct json convertion.
 *
 * @author Business Logic 2015
 * @version 1.1
 */
public class UserInfoResponseTest {
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


    /**     username
    *       UserRights (privileges)
    *       Full Name
    *       E-mail
    */
    @Test
    public void testGetBody() {
        ArrayList<String> array = new ArrayList<>();
        array.add("username");
        array.add("ADMIN");
        array.add("Random name");
        array.add("myEmail");

        UserInfoResponse rsp = new UserInfoResponse(array);
        String checker = rsp.getBody();
        assertEquals("{\"username\":\"username\",\"privileges\":\"ADMIN\",\"name\":\"Random name\",\"email\":\"myEmail\"}", checker);
    }
}
