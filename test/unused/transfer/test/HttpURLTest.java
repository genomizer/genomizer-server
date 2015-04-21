package unused.transfer.test;

import org.junit.Before;
import org.junit.Test;
import unused.transfer.HTTPURLDownload;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;


/**
 * Created by c11epm on 4/28/14.
 */
public class HttpURLTest {
    private HTTPURLDownload dummy;
    private final static String testURL  = "http://i.imgur.com/8uuZIMp.jpg";
    private final static String testURL2 = "http://i.imgur.com/jwp9SpW.jpg";

    @Before
    public void setup() throws MalformedURLException {
        String localFilePath = "/tmp/testfile.jpg";
        dummy = new HTTPURLDownload(testURL, localFilePath);
    }

    @Test
    public void shouldNotBeNull() {
        assertNotNull(dummy);
    }

    @Test
    public void shouldConnectToURL() {
        System.out.println();

        assertTrue(dummy.openStreams());
    }
    @Test
    public void shouldReadDataFromURL(){
        shouldConnectToURL();
        assertTrue(dummy.readData());
    }
    @Test
    public void shouldMatchURL(){
        assertURLs(dummy.getURL(), testURL);
    }

    @Test
    public void shouldChangeURL(){
        assertURLs(dummy.getURL(), testURL);
        try {
            assertTrue(dummy.changeURL(new URL(testURL2)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assertURLs(dummy.getURL(), testURL2);
    }

    private void assertURLs(String expected, String actual){
        assertEquals(expected, actual);
    }
}
