package command.test.annotation;

import command.Command;
import command.annotation.DeleteAnnotationValueCommand;
import command.ValidateException;
import database.subClasses.UserMethods.UserType;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeleteAnnotationValueCommandTest {

    /**
     * Test used to check that ValidateException is not thrown
     * when the user have the required rights.
     *
     * @throws ValidateException
     */
    @Test
    public void testHavingRights() throws ValidateException {

        Command c = new DeleteAnnotationValueCommand();
        c.setFields("/test/uri/name/value", null, null, UserType.USER);
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

        Command c = new DeleteAnnotationValueCommand();
        c.setFields("/test/uri/name/value", null, null, UserType.GUEST);
        c.validate();
        fail();
    }

}