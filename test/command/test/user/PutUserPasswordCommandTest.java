package command.test.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.user.*;
import command.ValidateException;
import database.constants.MaxLength;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Test to see if PutUserPasswordCommand class works correctly
 * @author dv13jen
 */
@SuppressWarnings("deprecation")
public class PutUserPasswordCommandTest {

    private Gson gson = null;
    private String json = null;

    /**
     * Setup method to initiate GSON builder.
     */
    @Before
    public void setUp() {

        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        gson = builder.create();

    }

    /**
     * Test used to check that creation is not null.
     */
    @Test
    public void testCreationNotNull() {

        json = createJSON("a","b");
        PutUserPasswordCommand cmd = gson.fromJson(json, PutUserPasswordCommand.class);
        assertNotNull(cmd);
    }

    /**
     * Test used to check that conversion to and from
     * JSON works properly.
     */
    @Test
    public void testConvertJSON() {

        json = createJSON("a","b");
        PutUserPasswordCommand cmd = gson.fromJson(json, PutUserPasswordCommand.class);
        String compare = gson.toJson(cmd);
        assertEquals(compare, json);

    }

    /**
     * Test used to check that username validation works
     * properly.
     */
    @Test (expected = ValidateException.class)
    public void testValidateUsernameLength() throws ValidateException {

        String username = "";
        for(int i = 0; i < MaxLength.USERNAME+1; i++) {
            username = username + "a";
        }
        json = createJSON(username,"b");
        PutUserPasswordCommand cmd = gson.fromJson(json, PutUserPasswordCommand.class);

        json = createJSON("","b");
        PutUserPasswordCommand cmd2 = gson.fromJson(json, PutUserPasswordCommand.class);

        cmd.validate();
        cmd2.validate();
    }

    /**
     * Test used to check that password validation works
     * properly.
     */
    @Test (expected = ValidateException.class)
    public void testValidatePasswordLength() throws ValidateException {

        String password = "";
        for(int i = 0; i < MaxLength.PASSWORD+1; i++) {
            password = password + "a";
        }
        json = createJSON("a",password);
        PutUserPasswordCommand cmd;
        cmd = gson.fromJson(json, PutUserPasswordCommand.class);

        json = createJSON("a","");
        PutUserPasswordCommand cmd2;
        cmd2 = gson.fromJson(json, PutUserPasswordCommand.class);

        cmd.validate();
        cmd2.validate();
    }

    /**
     * Used to create JSON objects.
     *
     * @param username the username
     * @param password the password
     * @return string
     */
    private String createJSON(String username, String password) {

        JsonObject j = new JsonObject();
        j.addProperty("username", username);
        j.addProperty("password", password);

        return j.toString();

    }
}