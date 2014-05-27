package database.containers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.constants.ServerDependentValues;

public class Genome {

    public final String genomeVersion;
    public final String species;
    public final String folderPath;
    private final List<String> files;

    public Genome(ResultSet resSet) throws SQLException {
        genomeVersion = resSet.getString("Version");
        species = resSet.getString("Species");
        folderPath = resSet.getString("FolderPath");
        files = new ArrayList<String>();
        do {
            files.add(resSet.getString("FileName"));
        } while (resSet.next() && resSet.getString("Version").equals(genomeVersion));
    }

    /**
     * Gets a map with filename as key and the status of the file as value.
     * @return
     */
    public List<String> getFiles() {
        return files;
    }

    public List<String> getDownloadURLs() {
        List<String> downloadURLs = new ArrayList<String>();
        for (String s: files) {
            downloadURLs.add(ServerDependentValues.DownloadURL + folderPath + s);
        }
        return downloadURLs;
    }

    public String getFilePrefix() {
        if (files.isEmpty()) {
            return null;
        }
        String fileName = files.get(0);
        int indexOfFirstDot = fileName.indexOf('.');
        if (indexOfFirstDot == -1) {
            return null;
        }
        return fileName.substring(0, indexOfFirstDot);
    }

    @Override
    public String toString() {
        return "Genome [genomeVersion=" + genomeVersion + ", species="
                + species + ", folderPath=" + folderPath + ", files=" + files
                + "]";
    }


}
