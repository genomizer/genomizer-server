package requests;

/**
 * Request for adding files to an experiment from the geo
 * database.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class AddFileFromGeoRequest extends Request {
    
    private String experimentID;
    private String fileName;
    private String type;
    private String author;
    private String uploader;
    private boolean isPrivate;
    private String grVersion;
    private String url;

    /**
     * Command for adding files from the geo database.
     * @param experimentID
     * @param filename
     * @param type
     * @param author
     * @param uploader
     * @param isPrivate
     * @param grVersion
     * @param url
     */
    public AddFileFromGeoRequest(String experimentID, String filename, String type,
            String author, String uploader, boolean isPrivate, String grVersion, String url) {
        super("addgeofile", "/geo", "POST");
        this.experimentID = experimentID;
        this.fileName = filename;
        this.type = type;
        this.author = author;
        this.uploader = uploader;
        this.isPrivate = isPrivate;
        this.grVersion = grVersion;
        this.url = url;
        
    }
    
}
