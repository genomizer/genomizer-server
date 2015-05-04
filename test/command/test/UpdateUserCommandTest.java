package command.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.UpdateUserCommand;
import command.ValidateException;
import database.constants.MaxLength;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UpdateUserCommandTest {

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

        json = createJSON("a","b","c","d");
        UpdateUserCommand cmd = new UpdateUserCommand();
        cmd = gson.fromJson(json, UpdateUserCommand.class);
        assertNotNull(cmd);

    }

    /**
     * Test used to check that conversion to and from
     * JSON works properly.
     */
    @Test
    public void testConvertJSON() {

        json = createJSON("a","b","c","d");
        UpdateUserCommand cmd = new UpdateUserCommand();
        cmd = gson.fromJson(json, UpdateUserCommand.class);
        String compare = gson.toJson(cmd);
        assertEquals(compare, json);

    }

    /**
     * Test used to check that oldpassword validation works
     * properly.
     */
    @Test(expected = ValidateException.class)
    public void testValidateUsernameLength() throws ValidateException {

        String password = "";
        for(int i = 0; i < MaxLength.PASSWORD+1; i++) {
            password = password + "a";
        }
        json = createJSON(password,"b","c","d");
        UpdateUserCommand cmd = new UpdateUserCommand();
        cmd = gson.fromJson(json, UpdateUserCommand.class);

        json = createJSON("","b","c","d");
        UpdateUserCommand cmd2 = new UpdateUserCommand();
        cmd2 = gson.fromJson(json, UpdateUserCommand.class);

        cmd.validate();
        cmd2.validate();
    }

    /**
     * Test used to check that newpassword validation works
     * properly.
     */
    @Test(expected = ValidateException.class)
    public void testValidatePasswordLength() throws ValidateException {

        String password = "";
        for(int i = 0; i < MaxLength.PASSWORD+1; i++) {
            password = password + "a";
        }
        json = createJSON("a",password,"c","d");
        UpdateUserCommand cmd = new UpdateUserCommand();
        cmd = gson.fromJson(json, UpdateUserCommand.class);

        json = createJSON("a","","c","d");
        UpdateUserCommand cmd2 = new UpdateUserCommand();
        cmd2 = gson.fromJson(json, UpdateUserCommand.class);

        cmd.validate();
        cmd2.validate();
    }

    /**
     * Test used to check that name validation works
     * properly.
     */
    @Test(expected = ValidateException.class)
    public void testValidateNameLength() throws ValidateException {

        String name = "";
        for(int i = 0; i < MaxLength.FULLNAME+1; i++) {
            name = name + "a";
        }
        json = createJSON("a","b",name,"d");
        UpdateUserCommand cmd = new UpdateUserCommand();
        cmd = gson.fromJson(json, UpdateUserCommand.class);

        json = createJSON("a","b","","d");
        UpdateUserCommand cmd2 = new UpdateUserCommand();
        cmd2 = gson.fromJson(json, UpdateUserCommand.class);

        cmd.validate();
        cmd2.validate();

    }
    /**
     * Test used to check that email validation works
     * properly.
     */
    @Test(expected = ValidateException.class)
    public void testValidateEmailLength() throws ValidateException {

        String email = "";
        for(int i = 0; i < MaxLength.EMAIL+1; i++) {
            email = email + "a";
        }
        json = createJSON("a","b",email,"d");
        UpdateUserCommand cmd = new UpdateUserCommand();
        cmd = gson.fromJson(json, UpdateUserCommand.class);

        json = createJSON("a","b","","d");
        UpdateUserCommand cmd2 = new UpdateUserCommand();
        cmd2 = gson.fromJson(json, UpdateUserCommand.class);

        cmd.validate();
        cmd2.validate();

    }

    /**
     * Test used to check that oldpassword does not contains
     * any slashes.
     */
    @Test(expected = ValidateException.class)
    public void testNoSlashesOldPassword() throws ValidateException {

        json = createJSON("a/b/c","b","c","d");
        UpdateUserCommand cmd = new UpdateUserCommand();
        cmd = gson.fromJson(json, UpdateUserCommand.class);
        cmd.validate();

    }

    /**
     * Test used to check that newpassword does not contains
     * any slashes.
     */
    @Test(expected = ValidateException.class)
    public void testNoSlashesNewPassword() throws ValidateException {

        json = createJSON("a/b/c","b","c","d");
        UpdateUserCommand cmd = new UpdateUserCommand();
        cmd = gson.fromJson(json, UpdateUserCommand.class);
        cmd.validate();

    }
    /**
     * Test used to check a properly formatted creation.
     */
    @Test
    public void testValidationProperlyFormatted() throws ValidateException {
        json = createJSON("a","b","c","d");
        UpdateUserCommand cmd = new UpdateUserCommand();
        cmd = gson.fromJson(json, UpdateUserCommand.class);

        cmd.validate();

    }

    /**
     * Used to create JSON objects.
     *
     * @param opw the oldpassword.
     * @param npw the newpassword.
     * @param n the name.
     * @param e the email.
     * @return String with the JSON.
     */
    private String createJSON(String opw, String npw, String n, String e) {

        JsonObject j = new JsonObject();
        j.addProperty("oldPassword", opw);
        j.addProperty("newPassword", npw);
        j.addProperty("name", n);
        j.addProperty("email", e);

        return j.toString();

    }

}