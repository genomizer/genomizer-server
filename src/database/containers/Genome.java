package database.containers;

import database.constants.ServerDependentValues;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Genome {

    public final String genomeVersion;
    public final String species;
    public final String folderPath;
    private final List<String> files;


    //Depricated!
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
     *  Construct a Genome.
     *
     * @param genomeVersion - the version
     * @param species - the specie
     * @param folderPath - the path where the genomeRelease files should be.
     * @param files - The Files.
     */
    public Genome(String genomeVersion, String species, String folderPath, List<String> files) {
        this.genomeVersion = genomeVersion;
        this.species = species;
        this.folderPath = folderPath;
        this.files = files;
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

    public String getFolderPath() {
        return folderPath;
    }

    @Override
    public String toString() {
        return "Genome [genomeVersion=" + genomeVersion + ", species="
                + species + ", folderPath=" + folderPath + ", files=" + files
                + "]";
    }
}
