package responses;


public class DownloadFileResponse extends Response {
    public String experimentID;
    public String fileName;
    public String size;
    public String type;
    public String URL;

    public DownloadFileResponse(String experimentID, String fileName,
            String size, String type, String URL) {
        super("downloadfile");
        this.experimentID = experimentID;
        this.fileName = fileName;
        this.size = size;
        this.type = type;
        this.URL = URL;
    }
}
