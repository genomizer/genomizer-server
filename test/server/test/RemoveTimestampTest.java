package server.test;

import static org.junit.Assert.*;

import command.ValidateException;
import org.junit.Before;
import org.junit.Test;
import server.RequestHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class RemoveTimestampTest {

    String uri;
    String uri2;
    Method method;

    @Test
    public void testRemoveNormalTimestamp() throws ValidateException {
        uri = "temp_=123";
        uri2 = removeTimeStamp(uri);

        assertEquals("temp", uri2);
    }

    @Test
    public void testDontRemoveIfNoTimestamp() throws ValidateException {
        uri = "temp_thr&ee=123_543=ref";
        uri2 = removeTimeStamp(uri);

        assertEquals(uri, uri2);
    }

    @Test
    public void testDontRemoveIfCharacterTimestamp() throws ValidateException {
        uri = "temp_three&_=abc";
        uri2 = removeTimeStamp(uri);

        assertEquals(uri, uri2);
    }

    @Test
    public void testAllNumbersInTheTimestamp() throws ValidateException {
        uri = "temp_three_=1234567890";
        uri2 = removeTimeStamp(uri);

        assertEquals("temp_three", uri2);
    }

    @Test
    public void testOnlyRemoveTheTimestamp() throws ValidateException {
        uri = "temp_three_=123 test=689";
        uri2 = removeTimeStamp(uri);

        assertEquals("temp_three test=689", uri2);
    }

    @Test
    public void testShouldBreakAtLetters() throws ValidateException {
        uri = "temp_three_=12345s7890";
        uri2 = removeTimeStamp(uri);

        assertEquals("temp_threes7890", uri2);
    }

    @Test
    public void testRemoveTimestampWithAndSign() throws ValidateException {
        uri = "temp_three=123&_=467";
        uri2 = removeTimeStamp(uri);

        assertEquals("temp_three=123", uri2);
    }

    @Test
    public void testRemoveTheFollowingAndSign() throws ValidateException {
        uri = "temp_three_=123&_test=689";
        uri2 = removeTimeStamp(uri);

        assertEquals("temp_three_test=689", uri2);
    }

    @Test
    public void testOnlyRemoveOneAndSign() throws ValidateException {
        uri = "temp_three=345&_=123&_test=689";
        uri2 = removeTimeStamp(uri);

        assertEquals("temp_three=345&_test=689", uri2);
    }

    @Test
    public void testRemoveTimestampWithQuestionMark() throws ValidateException {
        uri = "temp?_=467";
        uri2 = removeTimeStamp(uri);

        assertEquals("temp", uri2);
    }

    @Test
    public void testDontRemoveQuestionMarkIfMoreInQuery() throws ValidateException {
        uri = "temp?_=467&test=abc";
        uri2 = removeTimeStamp(uri);

        assertEquals("temp?test=abc", uri2);
    }


    /* Finds the remove timestamp method in RequestHandler and invokes it.*/
    private String removeTimeStamp(String uri){

        try {
            Class param[] = new Class[1];
            param[0] = String.class;
            method = RequestHandler.class.getDeclaredMethod(
                    "removeTimeStamp", param);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        method.setAccessible(true);

        try {
            return (String) method.invoke(new RequestHandler(),uri);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return "";
    }

}
