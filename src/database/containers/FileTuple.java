package database.containers;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import database.DatabaseAccessor;
import server.ServerSettings;

/**
 * Container class for holding data on a file in the database. Public variables
 * are used to get the different values of the file.
 */
public class FileTuple implements HasCheckSumMD5 {

    public static final int RAW = 1;
    public static final int PROFILE = 2;
    public static final int REGION = 3;
    public static final int OTHER = 4;

    public final Integer id;
    public final String path;
    public final String inputFilePath;
    public final String type;
    public final String filename;
    public final Date date;
    public final String metaData;
    public final String author;
    public final String uploader;
    public final Boolean isPrivate;
    public final String expId;
    public final String grVersion;
    public final String status;
    public final String checkSumMD5;
    public final String fileSize;

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
        checkSumMD5 = resSet.getString("MD5");
        fileSize = resSet.getString("FileSize");
    }

    /**
     * Gets the upload URL for the file on the file system
     *
     * @return String file URL
     */
    public String getUploadURL() {

    	return ServerSettings.generateUploadURL(path);
    }

    /**
     * Gets the download URL for the file on the file system.
     *
     * @return String file URL
     */
    public String getDownloadURL() {

    	return ServerSettings.generateDownloadURL(path);
    }

    /**
     * Gets the input file upload URL for the file on the file system
     *
     * @return String file URL
     */
    public String getInputFileUploadURL() {

    	return ServerSettings.generateUploadURL(inputFilePath);
    }

    /**
     * Gets the input file download URL for the file on the file system
     *
     * @return String file URL
     */
    public String getInputFileDownloadURL() {

    	return ServerSettings.generateDownloadURL(inputFilePath);
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

    public String getCheckSumMD5() {
        return checkSumMD5;
    }

    public void setCheckSumMD5(DatabaseAccessor db, String checkSumMD5) throws SQLException {
        db.setFileCheckSumMD5(this, checkSumMD5);
    }

    /**
     * Gets the file size,
     *
     * @return
     */
    public String getFileSize() {
        return fileSize;
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
                + ", status=" + status + ", md5=" + checkSumMD5 + "]";
    }
}