package transfer.Test;

import org.junit.Before;
import org.junit.Test;
import transfer.HttpURLDummy;

import java.net.MalformedURLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Created by c11epm on 4/28/14.
 */
public class HttpURLTest {
    private HttpURLDummy dummy;

    @Before
    public void setup() throws MalformedURLException {
        dummy = new HttpURLDummy("http://www8.cs.umu.se/~c11epm/thailand/IMG_20131227_105730.jpg");
    }

    @Test
    public void shouldNotBeNull() {
        assertNotNull(dummy);
    }

    @Test
    public void shouldConnectToURL() {
        assertTrue(dummy.connect());
    }
    @Test
    public void shouldReadDataFromURL(){
        shouldConnectToURL();
        assertTrue(dummy.readData());
    }

}
