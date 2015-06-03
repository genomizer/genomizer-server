package requests;

/**
 * Request for adding a genome release.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class AddGenomeReleaseRequest extends Request {

    private String genomeVersion;
    private String specie;
    private String[] files;

    /**
     * Creates the command for adding genome releases.
     * @param files The files to add to the genome release.
     * @param species The species for the genome release.
     * @param genomeVersion The genome version for the genome release.
     */
    public AddGenomeReleaseRequest(String[] files, String species,
            String genomeVersion) {
        super("AddGenomeRelease", "/genomeRelease", "POST");
        this.files = files;
        this.specie = species;
        this.genomeVersion = genomeVersion;
    }

}
