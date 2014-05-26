package database.subClasses;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.FilePathGenerator;
import database.constants.ServerDependentValues;
import database.containers.ChainFile;
import database.containers.Genome;

/**
 * Class that contains all the methods for adding,changing, getting and removing
 * genome releases and chain files. This class is a subClass of
 * databaseAcessor.java.
 *
 * date: 2014-05-14 version: 1.0
 */
public class GenomeMethods {

    private Connection conn;
    private FilePathGenerator fpg;

    /**
     * constructor for the genomeMethods class.
     *
     * @param connection
     *            Connection, the database jdbc connection.
     * @param filePG
     *            FilePathGenerator, object reference to that class.
     */
    public GenomeMethods(Connection connection, FilePathGenerator filePG) {
        conn = connection;
        fpg = filePG;
    }

    /**
     * Gets the file path to a stored Genome Release
     *
     * @param genomeVersion
     *            - The version to get filepath to, should use
     *            getAllGenomeReleases() and let user choose a version
     * @return String path - a file path, NULL if it was not found.
     * @throws SQLException
     */

    public Genome getGenomeRelease(String genomeVersion) throws SQLException {

        String query = "SELECT * FROM Genome_Release " +
        		"JOIN Genome_Release_Files " +
        		"ON (Genome_Release.Version = Genome_Release_Files.Version) " +
        		"WHERE (Genome_Release.Version ~~* ?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, genomeVersion);
        ResultSet rs = stmt.executeQuery();

        Genome genome = null;
        if (rs.next()) {
            genome = new Genome(rs);
        }

        stmt.close();

        return genome;
    }

    /**
     * Add one genomerelease to the database.
     *
     * @param String
     *            genomeVersion.
     * @param String
     *            species.
     * @return String The upload URL.
     * @throws SQLException
     *             if adding query failed.
     */
    public String addGenomeRelease(String genomeVersion, String species,
            String filename) throws SQLException {

        String folderPath = fpg.generateGenomeReleaseFolder(genomeVersion,
                species);

        StringBuilder filePathBuilder = new StringBuilder(folderPath);
        filePathBuilder.append(filename);

        PreparedStatement stmt;

        if (getGenomeRelease(genomeVersion) == null) {
            String query = "INSERT INTO Genome_Release "
                    + "(Version, Species, FolderPath) " + "VALUES (?, ?, ?)";

            stmt = conn.prepareStatement(query);
            stmt.setString(1, genomeVersion);
            stmt.setString(2, species);
            stmt.setString(3, folderPath);

            stmt.executeUpdate();
            stmt.close();
        }

        String query2 = "INSERT INTO Genome_Release_Files " +
                "(Version, FileName) VALUES (?, ?)";

        stmt = conn.prepareStatement(query2);
        stmt.setString(1, genomeVersion);
        stmt.setString(2, filename);
        stmt.executeUpdate();
        stmt.close();

        filePathBuilder.insert(0, ServerDependentValues.UploadURL);
        stmt.close();
        return filePathBuilder.toString();
    }

    /**
     * Sets the status for a genome release file to "Done".
     * @param version the file version.
     * @param fileName the file name.
     * @return the number of tuples updated.
     * @throws SQLException
     */
    public int fileReadyForDownload(String version, String fileName) throws SQLException {

        String statusUpdateString = "UPDATE Genome_Release_Files SET Status = 'Done' " +
                "WHERE Version = ? AND FileName = ?";

        PreparedStatement statusUpdate = conn.prepareStatement(statusUpdateString);
        statusUpdate.setString(1, version);
        statusUpdate.setString(2, fileName);

        int resCount = statusUpdate.executeUpdate();
        statusUpdate.close();

        return resCount;
    }

    /**
     * Removes one specific genome version stored in the database.
     *
     * @param version
     *            the genome version.
     * @param specie
     *            .
     * @return boolean true if succeded, false if failed.
     * @throws SQLException
     * @throws IOException
     */
    public boolean removeGenomeRelease(String genomeVersion)
            throws SQLException, IOException {

        if (isGenomeVersionUsed(genomeVersion)) {
            throw new IOException(genomeVersion + " is used by at least one" +
            						" file and can therefore not be removed");
        }

        Genome g = getGenomeRelease(genomeVersion);

        if(g == null){
        	return false;
        }
        File genomeReleaseFolder = new File(fpg.getGenomeReleaseFolderPath(
                g.genomeVersion, g.species));

        if (genomeReleaseFolder.exists()) {
            recursiveDelete(genomeReleaseFolder);
        }

        String query = "DELETE FROM Genome_Release " +
                "WHERE Version ~~* ?";

        PreparedStatement stmt;

        stmt = conn.prepareStatement(query);
        stmt.setString(1, genomeVersion);
        int res = stmt.executeUpdate();
        stmt.close();
        return res > 0;
    }

    private boolean isGenomeVersionUsed(String genomeVersion)
    									throws SQLException {
        String query = "SELECT * FROM File WHERE GRVersion = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, genomeVersion);
        ResultSet rs = ps.executeQuery();
        return (rs.next());
    }

    /**
     * Method for getting all the genome releases for a species currently stored
     * in the database.
     *
     * @param species
     *            String, the name of the species you want to get genome
     *            realeases for.
     * @return genomelist ArrayList<Genome>, list of all the genome releases for
     *         a specific species. Returns NULL if the specified specie did NOT
     *         have a genomeRelase entry in the database.
     * @throws SQLException
     */
    public ArrayList<Genome> getAllGenomReleasesForSpecies(String species)
            throws SQLException {

        String query = "SELECT * FROM Genome_Release " +
                "JOIN Genome_Release_Files " +
                "ON (Genome_Release.Version = Genome_Release_Files.Version) " +
                "WHERE (Genome_Release.Species ~~* ?) ORDER BY Genome_Release.Version";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, species);
        ResultSet rs = stmt.executeQuery();

