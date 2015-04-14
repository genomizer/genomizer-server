package transfer.test;

import org.junit.Before;
import org.junit.Test;
import transfer.HTTPURLDownload;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;


/**
 * Created by c11epm on 4/28/14.
 */
public class HttpURLTest {
    private HTTPURLDownload dummy;

    @Before
    public void setup() throws MalformedURLException {
        String localFilePath = "/home/" +
                System.getProperty("user.name").substring(0,3) +
                "/" + System.getProperty("user.name") + "/testfile.jpg";
        dummy = new HTTPURLDownload("http://www8.cs.umu.se/~c11epm/thailand/IMG_20131227_105730.jpg", localFilePath);
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
        assertURLs(dummy.getURL(), "http://www8.cs.umu.se/~c11epm/thailand/IMG_20131227_105730.jpg");
    }

    @Test
    public void shouldChangeURL(){
        assertURLs(dummy.getURL(), "http://www8.cs.umu.se/~c11epm/thailand/IMG_20131227_105730.jpg");
        try {
            assertTrue(dummy.changeURL(new URL("http://www8.cs.umu.se/~c11epm/thailand/IMG_20131227_105734.jpg")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assertURLs(dummy.getURL(), "http://www8.cs.umu.se/~c11epm/thailand/IMG_20131227_105734.jpg");
    }

    private void assertURLs(String expected, String actual){
        assertEquals(expected, actual);
    }
}
