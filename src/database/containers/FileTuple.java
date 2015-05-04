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


    public static final int RAW = 0;
    public static final int PROFILE = 1;
    public static final int REGION = 2;
    public static final int OTHER = 3;


    public enum Type {
        Raw(0), Profile(1), Region(2), Other(3);

        public final int val;

        Type(int i){
            this.val = i;
        }

        public static Type fromInt(int i){
            for(Type t : Type.values()){
                if(t.val == i){
                    return t;
                }
            }
            throw new IllegalArgumentException("Unrecongized filetype!");
        }
        public static Type fromString(String s){
            for(Type t: Type.values()){
                if(s.equalsIgnoreCase(t.name())){
                    return t;
                }
            }
            throw new IllegalArgumentException("Unrecognized file type!");
        }
    }

    private Integer fileID;
    private Type type;

    private String status;

    //TODO Fix this
    public final String checkSumMD5;


    public static FileTupleBuilder makeNew(){
        return new FileTupleBuilder();
    }


    /**
     * Getter for the FileID associated with this tuple.
     * @return the fileid as an int
     */
    public Integer getFileId() {
        return fileID;
    }

    /**
     * Getter for the filetype associated with this file; see {@link database.containers.FileTuple.Type}.
     * @return a FileTuple.Type
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the filename of the file represented by this tuple.
     *
     * @see #getFullPath()
     * @see #getFolderPath()
     * @return
     */
    public String getFileName() {
        int filenameIndex = path.lastIndexOf(File.separator);
        return path.substring(filenameIndex + 1);
    }

    /**
     * Returns the contents of the "status" field of the file table associated with this tuple.
     * @return a string
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the full path of the file associated with this tuple.
     *
     * @see #getFileName()
     * @see #getFolderPath()
     * @return
     */
    public String getFullPath() {
        return path;
    }


    void setFileID(Integer fileID) {
        this.fileID = fileID;
    }

    void setType(Type type) {
        this.type = type;

    }



    void setStatus(String status) {
        this.status = status;
    }

    /**
     * Constructs a FileTuple object. Parameter: ResultSet
     *
     * @param resSet a sql result set
     * @throws SQLException
     */
    public FileTuple(ResultSet resSet) throws SQLException {

        fileID = resSet.getInt("FileID");
        path = resSet.getString("Path");
        inputFilePath = resSet.getString("InputFilePath");
        type = Type.fromString(resSet.getString("FileType"));
        date = resSet.getDate("Date");
        metaData = resSet.getString("MetaData");
        author = resSet.getString("Author");
        uploader = resSet.getString("Uploader");
        isPrivate = resSet.getBoolean("IsPrivate");
        expId = resSet.getString("ExpID");
        grVersion = resSet.getString("GRVersion");
        status = resSet.getString("Status");
        checkSumMD5 = resSet.getString("MD5");




    }

    FileTuple(){
        checkSumMD5 = null;
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
     * Gets path to the folder that the associated file is located in.
     *
     * @see #getFileName()
     * @see #getFullPath()
     * @return String folder URL
     */
    public String getFolderPath() {

    	int filenameIndex = path.lastIndexOf(File.separator);
        return path.substring(0, filenameIndex + 1);
    }

    /**
     * Outputs the FileTuple as a String
     */
    @Override
    public String toString() {

        return "FileTuple [id=" + fileID + ", path=" + path + ", inputFilePath="
                + inputFilePath + ", type=" + type.name() + ", filename=" + this.getFileName()
                + ", date=" + date + ", metaData=" + metaData + ", author="
                + author + ", uploader=" + uploader + ", isPrivate="
                + isPrivate + ", expId=" + expId + ", grVersion=" + grVersion
                + ", status=" + status + ", md5=" + checkSumMD5 + "]";
    }
}
