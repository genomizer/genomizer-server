package requests;

/**
 * Request for downloading a file.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class DownloadFileRequest extends Request {

    /**
     * Constructor creating the request.
     * @param fileID String representing the file id.
     * @param fileFormat String representing the format of the file.
     */
    public DownloadFileRequest(String fileID, String fileFormat) {
        super("downloadfile", "/file/" + fileID, "GET");
    }

}
