package database.subClasses;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.FilePathGenerator;
import database.ServerDependentValues;

public class GenomeMethods {

	private Connection conn;
	private FilePathGenerator fpg;

	public GenomeMethods(Connection connection, FilePathGenerator filePG){

			conn = connection;
			fpg = filePG;
	}

	/**
     * Gets the file path to a stored Genome Release
     * @param genomeVersion - The version to get filepath to, should use getAllGenomeReleases()
     * and let user choose a version
     * @return - a file path
     * @throws SQLException
     */

    public String getGenomeRelease(String genomeVersion) throws SQLException{

        String query = "SELECT FilePath FROM Genome_Release WHERE (Version = ?)";

        PreparedStatement ps = conn.prepareStatement(query);

        ps.setString(1, genomeVersion);

        ResultSet rs = ps.executeQuery();

        String path = null;

        if (rs.next()) {
            path = rs.getString("FilePath");
        }

        ps.close();

    	return path;
    }


    /**
     * Add one genomerelease to the database.
     *
     * @param String
     *            genomeVersion.
     * @param String
     *            species.
     * @return String The path to the folder where the genome release
     *         files should be saved.
     * @throws SQLException
     *             if adding query failed.
     */
    public String addGenomeRelease(String genomeVersion,
            String species, String filename) throws SQLException {

        String folderPath = fpg.generateGenomeReleaseFolder(genomeVersion,
                species);

        StringBuilder filePathBuilder = new StringBuilder(folderPath);
        filePathBuilder.append(filename);

        String filePath = filePathBuilder.toString();

        String insertGenRelQuery = "INSERT INTO Genome_Release "
                + "(Version, Species, FilePath) " + "VALUES (?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(insertGenRelQuery);
        ps.setString(1, genomeVersion);
        ps.setString(2, species);
        ps.setString(3, filePath.toString());

        ps.execute();

        filePathBuilder.insert(0, ServerDependentValues.UploadURL);

        return filePathBuilder.toString();
    }

    /**
     * Removes one specific genome version stored in the database.
     *
     * @param version
     *            , the genome version.
     * @param specie
     *            .
     * @return boolean, true if succeded, false if failed.
     */
    public boolean removeGenomeRelease(String genomeVersion,
            String specie) {

        String removeQuery = "DELETE FROM Genome_Release WHERE "
                + "(Version = ? AND Species = ?)";

        PreparedStatement ps;

        try {
            ps = conn.prepareStatement(removeQuery);

            ps.setString(1, genomeVersion);
            ps.setString(2, specie);
            ps.execute();
        } catch (SQLException e) {
            System.out.println("Failed to remove genome release!");
            return false;
        }
        return true;
    }

    /**
    *
    * @param species
    * @return
    * @throws SQLException
    */
   public List<String> getAllGenomReleases(String species) throws SQLException {

        List<String> genomeVersions = new ArrayList<String>();

        String query = "SELECT Version FROM Genome_Release WHERE Species = ?";

        PreparedStatement ps = conn.prepareStatement(query);

        ps.setString(1, species);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            genomeVersions.add(rs.getString("Version"));
        }

        ps.close();
        return genomeVersions;
    }

    public String getChainFile(String fromVersion, String toVersion)
            throws SQLException {

        String query = "SELECT FilePath FROM Chain_File WHERE (FromVersion = ?)"
                + " AND (ToVersion = ?)";
        PreparedStatement ps = conn.prepareStatement(query);

        ps.setString(1, fromVersion);
        ps.setString(2, toVersion);

        ResultSet rs = ps.executeQuery();
        String res = null;

        if (rs.next()) {

            res = rs.getString("FilePath");
        }

        ps.close();

        return res;
    }

    /**
     * Adds a chain file to database for conversions. Parameters:
     * Oldversion, new version and filename. Returns: upload URL
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
                + " WHERE (version = ?)";

        PreparedStatement speciesStat = conn.prepareStatement(speciesQuery);
        speciesStat.setString(1, fromVersion);

        ResultSet rs = speciesStat.executeQuery();

        while (rs.next()) {
            species = rs.getString("Species");
        }

        String filePath = fpg.generateChainFolderPath(species, fromVersion,
                toVersion) + fileName;

        String insertQuery = "INSERT INTO Chain_File "
                + "(FromVersion, ToVersion, FilePath) VALUES (?, ?, ?)";

        PreparedStatement insertStat = conn.prepareStatement(insertQuery);
        insertStat.setString(1, fromVersion);
        insertStat.setString(2, toVersion);
        insertStat.setString(3, filePath);
        insertStat.executeUpdate();
        insertStat.close();

        String URL = ServerDependentValues.UploadURL;

        return URL + filePath;
    }

    /**
     * Deletes a chain_file from the database. You find the unique
     * file by sending in the genome version the file converts from
     * and the genome version the file converts to.
     *
     * @param fromVersion
     *            - genome version the Chain_file converts from
     * @param toVersion
     *            - genome version the Chin_file converts to
     * @return the number of deleted tuples in the database. (Should
     *         be one if success)
     * @throws SQLException
     *             - if the query does not succeed
     */
    public int removeChainFile(String fromVersion, String toVersion)
            throws SQLException {

        String query = "DELETE FROM Chain_File WHERE (FromVersion = ?)"
                + " AND (ToVersion = ?)";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, fromVersion);
        ps.setString(2, toVersion);

        int res = ps.executeUpdate();
        ps.close();

        return res;
    }
}
