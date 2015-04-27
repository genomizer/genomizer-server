package test.command.test;

import static org.junit.Assert.*;

import command.Command;
import command.LoginCommand;
import command.ValidateException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class CommandTest {

    private Command com;

    @Before
    public void setUp(){
        com = new LoginCommand();
    }

    @Test(expected = ValidateException.class)
    public void testReturnFalseForUnknown() throws ValidateException {

        com.hasRights(Command.UserType.UNKNOWN, Command.UserType.UNKNOWN);
        fail();
    }

    @Test
    public void testReturnTrueForEqual() throws ValidateException {

        com.hasRights(Command.UserType.ADMIN, Command.UserType.ADMIN);
        com.hasRights(Command.UserType.USER, Command.UserType.USER);
        com.hasRights(Command.UserType.GUEST, Command.UserType.GUEST);
    }

    @Test
    public void testReturnTrueForAdmin() throws ValidateException {

        com.hasRights(Command.UserType.USER, Command.UserType.ADMIN);
        com.hasRights(Command.UserType.GUEST, Command.UserType.ADMIN);
        com.hasRights(Command.UserType.UNKNOWN, Command.UserType.ADMIN);
    }

    @Test(expected = ValidateException.class)
    public void testReturnFalseWithoutAdminRights1() throws ValidateException {

        com.hasRights(Command.UserType.ADMIN, Command.UserType.USER);
        fail();
    }

    @Test(expected = ValidateException.class)
    public void testReturnFalseWithoutAdminRights2() throws ValidateException {

        com.hasRights(Command.UserType.ADMIN, Command.UserType.GUEST);
        fail();
    }

    @Test(expected = ValidateException.class)
    public void testReturnFalseWithoutAdminRights3() throws ValidateException {

        com.hasRights(Command.UserType.ADMIN, Command.UserType.UNKNOWN);
        fail();
    }

    @Test(expected = ValidateException.class)
    public void testReturnFalseWithoutUserRights1() throws ValidateException {

        com.hasRights(Command.UserType.USER, Command.UserType.GUEST);
        fail();
    }

    @Test(expected = ValidateException.class)
    public void testReturnFalseWithoutUserRights2() throws ValidateException {

        com.hasRights(Command.UserType.USER, Command.UserType.UNKNOWN);
        fail();
    }

    @Test
    public void testReturnTrueWithGuestRights() throws ValidateException {

        com.hasRights(Command.UserType.GUEST, Command.UserType.USER);
    }

    @Test(expected = ValidateException.class)
    public void testReturnFalseWithoutGuestRights() throws ValidateException {

        com.hasRights(Command.UserType.GUEST, Command.UserType.UNKNOWN);
        fail();
    }

    @Test(expected = ValidateException.class)
    public void testDoNotAllowUsersForUnknown1() throws ValidateException {

        com.hasRights(Command.UserType.UNKNOWN, Command.UserType.USER);
        fail();
    }

    @Test(expected = ValidateException.class)
    public void testDoNotAllowUsersForUnknown2() throws ValidateException {

        com.hasRights(Command.UserType.UNKNOWN, Command.UserType.GUEST);
        fail();
    }

}