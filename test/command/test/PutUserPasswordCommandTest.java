package command.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.user.*;
import command.ValidateException;
import database.constants.MaxLength;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Test to see if PutUserPasswordCommand class works correctly
 * @author dv13jen
 */
@SuppressWarnings("deprecation")
public class PutUserPasswordCommandTest extends TestCase {

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
        PutUserPasswordCommand cmd = new PutUserPasswordCommand();
        cmd = gson.fromJson(json, PutUserPasswordCommand.class);
        assertNotNull(cmd);

    }

    /**
     * Test used to check that conversion to and from
     * JSON works properly.
     */
    @Test
    public void testConvertJSON() {

        json = createJSON("a","b");
        PutUserPasswordCommand cmd = new PutUserPasswordCommand();
        cmd = gson.fromJson(json, PutUserPasswordCommand.class);
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
        for(int i = 0; i < MaxLength.USERNAME+1; i++) {
            username = username + "a";
        }
        json = createJSON(username,"b");
        PutUserPasswordCommand cmd = new PutUserPasswordCommand();
        cmd = gson.fromJson(json, PutUserPasswordCommand.class);

        json = createJSON("","b");
        PutUserPasswordCommand cmd2 = new PutUserPasswordCommand();
        cmd2 = gson.fromJson(json, PutUserPasswordCommand.class);

        try {
            cmd.validate();
            cmd2.validate();
            fail("Expected ValidateException to be thrown.");
        }catch(ValidateException e){

        }
    }

    /**
     * Test used to check that password validation works
     * properly.
     */
    @Test
    public void testValidatePasswordLength() {

        String password = "";
        for(int i = 0; i < MaxLength.PASSWORD+1; i++) {
            password = password + "a";
        }
        json = createJSON("a",password);
        PutUserPasswordCommand cmd = new PutUserPasswordCommand();
        cmd = gson.fromJson(json, PutUserPasswordCommand.class);

        json = createJSON("a","");
        PutUserPasswordCommand cmd2 = new PutUserPasswordCommand();
        cmd2 = gson.fromJson(json, PutUserPasswordCommand.class);

        try {
            cmd.validate();
            cmd2.validate();
            fail("Expected ValidateException to be thrown.");
        }catch(ValidateException e){

        }
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