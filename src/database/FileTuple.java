package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class FileTuple {

    public static final int RAW = 1;
    public static final int PROFILE = 2;
    public static final int REGION = 3;
    public static final int OTHER = 4;

    public final Integer id;
    public final String path;
    public final String inputFilePath;
    public final int type;
    public final String filename;
    public final Date date;
    public final String metaData;
    public final String author;
    public final String uploader;
    public final Boolean isPrivate;
    public final String expId;
    public final String grVersion;

    public FileTuple(ResultSet resSet) throws SQLException {
        id = resSet.getInt("FileID");
        path = resSet.getString("Path");
        inputFilePath = resSet.getString("InputFilePath");

        switch (resSet.getString("FileType")) {
        case "Raw":
            type = RAW;
            break;
        case "Profile":
            type = PROFILE;
            break;
        case "Region":
            type = REGION;
            break;
        default:
            type = OTHER;
            break;
        }

        filename = resSet.getString("FileName");
        date = resSet.getDate("Date");
        metaData = resSet.getString("MetaData");
        author = resSet.getString("Author");
        uploader = resSet.getString("Uploader");
        isPrivate = resSet.getBoolean("IsPrivate");
        expId = resSet.getString("ExpID");
        grVersion = resSet.getString("GRVersion");
    }

    public String getDownloadURL() {
        return ServerDependentValues.DownLoadURL+path;
    }

    public String getUploadURL() {
        return ServerDependentValues.UploadURL+path;
    }
}








