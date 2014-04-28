package transfer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Project: genomizer-Server
 * Package: transfer
 * User: c08esn
 * Date: 4/25/14
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class UploadCommand extends Command {

    private String path;
    private Socket sendSocket;


    public UploadCommand(String path, Socket sendSocket) {
        this.sendSocket = sendSocket;
        this.path = "/file/ex1.raw";
        receive r = new receive();
        r.start();




    }



    @Override
    public void Execute() {
        try {
            byte[] sendData = path.getBytes();
            OutputStream output = sendSocket.getOutputStream();
            output.write(sendData);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String getPath() {
        return path;
    }



    public String pathToURL(){
        return "GET "+path+" HTTP/1.1";


    }



   public class receive extends Thread {


       @Override
       public void run() {

       }
   }
}
