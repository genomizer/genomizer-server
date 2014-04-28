package transfer.Test;

import org.junit.Before;
import org.junit.Test;
import transfer.HttpURLDummy;

import static org.junit.Assert.*;


/**
 * Created by c11epm on 4/28/14.
 */
public class HttpURLTest {
    private HttpURLDummy dummy;
    @Before
    public void setup(){
        dummy = new HttpURLDummy();
    }
    @Test
    public void shouldBeNull(){
        assertNull(dummy);
    }

    @Test
    public void shouldReturnRequestMethod(){

    }

}
