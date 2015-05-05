package database.containers;

import database.DatabaseAccessor;

import java.sql.ResultSet;
import java.sql.SQLException;

// A chain file collection can contain multiple files.
// This class represents a single such file.
public class ChainFile implements HasCheckSumMD5 {
    public final String fromVersion;
    public final String toVersion;
    public final String fileName;
    public final String checkSumMD5;
    public final String status;

    public ChainFile (ResultSet rs) throws SQLException {
        fromVersion = rs.getString("FromVersion");
        toVersion   = rs.getString("ToVersion");
        fileName    = rs.getString("FileName");
        checkSumMD5 = rs.getString("MD5");
        status      = rs.getString("Status");
    }

    public String getCheckSumMD5() {
        return checkSumMD5;
    }

    public void setCheckSumMD5(DatabaseAccessor db, String checkSumMD5) throws SQLException {
        db.setChainFileCheckSumMD5(this, checkSumMD5);
    }
}
