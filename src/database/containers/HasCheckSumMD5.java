package database.containers;

import database.DatabaseAccessor;

import java.sql.SQLException;

public interface HasCheckSumMD5 {
    public String getCheckSumMD5();
    public void setCheckSumMD5(DatabaseAccessor db, String checkSumMD5) throws SQLException;
}
