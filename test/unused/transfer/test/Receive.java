package unused.transfer.test;

import org.junit.Ignore;

import java.io.DataInputStream;
import java.io.IOException;
//import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Project: genomizer-Server
 * Package: transfer.Test
 * User: c08esn
 * Date: 4/28/14
 * Time: 9:54 AM
 * To change this template use File | Settings | File Templates.
 */

@Ignore
public class Receive implements Runnable {


//  private InputStream is;
    private ServerSocket welcome;
    private Socket listen;
    public Receive() {

        try {
            welcome = new ServerSocket(7777);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {

        try {
            byte[] info = new byte[1000];
            listen = welcome.accept();
            DataInputStream stream = new DataInputStream(listen.getInputStream());
            stream.read(info);
            System.out.println(new String(info));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
