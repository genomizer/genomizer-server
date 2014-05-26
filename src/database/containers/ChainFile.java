package database.containers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import database.DatabaseAccessor;

/**
 * This is a chain file with all properties as
 * seen in the database. Use the public final
 * fields to get the values for fromVersion, toVersion,
 * and folderPath. Use the
 * {@link #getFilesWithStatus()
 * getFilesWithStatus} method to get all files and their status.
 */
public class ChainFile {

    public final String fromVersion;
    public final String toVersion;
    public final String folderPath;
    private Map<String, String> files;

    /**
     * Creates a chain file object from a ResultSet. Don't call
     * this constructor directly, use
     * {@link database.DatabaseAccessor#
     * getChainFile(String fromVersion, String toVersion)
     * getChainFile} to create a chain file object.
     * @param rs the ResultSet.
     * @throws SQLException
     */
    public ChainFile(ResultSet rs) throws SQLException {
        fromVersion = rs.getString("FromVersion");
        toVersion = rs.getString("ToVersion");
        folderPath = rs.getString("FolderPath");
        files = new HashMap<String, String>();
        do {
            files.put(rs.getString("FileName"), rs.getString("Status"));
        } while (rs.next());
    }

    /**
     * Gets all files belonging to this chain file together with their status.
     * @return a Map with file name as key and file status as value.
     */
    public Map<String, String> getFilesWithStatus() {
    	return files;
    }

}
