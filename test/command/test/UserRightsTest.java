package command.test;

import static org.junit.Assert.*;

import command.Command;
import command.annotation.DeleteAnnotationValueCommand;
import command.ValidateException;
import database.subClasses.UserMethods.UserType;
import org.junit.Test;

public class UserRightsTest {

    private Command com;


    @Test(expected = ValidateException.class)
    public void testReturnFalseForUnknown() throws ValidateException {

        com = new DeleteAnnotationValueCommand();
        com.setFields("/test/name/value/testname",null, UserType.UNKNOWN);
        com.hasRights(UserType.UNKNOWN);
        fail();
    }

    @Test
    public void testReturnTrueForEqual() throws ValidateException {

        com = new DeleteAnnotationValueCommand();

        com.setFields("/test/name/value/testname",null, UserType.ADMIN);
        com.hasRights(UserType.ADMIN);

        com.setFields("/test/name/value/testname",null, UserType.USER);
        com.hasRights(UserType.USER);

        com.setFields("/test/name/value/testname",null, UserType.GUEST);
        com.hasRights(UserType.GUEST);
    }

    @Test
    public void testReturnTrueForAdmin() throws ValidateException {

        com = new DeleteAnnotationValueCommand();
        com.setFields("/test/name/value/testname",null, UserType.ADMIN);
        com.hasRights(UserType.USER);
        com.hasRights(UserType.GUEST);
        com.hasRights(UserType.UNKNOWN);
    }

    @Test(expected = ValidateException.class)
    public void testReturnFalseWithoutAdminRights1() throws ValidateException {

        com = new DeleteAnnotationValueCommand();
        com.setFields("/test/name/value/testname",null, UserType.USER);
        com.hasRights(UserType.ADMIN);
        fail();
    }

    @Test(expected = ValidateException.class)
    public void testReturnFalseWithoutAdminRights2() throws ValidateException {

        com = new DeleteAnnotationValueCommand();
        com.setFields("/test/name/value/testname",null, UserType.GUEST);
        com.hasRights(UserType.ADMIN);
        fail();
    }

    @Test(expected = ValidateException.class)
    public void testReturnFalseWithoutUserRights1() throws ValidateException {

        com = new DeleteAnnotationValueCommand();
        com.setFields("/test/name/valu/testname",null, UserType.GUEST);
        com.hasRights(UserType.USER);
        fail();
    }

    @Test(expected = ValidateException.class)
    public void testDoNotAllowUsersForUnknown2() throws ValidateException {

        com = new DeleteAnnotationValueCommand();
        com.setFields("/test/name/value/testname",null, UserType.GUEST);
        com.hasRights(UserType.UNKNOWN);
        fail();
    }

    @Test(expected = ValidateException.class)
    public void testReturnFalseWithoutAdminRights3() throws ValidateException {

        com = new DeleteAnnotationValueCommand();
        com.setFields("/test/name/value/testname",null, UserType.UNKNOWN);
        com.hasRights(UserType.ADMIN);
        fail();
    }

    @Test(expected = ValidateException.class)
    public void testReturnFalseWithoutUserRights2() throws ValidateException {

        com = new DeleteAnnotationValueCommand();
        com.setFields("/test/name/value/testname",null, UserType.UNKNOWN);
        com.hasRights(UserType.USER);
        fail();
    }

    @Test(expected = ValidateException.class)
    public void testReturnFalseWithoutGuestRights() throws ValidateException {

        com = new DeleteAnnotationValueCommand();
        com.setFields("/test/name/value/testname",null, UserType.UNKNOWN);
        com.hasRights(UserType.GUEST);
        fail();
    }

    @Test
    public void testReturnTrueWithGuestRights() throws ValidateException {

        com = new DeleteAnnotationValueCommand();
        com.setFields("/test/name/value/testname",null, UserType.USER);
        com.hasRights(UserType.GUEST);
    }

    @Test(expected = ValidateException.class)
    public void testDoNotAllowUsersForUnknown1() throws ValidateException {

        com = new DeleteAnnotationValueCommand();
        com.setFields("/test/name/value/testname",null, UserType.USER);
        com.hasRights(UserType.UNKNOWN);
        fail();
    }

}