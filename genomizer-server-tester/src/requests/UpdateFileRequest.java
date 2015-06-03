package requests;

/**
 * Request for sending the update file command.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class UpdateFileRequest extends Request {

    /**
     * Request to send.
     * @param fileID The file-id to update.
     */
    public UpdateFileRequest(String fileID) {
        super("updatefilerequest", "/file/" + fileID, "PUT");
    }
}
