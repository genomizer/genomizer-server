package command.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.ChangeUserPasswordCommand;
import database.constants.MaxSize;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Test to see if ChangeUserPasswordCommand class works correctly
 * @author dv13jen
 */
public class ChangeUserPasswordCommandTest extends TestCase {

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
        ChangeUserPasswordCommand cmd = new ChangeUserPasswordCommand();
        cmd = gson.fromJson(json, ChangeUserPasswordCommand.class);
        assertNotNull(cmd);

    }

    /**
     * Test used to check that conversion to and from
     * JSON works properly.
     */
    @Test
    public void testConvertJSON() {

        json = createJSON("a","b");
        ChangeUserPasswordCommand cmd = new ChangeUserPasswordCommand();
        cmd = gson.fromJson(json, ChangeUserPasswordCommand.class);
        String compare = gson.toJson(cmd);
        assertEquals(compare, json);

    }

    /**
     * Test used to check that username validation works
     * properly.
     */
    @Test
    public void testValidateUsernameLength() {

        String username = "";
        for(int i = 0; i < MaxSize.USERNAME+1; i++) {
            username = username + "a";
        }
        json = createJSON(username,"b");
        ChangeUserPasswordCommand cmd = new ChangeUserPasswordCommand();
        cmd = gson.fromJson(json, ChangeUserPasswordCommand.class);

        json = createJSON("","b");
        ChangeUserPasswordCommand cmd2 = new ChangeUserPasswordCommand();
        cmd2 = gson.fromJson(json, ChangeUserPasswordCommand.class);

        assertFalse(cmd.validate());
        assertFalse(cmd2.validate());

    }

    /**
     * Test used to check that password validation works
     * properly.
     */
    @Test
    public void testValidatePasswordLength() {

        String password = "";
        for(int i = 0; i < MaxSize.PASSWORD+1; i++) {
            password = password + "a";
        }
        json = createJSON("a",password);
        ChangeUserPasswordCommand cmd = new ChangeUserPasswordCommand();
        cmd = gson.fromJson(json, ChangeUserPasswordCommand.class);

        json = createJSON("a","");
        ChangeUserPasswordCommand cmd2 = new ChangeUserPasswordCommand();
        cmd2 = gson.fromJson(json, ChangeUserPasswordCommand.class);

        assertFalse(cmd.validate());
        assertFalse(cmd2.validate());

    }

    /**
     * Used to create JSON objects.
     *
     * @param username
     * @param password
     * @return
     */
    private String createJSON(String username, String password) {

        JsonObject j = new JsonObject();
        j.addProperty("username", username);
        j.addProperty("password", password);

        return j.toString();

    }
}