        ArrayList<Genome> genomeList = new ArrayList<Genome>();

        boolean foundAnything = rs.next();

        if(!foundAnything){
        	return null;
        }

        while (!rs.isAfterLast()) {
            Genome g = new Genome(rs);
            genomeList.add(g);
        }

        stmt.close();
        return genomeList;
    }

    /**
     * get a specific chainfile depending on from and to what genome release you
     * want to convert between.
     *
     * @param fromVersion
     *            String, the name of the old genome release version
     * @param toVersion
     *            String, the name of the new genome release version
     * @return resFilePath String, the filePath of that chain file.
     * @throws SQLException
     */
    public ChainFile getChainFile(String fromVersion, String toVersion)
            throws SQLException {

        String query = "SELECT * FROM Chain_File NATURAL JOIN Chain_File_Files" +
        		" WHERE (FromVersion ~~* ?)" +
                " AND (ToVersion ~~* ?)";
        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setString(1, fromVersion);
        stmt.setString(2, toVersion);
        ResultSet rs = stmt.executeQuery();

        ChainFile cf = null;

        if (rs.next()) {
            cf = new ChainFile(rs);
        }

        stmt.close();

        return cf;
    }

    /**
     * Adds a chain file to database for conversions. Parameters: Oldversion,
     * new version and filename. Returns: upload URL
     *
     * @param String
     *            fromVersion
     * @param String
     *            toVersion
     * @param String
     *            fileName
     * @return String upload URL
     * @throws SQLException
     */
    public String addChainFile(String fromVersion, String toVersion,
            String fileName) throws SQLException {

        String species = "";
        String speciesQuery = "SELECT Species From Genome_Release"
                + " WHERE (version ~~* ?)";

        PreparedStatement speciesStat = conn.prepareStatement(speciesQuery);
        speciesStat.setString(1, fromVersion);

        ResultSet rs = speciesStat.executeQuery();

        if (rs.next()) {
            species = rs.getString("Species");
        }
        speciesStat.close();

        String filePath = fpg.generateChainFolder(species, fromVersion,
                toVersion);

        String insertQuery = "INSERT INTO Chain_File "
                + "(FromVersion, ToVersion, FolderPath) VALUES (?, ?, ?)";

        String insertQuery2 = "INSERT INTO Chain_File_Files " +
        		"(FromVersion, ToVersion, FileName) " +
        		"VALUES (?, ?, ?)";

        ChainFile cf = getChainFile(fromVersion, toVersion);
        if (cf == null) {
	        PreparedStatement insertStat = conn.prepareStatement(insertQuery);
	        insertStat.setString(1, fromVersion);
	        insertStat.setString(2, toVersion);
	        insertStat.setString(3, filePath);
	        insertStat.executeUpdate();
	        insertStat.close();
        }

        PreparedStatement stmt = conn.prepareStatement(insertQuery2);
        stmt.setString(1, fromVersion);
        stmt.setString(2, toVersion);
        stmt.setString(3, fileName);
        stmt.executeUpdate();
        stmt.close();

        String URL = ServerDependentValues.UploadURL;

        return URL + filePath;
    }

    /**
     * Deletes a chain_file from the database and the physical file on the
     * system. You find the unique file by sending in the genome version the
     * file converts from and the genome version the file converts to.
     *
     * @param fromVersion
     *            - genome version the Chain_file converts from
     * @param toVersion
     *            - genome version the Chin_file converts to
     * @return the number of deleted tuples in the database. (Should be one if
     *         success)
     * @throws SQLException
     *             - if the query does not succeed
     */
    public int removeChainFile(String fromVersion, String toVersion)
            throws SQLException {

        int resCount = 0;

        ChainFile cf = getChainFile(fromVersion, toVersion);

        if (cf == null) {
            return 0;
        }
        String filePath = cf.folderPath;


		String query = "DELETE FROM Chain_File WHERE (FromVersion ~~* ?)"
			+ " AND (ToVersion ~~* ?)";

        File chainFile = new File(filePath);
        File chainFolder = chainFile.getParentFile();

        if (chainFolder.exists()) {
            recursiveDelete(chainFolder);
        }

        PreparedStatement deleteStatement = conn.prepareStatement(query);
        deleteStatement.setString(1, fromVersion);
        deleteStatement.setString(2, toVersion);
        resCount = deleteStatement.executeUpdate();
        deleteStatement.close();

        return resCount;
    }

    private void recursiveDelete(File folder) {
        File[] contents = folder.listFiles();
        if (contents == null || contents.length == 0) {
            folder.delete();
        } else {
            for (File f : contents) {
                recursiveDelete(f);
            }
        }
        folder.delete();
    }

    public List<String> getAllGenomReleaseSpecies() throws SQLException {
        String query = "SELECT DISTINCT Species FROM Genome_Release";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        List<String> species = new ArrayList<String>();
        while (rs.next()) {
            species.add(rs.getString("Species"));
        }
        return species;
    }

    public List<Genome> getAllGenomReleases() throws SQLException {
        String query = "SELECT * FROM Genome_Release " +
                "NATURAL JOIN Genome_Release_Files ";

        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        ArrayList<Genome> genomeList = new ArrayList<Genome>();

        rs.next();
        while (!rs.isAfterLast()) {
            Genome g = new Genome(rs);
            genomeList.add(g);
        }

        stmt.close();
        return genomeList;
    }
}
