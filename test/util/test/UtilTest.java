package util.test;

import static util.Util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;

public class UtilTest {

    @Test(expected=IOException.class)
    public void absolutePathsAreNotAllowed() throws IOException {
        validatePath("/an/absolute/path");
    }

    @Test(expected=IOException.class)
    public void pathsEndingWithSlashAreNotAllowed() throws IOException {
        validatePath("path/ending/with/slash/");
    }

    @Test(expected=IOException.class)
    public void neitherArePathsEndingAndStartingWithSlash() throws IOException {
        validatePath("/path/ending/and/starting/with/slash/");
    }

    @Test(expected=IOException.class)
    public void dotsAreNotAllowed() throws IOException {
        validatePath("a/path/with/a/dot/./in/it");
    }

    @Test(expected=IOException.class)
    public void doubleDotsAreNotAllowed() throws IOException {
        validatePath("a/path/with/a/double/dot/../in/it");
    }

    @Test(expected=IOException.class)
    public void multipleSlashesAreNotAllowed() throws IOException {
        validatePath("a/path/with/a/double/slash//in/it");
    }

    @Test(expected=IOException.class)
    public void invalidCharactersAreNotAllowed() throws IOException {
        validatePath("a/path/with/some/invalid!?#$%^*/characters/in/it");
    }

    @Test
    public void parseURIWithEncodedSpaces() 
    		throws URISyntaxException, UnsupportedEncodingException {
      HashMap<String, String> outParams = new HashMap<>();
      String str = "http://server.com/some/path?foo=bar%20baar&baz=quux";
      String path = parseURI(new URI(str), outParams);

      assertEquals("/some/path", path);
      assertEquals("bar baar", outParams.get("foo"));
      assertEquals("bar baar", URLDecoder.decode(outParams.get("foo"), "UTF-8"));
      assertEquals("quux", outParams.get("baz"));
    }

    @Test
    public void someSeeminglyInvalidCharactersAreAllowed() throws IOException {
        validatePath("a/path/with/some/seemingly/invalid/ÅÄÖAWE90a/characters/in/it");
    }
    @Test
    public void formatTimeDifferenceTest() {
        long oneDay = 24 * 60 * 60 * 1000;
        assertEquals("1 day, 0 ms",
                formatTimeDifference(oneDay));

        long twoDays = oneDay*2;
        assertEquals("2 days, 0 ms",
                formatTimeDifference(twoDays));

        long twoDays15Hours = twoDays + 15*60*60*1000;
        assertEquals("2 days, 15 hours, 0 ms",
                formatTimeDifference(twoDays15Hours));

        long twoDays15Hours45Minutes = twoDays15Hours + 45*60*1000;
        assertEquals("2 days, 15 hours, 45 minutes, 0 ms",
                formatTimeDifference(twoDays15Hours45Minutes));

        long twoDays15Hours45Minutes11Seconds = twoDays15Hours45Minutes + 11*1000;
        assertEquals("2 days, 15 hours, 45 minutes, 11 seconds, 0 ms",
                formatTimeDifference(twoDays15Hours45Minutes11Seconds));

    }

}
