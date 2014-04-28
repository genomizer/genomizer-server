package transfer.Test;

import org.junit.Before;
import org.junit.Test;
import transfer.UploadCommand;

import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Project: genomizer-Server
 * Package: transfer.Test
 * User: c08esn
 * Date: 4/25/14
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class UploadCommandTest {

    private UploadCommand uploadCommand;
    private Socket s ;

    private String svar = "GET /file/ex1.raw HTTP/1.1";

    @Before
    public void setup() {
        s  = new Socket();
        uploadCommand = new UploadCommand("fake path",s);
    }

    @Test
    public void shouldHavePath() {
        assertNotNull(uploadCommand.getPath());
    }

    @Test
    public void shouldhaveSvar(){
        assertEquals(uploadCommand.pathToURL(),svar);
    }



}
