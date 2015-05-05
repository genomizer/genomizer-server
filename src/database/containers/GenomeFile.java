package database.containers;


import database.DatabaseAccessor;

import java.sql.ResultSet;
import java.sql.SQLException;

// A single genome release can consist of multiple files.
// This class represents a single such file.
public class GenomeFile implements HasCheckSumMD5 {
    public final String genomeVersion;
    public final String fileName;
    public final String checkSumMD5;
    public final String status;

    public GenomeFile(ResultSet rs) throws SQLException {
        genomeVersion = rs.getString("Version");
        fileName      = rs.getString("FileName");
        checkSumMD5   = rs.getString("MD5");
        status        = rs.getString("Status");
    }

    @Override
    public String getCheckSumMD5() {
        return checkSumMD5;
    }

    @Override
    public void setCheckSumMD5(DatabaseAccessor db, String checkSumMD5) throws SQLException {
        db.setGenomeReleaseFileCheckSumMD5(this, checkSumMD5);
    }
}
