package command.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.EditUserCommand;
import command.ValidateException;
import database.constants.MaxLength;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EditUserCommandTest {

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

        json = createJSON("a","b","c","d","e");
        EditUserCommand cmd = new EditUserCommand();
        cmd = gson.fromJson(json, EditUserCommand.class);
        assertNotNull(cmd);

    }

    /**
     * Test used to check that conversion to and from
     * JSON works properly.
     */
    @Test
    public void testConvertJSON() {

        json = createJSON("a","b","c","d","e");
        EditUserCommand cmd = new EditUserCommand();
        cmd = gson.fromJson(json, EditUserCommand.class);
        String compare = gson.toJson(cmd);
        assertEquals(compare, json);

    }

    /**
     * Test used to check that username validation works
     * properly.
     */
    @Test(expected = ValidateException.class)
    public void testValidateUsernameLength() throws ValidateException {

        String username = "";
        for(int i = 0; i < MaxLength.USERNAME+1; i++) {
            username = username + "a";
        }
        json = createJSON(username,"b","c","d","e");
        EditUserCommand cmd = new EditUserCommand();
        cmd = gson.fromJson(json, EditUserCommand.class);

        json = createJSON("","b","c","d","e");
        EditUserCommand cmd2 = new EditUserCommand();
        cmd2 = gson.fromJson(json, EditUserCommand.class);

        cmd.validate();
        cmd2.validate();
    }

    /**
     * Test used to check that password validation works
     * properly.
     */
    @Test(expected = ValidateException.class)
    public void testValidatePasswordLength() throws ValidateException {

        String password = "";
        for(int i = 0; i < MaxLength.PASSWORD+1; i++) {
            password = password + "a";
        }
        json = createJSON("a",password,"c","d","e");
        EditUserCommand cmd = new EditUserCommand();
        cmd = gson.fromJson(json, EditUserCommand.class);

        json = createJSON("a","","c","d","e");
        EditUserCommand cmd2 = new EditUserCommand();
        cmd2 = gson.fromJson(json, EditUserCommand.class);

        cmd.validate();
        cmd2.validate();

    }

    /**
     * Test used to check that privileges validation works
     * properly.
     */
    @Test(expected = ValidateException.class)
    public void testValidatePrivilegesLength() throws ValidateException {

        String priv = "";
        for(int i = 0; i < MaxLength.ROLE+1; i++) {
            priv = priv + "a";
        }
        json = createJSON("a","b",priv,"d","e");
        EditUserCommand cmd = new EditUserCommand();
        cmd = gson.fromJson(json, EditUserCommand.class);

        json = createJSON("a","b","","d","e");
        EditUserCommand cmd2 = new EditUserCommand();
        cmd2 = gson.fromJson(json, EditUserCommand.class);

        cmd.validate();
        cmd2.validate();

    }

    /**
     * Test used to check that username does not contains
     * any slashes.
     */
    @Test(expected = ValidateException.class)
    public void testNoSlashesUserName() throws ValidateException {

        json = createJSON("a/b/c","b","c","d","e");
        EditUserCommand cmd = new EditUserCommand();
        cmd = gson.fromJson(json, EditUserCommand.class);

        cmd.validate();

    }

    /**
     * Test used to check a properly formatted creation.
     */
    @Test
    public void testValidationProperlyFormatted() throws ValidateException {
        json = createJSON("a","b","c","d","e");
        EditUserCommand cmd = new EditUserCommand();
        cmd = gson.fromJson(json, EditUserCommand.class);

        cmd.validate();

    }

    /**
     * Used to create JSON objects.
     *
     * @param un the username.
     * @param pw the password.
     * @param p the privileges.
     * @param n the name.
     * @param e the email.
     * @return String with the JSON.
     */
    private String createJSON(String un, String pw, String p, String n, String e) {

        JsonObject j = new JsonObject();
        j.addProperty("username", un);
        j.addProperty("password", pw);
        j.addProperty("privileges", p);
        j.addProperty("name", n);
        j.addProperty("email", e);

        return j.toString();

    }

}
