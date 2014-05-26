package database.containers;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import database.constants.ServerDependentValues;

public class FileTuple {

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

    public String getDownloadURL() {
        return ServerDependentValues.DownloadURL + path;
    }

    public String getUploadURL() {
        return ServerDependentValues.UploadURL + path;
    }

    public String getInputFileUploadURL() {
        return ServerDependentValues.UploadURL + inputFilePath;
    }

    public String getInputFileDownloadURL() {
        return ServerDependentValues.DownloadURL + inputFilePath;
    }

    public String getParentFolder() {
        int filenameIndex = path.lastIndexOf(File.separator);
        return path.substring(0, filenameIndex + 1);
    }

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








