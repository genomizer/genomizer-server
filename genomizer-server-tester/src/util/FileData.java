package util;

/**
 * Class containing the file information.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class FileData {
    public String id;
    public String expId;
    public String type;
    public String metaData;
    public String author;
    public String filename;
    public String uploader;
    public boolean isPrivate;
    public String grVersion;
    public String date;
    public String path;
    public String url;
    public String fileSize;

    /**
     * Class representing files data
     *
     * @param fileId
     * @param experimentID
     * @param type
     * @param metaData
     * @param author
     * @param uploader
     * @param isPrivate
     * @param grVersion
     * @param date
     * @param path
     * @param url
     * @param fileName
     */
    public FileData(String fileId, String experimentID, String type,
            String metaData, String author, String uploader, boolean isPrivate,
            String grVersion, String date, String path, String url,
            String fileName, String fileSize) {
        this.id = fileId;
        this.expId = experimentID;
        this.type = type;
        this.metaData = metaData;
        this.author = author;
        this.isPrivate = isPrivate;
        this.grVersion = grVersion;
        this.uploader = uploader;
        this.date = date;
        this.path = path;
        this.url = url;
        this.filename = fileName;
        this.fileSize = fileSize;
    }



    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }


    //TODO Saknar override för hashCode CF
    public boolean equals(Object o) {
        return (((FileData) o)).id.equals(id);
    }



    public String getName() {
        // TODO Auto-generated method stub
        return filename;
    }
}
