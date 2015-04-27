package test.command.test;

import command.Command;
import command.DeleteAnnotationValueCommand;
import command.ValidateException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeleteAnnotationValueCommandTest {

    private DeleteAnnotationValueCommand com;


    @Test
    public void testHavingUserRights() throws ValidateException {

        com = new DeleteAnnotationValueCommand("name", "string", Command.UserType.USER);
        com.validate();
        com = new DeleteAnnotationValueCommand("name", "string", Command.UserType.ADMIN);
        com.validate();
    }

    @Test(expected = ValidateException.class)
    public void testNotHavingUserRights1() throws ValidateException {

        com = new DeleteAnnotationValueCommand("name", "string", Command.UserType.GUEST);
        com.validate();
        fail();
    }

    @Test(expected = ValidateException.class)
    public void testNotHavingUserRights2() throws ValidateException {

        com = new DeleteAnnotationValueCommand("name", "string", Command.UserType.UNKNOWN);
        com.validate();
        fail();
    }

}