package transfer;

//import java.net.InetAddress;
//import java.net.UnknownHostException;

public class DownloadTransferCommand implements TransferCommand {

    private String filePath;

    public DownloadTransferCommand(String filePath) {
        this.filePath = filePath;

    }


    public void execute() {
        urlFactory();



    }

    private String urlFactory() {
        String url;


        url = "file://" + "webservernamn" + "/" + filePath;

        return url;

    }
}



