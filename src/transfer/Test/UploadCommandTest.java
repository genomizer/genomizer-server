package transfer.Test;

import org.junit.Before;
import org.junit.Test;
import transfer.UploadCommand;

import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static transfer.UploadCommand.*;

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
    private Receive res;



    private String svar = "POST http://ip/DB//file/ex1.raw:8080 HTTP/1.1";

    @Before
    public void setup() {

        res = new Receive();
        res.start();
        s = new Socket();
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


    @Test
    public void shouldHaveJson(){
        System.out.println(uploadCommand.dlLinkJSON());
        assertNotNull(uploadCommand.dlLinkJSON());
    }




}
