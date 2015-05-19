package server.test;

import static org.junit.Assert.*;

import command.ValidateException;
import org.junit.Test;


public class RemoveTimestampTest {

    String uri;
    String uri2;

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


    /* Finds the timestamp and removes it.*/
    private String removeTimeStamp(String uri){

        String newUri;

        if (!uri.contains("_="))
            return uri;

        int pos = uri.lastIndexOf("_=");
        int length = uri.length();
        int end = pos +2;

        if (length <= end ){
            return uri;
        }

        if (!isDigit(uri.charAt(end))){
            return uri;
        }

        while(length > end && isDigit(uri.charAt(end))){
            end++;
        }

        if (length > end && uri.charAt(end) == '&'){
            end ++;
        }
        else if (pos > 0 && uri.charAt(pos-1) == '&' || uri.charAt(pos-1) == '?') {
            pos -= 1;
        }

        newUri = uri.substring(0,pos) + uri.substring(end);

        return newUri;
    }

    //Used to simplify the code when checking for digits
    private boolean isDigit(char c){
            return ('0' <= c && c <= '9');
    }

}
