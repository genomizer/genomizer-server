package unused.transfer;

//import java.net.InetAddress;
//import java.net.UnknownHostException;

public class DownloadCommand implements Command {

    private String filePath;

    public DownloadCommand(String filePath) {
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



