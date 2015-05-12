package command.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.admin.PutUserAdminCommand;
import command.ValidateException;
import database.constants.MaxLength;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PutUserAdminCommandTest {

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
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);
        assertNotNull(cmd);

    }

    /**
     * Test used to check that conversion to and from
     * JSON works properly.
     */
    @Test
    public void testConvertJSON() {

        json = createJSON("a","b","c","d","e");
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);
        String compare = gson.toJson(cmd);
        assertEquals(compare, json);

    }

    /**
     * Test used to check that username length validation works
     * properly.
     */
    @Test(expected = ValidateException.class)
    public void testValidateUsernameLength() throws ValidateException {

        String username = "";
        for(int i = 0; i < MaxLength.USERNAME+1; i++) {
            username = username + "a";
        }
        json = createJSON(username,"b","c","d","e");
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

        cmd.validate();
    }
    /**
     * Test used to check empty password
     */
    @Test(expected = ValidateException.class)
    public void testValidateUsernameNotEmpty() throws ValidateException {

        json = createJSON("","b","c","d", "e");
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

        cmd.validate();
    }

    /**
     * Test used to check null password
     */
    @Test(expected = ValidateException.class)
    public void testValidateUsernameNotNull() throws ValidateException {

        String s = null;
        json = createJSON(s, "b", "c", "d", "e");
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

        cmd.validate();
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
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

        cmd.validate();
    }
    /**
     * Test used to check empty password
     */
    @Test(expected = ValidateException.class)
    public void testValidatePasswordNotEmpty() throws ValidateException {

        json = createJSON("a","","c","d", "e");
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

        cmd.validate();
    }

    /**
     * Test used to check null password
     */
    @Test(expected = ValidateException.class)
    public void testValidatePasswordNotNull() throws ValidateException {

        String s = null;
        json = createJSON("", s, "c", "d", "e");
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

        cmd.validate();
    }
    /**
     * Test used to check that privileges length validation works
     * properly.
     */
    @Test(expected = ValidateException.class)
    public void testValidatePrivilegesLength() throws ValidateException {

        String priv = "";
        for(int i = 0; i < MaxLength.ROLE+1; i++) {
            priv = priv + "a";
        }
        json = createJSON("a","b","c",priv,"e");
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

        cmd.validate();
    }
    /**
     * Test used to check empty Privileges
     */
    @Test(expected = ValidateException.class)
    public void testValidatePrivilegesNotEmpty() throws ValidateException {

        json = createJSON("a","b","","d", "e");
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

        cmd.validate();
    }

    /**
     * Test used to check null Privileges
     */
    @Test(expected = ValidateException.class)
    public void testValidatePrivilegesNotNull() throws ValidateException {

        String s = null;
        json = createJSON("","b", s, "d", "e");
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

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
        json = createJSON("a","b",name,"d","e");
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

        cmd.validate();
    }
    /**
     * Test used to check empty name
     */
    @Test(expected = ValidateException.class)
    public void testValidateNameNotEmpty() throws ValidateException {

        json = createJSON("a","b","c","", "e");
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

        cmd.validate();
    }

    /**
     * Test used to check null name
     */
    @Test(expected = ValidateException.class)
    public void testValidateNameNotNull() throws ValidateException {

        String s = null;
        json = createJSON("","b","c", s, "e");
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

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
        json = createJSON("a","b","c","d",email);
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

        cmd.validate();
    }
    /**
     * Test used to check empty email
     */
    @Test(expected = ValidateException.class)
    public void testValidateEmailNotEmpty() throws ValidateException {

        json = createJSON("a","b","c","d", "");
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

        cmd.validate();
    }

    /**
     * Test used to check null email
     */
    @Test(expected = ValidateException.class)
    public void testValidateEmailNotNull() throws ValidateException {

        String s = null;
        json = createJSON("","b","c","d",s);
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

        cmd.validate();
    }

    /**
     * Test used to check that username does not contains
     * any slashes.
     */
    @Test(expected = ValidateException.class)
    public void testNoSlashesUserName() throws ValidateException {

        json = createJSON("a/b/c","b","c","d","e");
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

        cmd.validate();
    }

    /**
     * Test used to check a properly formatted creation.
     */
    @Test
    public void testValidationProperlyFormatted() throws ValidateException {
        json = createJSON("a","b","c","d","e");
        PutUserAdminCommand cmd = gson.fromJson(json, PutUserAdminCommand.class);

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