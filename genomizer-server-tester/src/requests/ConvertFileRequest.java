package requests;

/**
 * Request for converting a file.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class ConvertFileRequest extends Request{
    private String fileid;
    private String toformat;

    /**
     * Creates the command for converting files.
     * @param fileID The file-id to convert.
     * @param format The format to convert to.
     */
    public ConvertFileRequest(String fileID, String format) {
        super("convertfile", "/convertfile", "PUT");
        this.fileid = fileID;
        this.toformat = format;
    }
}
