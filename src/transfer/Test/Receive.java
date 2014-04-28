package transfer.Test;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
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

    public class Receive extends Thread {


    private InputStream is;
    private ServerSocket welcome;
    private Socket listen;
    public Receive() {

        try {
            welcome = new ServerSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {

    try {
            listen = welcome.accept();
        DataInputStream stream = new DataInputStream(listen.getInputStream());
            System.out.println(stream.read());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
