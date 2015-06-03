package requests;

/**
 * This class represents a "Add a file to an experiment" request in an
 * application for genome researchers. This request adds a file to an experiment
 * in the database of the application.
 *
 * @author worfox
 * 25 April 2014
 */

public class AddFileToExperiment extends Request {

    public String experimentID;
    public String fileName;
    public String type;
    public String metaData;
    public String author;
    public String uploader;
    public boolean isPrivate;
    public String grVersion;

    /**
     * Constructor creating the experiment.
     *
     * @param experimentId String representing the experiment id.
     * @param fileName String representing the file name.
     * @param fileType String representing the type of the file.
     * @param metaData String representing meta data.
     * @param author String representing the author of the file.
     * @param uploader String representing who uploaded the file.
     * @param isPrivate boolean representing if the file should be private.
     * @param grVersion String representing the genome version of the file.
     */
    public AddFileToExperiment(String experimentId, String fileName,
            String fileType, String metaData, String author, String uploader,
            boolean isPrivate, String grVersion) {

        super("addfile", "/file", "POST");
        this.experimentID = experimentId;
        this.fileName = fileName;
        this.type = fileType;
        this.metaData = metaData;
        this.author = author;
        this.uploader = uploader;
        this.isPrivate = isPrivate;
        this.grVersion = grVersion;
    }

}
