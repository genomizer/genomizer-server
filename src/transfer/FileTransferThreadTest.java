package transfer;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Project: genomizer-Server
 * Package: transfer
 * User: c08esn
 * Date: 4/25/14
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileTransferThreadTest {
    FileTransferThread f ;
    Command_Object c ;
    @org.junit.Before
    public void setUp() throws Exception {
        f  = new FileTransferThread();
        c  = new Command_Object();
    }


    @Test
    public void shouldNotHaveEmptyQueue(){
        assertTrue(f.addWorkToQueue(c));
    }

    @Test
    public void shouldHaveWorkInQueue(){
        f.addWorkToQueue(c);
        assertTrue(f.popWorkFromQueue());
    }

    @Test
    public void shouldBeEmptyQueue(){
        assertEquals(0,f.numberOfWorkInQueue());
    }



    @org.junit.After
    public void tearDown() throws Exception {

    }
}
