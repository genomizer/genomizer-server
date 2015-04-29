package database.containers;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.constants.ServerDependentValues;

/**
 * Container class for holding data on a file in the database. Public variables
 * are used to get the different values of the file.
 */
public class FileTuple extends AbstractFileTuple {

    public static final int RAW = 1;
    public static final int PROFILE = 2;
    public static final int REGION = 3;
    public static final int OTHER = 4;

    private Integer id;
    private String type;
    private String filename;
    private String status;


    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getFilename() {
        return filename;
    }

    public String getStatus() {
        return status;
    }

    void setId(Integer id) {
        this.id = id;
    }

    void setType(String type) {
        this.type = type;
    }

    void setFilename(String filename) {
        this.filename = filename;
    }

    void setStatus(String status) {
        this.status = status;
    }

    /**
     * Constructs a FileTuple object. Parameter: ResultSet
     *
     * @param resSet
     * @throws SQLException
     */
    public FileTuple(ResultSet resSet) throws SQLException {

    	id = resSet.getInt("FileID");
        path = resSet.getString("Path");
        inputFilePath = resSet.getString("InputFilePath");
        type = resSet.getString("FileType");
        filename = resSet.getString("FileName");
        date = resSet.getDate("Date");
        metaData = resSet.getString("MetaData");
        author = resSet.getString("Author");
        uploader = resSet.getString("Uploader");
        isPrivate = resSet.getBoolean("IsPrivate");
        expId = resSet.getString("ExpID");
        grVersion = resSet.getString("GRVersion");
        status = resSet.getString("Status");
    }

    FileTuple(){

    }

    /**
     * Gets the upload URL for the file on the file system
     *
     * @return String file URL
     */
    public String getUploadURL() {

    	return ServerDependentValues.UploadURL + path;
    }

    /**
     * Gets the download URL for the file on the file system.
     *
     * @return String file URL
     */
    public String getDownloadURL() {

    	return ServerDependentValues.DownloadURL + path;
    }

    /**
     * Gets the input file upload URL for the file on the file system
     *
     * @return String file URL
     */
    public String getInputFileUploadURL() {

    	return ServerDependentValues.UploadURL + inputFilePath;
    }

    /**
     * Gets the input file download URL for the file on the file system
     *
     * @return String file URL
     */
    public String getInputFileDownloadURL() {

    	return ServerDependentValues.DownloadURL + inputFilePath;
    }

    /**
     * Gets the parent folder of the file on the file system.
     *
     * @return String folder URL
     */
    public String getParentFolder() {

    	int filenameIndex = path.lastIndexOf(File.separator);
        return path.substring(0, filenameIndex + 1);
    }

    /**
     * Outputs the FileTuple as a String
     */
    @Override
    public String toString() {

    	return "FileTuple [id=" + id + ", path=" + path + ", inputFilePath="
                + inputFilePath + ", type=" + type + ", filename=" + filename
                + ", date=" + date + ", metaData=" + metaData + ", author="
                + author + ", uploader=" + uploader + ", isPrivate="
                + isPrivate + ", expId=" + expId + ", grVersion=" + grVersion
                + ", status=" + status + "]";
    }
}