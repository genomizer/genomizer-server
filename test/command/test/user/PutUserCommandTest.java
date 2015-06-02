package command.test.user;

import authentication.Authenticate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.user.PutUserCommand;
import command.ValidateException;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PutUserCommandTest {

    private Gson gson = null;
    private String json = null;
    private String uuid;

    /**
     * Setup method to initiate GSON builder.
     */
    @Before
    public void setUp() {

        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        gson = builder.create();
        uuid = Authenticate.updateActiveUser(null, "testuser");
    }

    /**
     * Test used to check that creation is not null.
     */
    @Test
    public void testCreationNotNull() {

        json = createJSON("a","b","c","d");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        assertNotNull(cmd);
    }

    /**
     * Test used to check that conversion to and from
     * JSON works properly.
     */
    @Test
    public void testConvertJSON() {

        json = createJSON("a","b","c","d");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        String compare = gson.toJson(cmd);
        assertEquals(compare, json);

    }

    /**
     * Test used to check that oldpassword length validation works
     * properly.
     */
    @Test(expected = ValidateException.class)
    public void testValidateOldPwdLength() throws ValidateException {

        String password = "";
        for(int i = 0; i < MaxLength.PASSWORD+1; i++) {
            password = password + "a";
        }
        json = createJSON(password,"b","c","d");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        cmd.setFields("uri", null, uuid, UserType.ADMIN);

        cmd.validate();
    }

    /**
     * Test used to check empty oldpassword
     */
    @Test(expected = ValidateException.class)
    public void testValidateOldPwdNotEmpty() throws ValidateException {

        json = createJSON("","b","c","d");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        cmd.setFields("uri", null, uuid, UserType.ADMIN);

        cmd.validate();
    }

    /**
     * Test used to check null oldpassword
     */
    @Test(expected = ValidateException.class)
    public void testValidateOldPwdNotNull() throws ValidateException {
        json = createJSON(null,"b","c","d");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        cmd.setFields("uri", null, uuid, UserType.ADMIN);

        cmd.validate();
    }

    /**
     * Test used to check that newpassword length validation works
     * properly.
     */
    @Test(expected = ValidateException.class)
    public void testValidateNewPasswordLength() throws ValidateException {

        String password = "";
        for(int i = 0; i < MaxLength.PASSWORD+1; i++) {
            password = password + "a";
        }
        json = createJSON("a",password,"c","d");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        cmd.setFields("uri", null, uuid, UserType.ADMIN);

        cmd.validate();
    }

    /**
     * Test used to check empty newpassword
     */
    @Test(expected = ValidateException.class)
    public void testValidateNewPwdNotEmpty() throws ValidateException {

        json = createJSON("a","","c","d");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        cmd.setFields("uri", null, uuid, UserType.ADMIN);

        cmd.validate();
    }

    /**
     * Test used to check null newpassword
     */
    @Test(expected = ValidateException.class)
    public void testValidateNewPwdNotNull() throws ValidateException {
        json = createJSON("",null,"c","d");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        cmd.setFields("uri", null, uuid, UserType.ADMIN);

        cmd.validate();
    }

    /**
     * Test used to check that name length validation works
     * properly.
     */
    @Test(expected = ValidateException.class)
    public void testValidateNameLength() throws ValidateException {

        String name = "";
        for(int i = 0; i < MaxLength.FULLNAME+1; i++) {
            name = name + "a";
        }
        json = createJSON("a","b",name,"d");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        cmd.setFields("uri", null, uuid, UserType.ADMIN);

        cmd.validate();
    }
    /**
     * Test used to check empty name
     */
    @Test(expected = ValidateException.class)
    public void testValidateNameNotEmpty() throws ValidateException {

        json = createJSON("a","b","","d");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        cmd.setFields("uri", null, uuid, UserType.ADMIN);

        cmd.validate();
    }

    /**
     * Test used to check null name
     */
    @Test(expected = ValidateException.class)
    public void testValidateNameNotNull() throws ValidateException {
        json = createJSON("","b",null,"d");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        cmd.setFields("uri", null, uuid, UserType.ADMIN);

        cmd.validate();
    }
    /**
     * Test used to check that email length validation works
     * properly.
     */
    @Test(expected = ValidateException.class)
    public void testValidateEmailLength() throws ValidateException {

        String email = "";
        for(int i = 0; i < MaxLength.EMAIL+1; i++) {
            email = email + "a";
        }
        json = createJSON("a","b",email,"d");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        cmd.setFields("uri", null, uuid, UserType.ADMIN);

        cmd.validate();
    }
    /**
     * Test used to check empty email
     */
    @Test(expected = ValidateException.class)
    public void testValidateEmailNotEmpty() throws ValidateException {

        json = createJSON("a","b","c","");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        cmd.setFields("uri", null, uuid, UserType.ADMIN);

        cmd.validate();
    }

    /**
     * Test used to check null email
     */
    @Test(expected = ValidateException.class)
    public void testValidateEmailNotNull() throws ValidateException {
        json = createJSON("","b","c",null);
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        cmd.setFields("uri", null, uuid, UserType.ADMIN);

        cmd.validate();
    }
    /**
     * Test used to check that oldpassword does not contains
     * any slashes.
     */
    @Test(expected = ValidateException.class)
    public void testNoSlashesOldPassword() throws ValidateException {

        json = createJSON("a/b/c","b","c","d");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        cmd.setFields("uri", null, uuid, UserType.ADMIN);
        cmd.validate();

    }

    /**
     * Test used to check that newpassword does not contains
     * any slashes.
     */
    @Test(expected = ValidateException.class)
    public void testNoSlashesNewPassword() throws ValidateException {

        json = createJSON("a","a/b/c","c","d");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        cmd.setFields("uri", null, uuid, UserType.ADMIN);
        cmd.validate();

    }
    /**
     * Test used to check a properly formatted creation.
     */
    @Test
    public void testValidationProperlyFormatted() throws ValidateException {
        json = createJSON("a","b","c","d");
        PutUserCommand cmd = gson.fromJson(json, PutUserCommand.class);
        cmd.setFields("uri", null, uuid, UserType.ADMIN);
        cmd.setUsername("");
        cmd.validate();

    }

    /**
     * Test used to check that ValidateException is not thrown
     * when the user have the required rights.
     *
     * @throws ValidateException
     */
    @Test
    public void testHavingRights() throws ValidateException {

        json = createJSON("a","b","c","d");
        PutUserCommand c = gson.fromJson(json, PutUserCommand.class);
        c.setFields("uri", null, "uuid", UserType.USER);

        c.validate();
    }

    /**
     * Test used to check that ValidateException is thrown
     * when the user doesn't have the required rights.
     *
     * @throws ValidateException
     */
    @Test(expected = ValidateException.class)
    public void testNotHavingRights() throws ValidateException {

        json = createJSON("a","b","c","d");
        PutUserCommand c = gson.fromJson(json, PutUserCommand.class);
        c.setFields("uri", null, "uuid", UserType.GUEST);

        c.validate();
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