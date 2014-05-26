package database.containers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import database.constants.ServerDependentValues;

public class Genome {

    public final String genomeVersion;
    public final String species;
    public final String folderPath;
    private final Map<String, String> files;

    public Genome(ResultSet resSet) throws SQLException {
        genomeVersion = resSet.getString("Version");
        species = resSet.getString("Species");
        folderPath = resSet.getString("FolderPath");
        files = new HashMap<String, String>();
        do {
            files.put(resSet.getString("FileName"), resSet.getString("Status"));
        } while (resSet.next() && resSet.getString("Version").equals(genomeVersion));
    }

    /**
     * Gets a map with filename as key and the status of the file as value.
     * @return
     */
    public Map<String, String> getFilesWithStatus() {
        return files;
    }

    public List<String> getDownloadURLs() {
        List<String> downloadURLs = new ArrayList<String>();
        for (Entry<String, String> e: files.entrySet()) {
            downloadURLs.add(ServerDependentValues.DownloadURL + folderPath + e.getKey());
        }
        return downloadURLs;
    }

    public String getFilePrefix() {
        if (files.isEmpty()) {
            return null;
        }
        String fileName = files.entrySet().iterator().next().getKey();
        int indexOfFirstDot = fileName.indexOf('.');
        if (indexOfFirstDot == -1) {
            return null;
        }
        return fileName.substring(0, indexOfFirstDot);
    }
}
