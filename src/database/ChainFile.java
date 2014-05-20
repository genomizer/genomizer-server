package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ChainFile {

    public final String fromVersion;
    public final String toVersion;
    public final String folderPath;
    private Map<String, String> files;

    public ChainFile(ResultSet rs) throws SQLException {
        fromVersion = rs.getString("FromVersion");
        toVersion = rs.getString("ToVersion");
        folderPath = rs.getString("FolderPath");
        files = new HashMap<String, String>();
        do {
            files.put(rs.getString("FileName"), rs.getString("Status"));
        } while (rs.next());
    }
    
    public Map<String, String> getFilesWithStatus() {
    	return files;
    }

}
