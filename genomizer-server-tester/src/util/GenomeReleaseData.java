package util;

/**
 * Contains the information for the genome release.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class GenomeReleaseData {

    private String genomeVersion;
    private String species;
    private String folderPath;
    private String[] files;

    public GenomeReleaseData(String genomeVersion, String specie, String path,
            String fileName) {

        this.genomeVersion = genomeVersion;
        this.species = specie;
        this.folderPath = path;
    }

    public String getVersion() {
        return genomeVersion;
    }

    public String getSpecies() {
        return species;
    }

    public String[] getFilenames() {
        // TODO: The files are never set!? (OO)
        return files;
    }

    public String getPath() {
        return folderPath;
    }

}
