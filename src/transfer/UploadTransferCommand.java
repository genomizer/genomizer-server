package transfer;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Project: genomizer-Server
 * Package: transfer
 * User: c08esn
 * Date: 4/25/14
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class UploadTransferCommand implements TransferCommand {

    private String path;
    private Socket sendSocket;


    public UploadTransferCommand(String path) {
        this.path = "/file/ex1.raw";
    }



    public void execute() {
        try {
            InetAddress a = InetAddress.getByName("127.0.0.1");
            sendSocket = new Socket(a,7777);

            byte[] sendData = dlLinkJSON().getBytes();
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
        return "POST "+"http://ip/DB/"+path+":8080"+" HTTP/1.1";
    }


    public String dlLinkJSON(){
        return "200 (OK) \n" +
                "Content-Type: application/json \n\n" +
                "{\n" +
                "upload-link: "+pathToURL()+"\n" +
                "}";
    }







}
