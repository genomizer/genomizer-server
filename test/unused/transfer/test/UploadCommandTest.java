package unused.transfer.test;

import org.junit.Before;
import org.junit.Test;
import unused.transfer.UploadCommand;


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




    private String path = "POST http://ip/DB//file/ex1.raw:8080 HTTP/1.1";

    @Before
    public void setup() {



        uploadCommand = new UploadCommand("fake path");
    }

    @Test
    public void shouldHavePath() {
        assertNotNull(uploadCommand.getPath());
    }

    @Test
    public void shouldhaveCorrectPath(){
        assertEquals(uploadCommand.pathToURL(),path);
    }


    @Test
    public void shouldHaveJson(){
//        System.out.println(uploadCommand.dlLinkJSON());
        assertNotNull(uploadCommand.dlLinkJSON());
    }

    @Test
    public void shouldSendOverSocket(){
        Receive res = new Receive();
        Thread t = new Thread(res);
        t.start();
        uploadCommand.execute();

    }




}
