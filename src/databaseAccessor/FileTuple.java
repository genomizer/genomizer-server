package databaseAccessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class FileTuple {

    public final Integer id;
    public final String path;
    public final String type;
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
        type = resSet.getString("FileType");
        filename = resSet.getString("FileName");
        date = resSet.getDate("Date");
        metaData = resSet.getString("MetaData");
        author = resSet.getString("Author");
        uploader = resSet.getString("Uploader");
        isPrivate = resSet.getBoolean("IsPrivate");
        expId = resSet.getString("ExpID");
        grVersion = resSet.getString("GRVersion");
    }

}
