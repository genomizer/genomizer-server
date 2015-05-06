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
import database.FileValidator;
import database.constants.ServerDependentValues;
import database.containers.ChainFile;
import database.containers.ChainFiles;
import database.containers.Genome;
import database.containers.GenomeFile;

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

        String query = "SELECT * FROM Genome_Release "
                + "JOIN Genome_Release_Files "
                + "ON (Genome_Release.Version = Genome_Release_Files.Version) "
                + "WHERE (Genome_Release.Version ~~* ?)";

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
     * Add one genome release to the database.
     *
     * @param genomeVersion the genome version
     * @param species the species
	 * @param filename a filename
     * @return String The upload URL.
     * @throws SQLException
     *             if adding query failed.
     * @throws IOException
     */
    public String addGenomeRelease(String genomeVersion, String species,
            String filename, String checkSumMD5) throws SQLException, IOException {

    	if(!FileValidator.fileNameCheck(filename)){
    		throw new IOException("Invalid file name");
    	}


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

        if (genomeReleaseFileExists(genomeVersion, filename)) {
            throw new IOException(filename
                    + " already exists for this genome release!");
        }

        String query2 = "INSERT INTO Genome_Release_Files "
                + "(Version, FileName, MD5) VALUES (?, ?, ?)";

		stmt = conn.prepareStatement(query2);
		stmt.setString(1, genomeVersion);
		stmt.setString(2, filename);
		stmt.setString(3, checkSumMD5);
		stmt.executeUpdate();
		stmt.close();

		filePathBuilder.insert(0, ServerDependentValues.UploadURL);
		stmt.close();
		return filePathBuilder.toString();
	}

	/**
	 * Given a filesystem path, retrieve the corresponding genome release file record.
	 *
	 * @param  file          file name.
	 * @throws SQLException  if something went wrong.
	 */
	public GenomeFile getGenomeReleaseFile (String file) throws SQLException {
		File fileFile   = new File(file);
		String fileName = fileFile.getName();
		String fileDir  = fileFile.getParent();
		if (!fileDir.endsWith(File.separator))
			fileDir += File.separator;

		// Given a directory name, get genome release version...
		String genomeReleaseVersion = null;
		try (PreparedStatement stmt = conn.prepareStatement(
				"SELECT * FROM Genome_Release_Files "
				+ "NATURAL JOIN Genome_Release "
				+ "WHERE FolderPath ~~* ? AND FileName ~~* ?")) {
			stmt.setString(1, fileDir);
			stmt.setString(2, fileName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new GenomeFile(rs);
			}
		}

		return  null;
	}

	/**
	 * Update the MD5 checksum corresponding to a given genome release file.
	 *
	 * @param  gf            file record.
	 * @param  checkSumMD5   check sum.
	 * @throws SQLException  if something went wrong.
	 */
	public void setGenomeReleaseFileCheckSumMD5 (GenomeFile gf, String checkSumMD5) throws SQLException {
		try(PreparedStatement stmt = conn.prepareStatement(
				"UPDATE Genome_Release_Files "
				+ "SET MD5 = ? "
				+ "WHERE Version ~~* ? AND FileName ~~* ?")) {
			stmt.setString(1, checkSumMD5);
			stmt.setString(2, gf.genomeVersion);
			stmt.setString(3, gf.fileName);
			stmt.executeUpdate();
		}
	}

	/**
	 * Given a filesystem path, retrieve the corresponding chain file record.
	 *
	 * @param  file          file name.
	 * @throws SQLException  if something went wrong.
	 */
	public ChainFile getChainFile (String file) throws SQLException {
		File fileFile   = new File(file);
		String fileName = fileFile.getName();
		String fileDir  = fileFile.getParent();
		if (!fileDir.endsWith(File.separator))
			fileDir += File.separator;

		String chainFileFromVersion = null;
		String chainFileToVersion = null;
		try (PreparedStatement stmt = conn.prepareStatement(
				"SELECT * FROM Chain_File_Files "
				+ "NATURAL JOIN Chain_File "
				+ "WHERE FolderPath ~~* ? AND FileName ~~* ?")) {
			stmt.setString(1, fileDir);
			stmt.setString(2, fileName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new ChainFile(rs);
			}
		}

		return null;
	}

	/**
	 * Update the MD5 checksum corresponding to a given chain file.
	 *
	 * @param  cf            file record.
	 * @param  checkSumMD5   check sum.
	 * @throws SQLException  if something went wrong.
	 */
	public void setChainFileCheckSumMD5 (ChainFile cf, String checkSumMD5) throws SQLException {
		try(PreparedStatement stmt = conn.prepareStatement(
				"UPDATE Chain_File_Files "
						+ "SET MD5 = ? "
						+ "WHERE FromVersion ~~* ? AND ToVersion ~~* ? AND FileName ~~* ?")) {
			stmt.setString(1, checkSumMD5);
			stmt.setString(2, cf.fromVersion);
			stmt.setString(3, cf.toVersion);
			stmt.setString(4, cf.fileName);
			stmt.executeUpdate();
		}
	}

    private boolean genomeReleaseFileExists(String genomeVersion,
            String filename) throws SQLException {

        Genome g = getGenomeRelease(genomeVersion);
        if (g == null) {
            return false;
        }
        if (caseInsensitiveSearch(filename, g.getFiles()) != null) {
            return true;
        }
        return false;
    }

    private String caseInsensitiveSearch(String target,
										 List<String> list) {
        for (String s: list) {
            if (s.equalsIgnoreCase(target)) {
                return s;
            }
        }
        return null;
    }

	/**
	 * Sets the status for a genome release file to "Done".
	 *
	 * @param version
	 *            the file version.
	 * @param fileName
	 *            the file name.
	 * @return the number of tuples updated.
	 * @throws SQLException
	 */
	public int fileReadyForDownload(String version, String fileName)
			throws SQLException {

        String statusUpdateString = "UPDATE Genome_Release_Files SET Status = 'Done' "
                + "WHERE Version = ? AND FileName = ?";


        PreparedStatement statusUpdate = conn
                .prepareStatement(statusUpdateString);
        statusUpdate.setString(1, version);
        statusUpdate.setString(2, fileName);


		int resCount = statusUpdate.executeUpdate();
		statusUpdate.close();

		return resCount;
	}

	/**
	 * Removes one specific genome version stored in the database.
	 *
	 * @param genomeVersion
	 *            the genome version.
	 * @return boolean true if succeded, false if failed.
	 * @throws SQLException
	 * @throws IOException
	 */
	public boolean removeGenomeRelease(String genomeVersion)
			throws SQLException, IOException {

        if (isGenomeVersionUsed(genomeVersion)) {
            throw new IOException(genomeVersion + " is used by at least one"
                    + " file and can therefore not be removed");
        }

		Genome g = getGenomeRelease(genomeVersion);

        if (g == null) {
            return false;
        }
        File genomeReleaseFolder = new File(fpg.getGenomeReleaseFolderPath(
                g.genomeVersion, g.species));

		if (genomeReleaseFolder.exists()) {
			recursiveDelete(genomeReleaseFolder);
		}

        String query = "DELETE FROM Genome_Release " + "WHERE Version ~~* ?";

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
	 *            releases for.
	 * @return genomelist ArrayList<Genome>, list of all the genome releases for
	 *         a specific species. Returns NULL if the specified species did NOT
	 *         have a genomeRelease entry in the database.
	 * @throws SQLException
	 */
	public ArrayList<Genome> getAllGenomeReleasesForSpecies(String species)
			throws SQLException {

        String query = "SELECT * FROM Genome_Release "
                + "JOIN Genome_Release_Files "
                + "ON (Genome_Release.Version = Genome_Release_Files.Version) "
                + "WHERE (Genome_Release.Species ~~* ?) ORDER BY Genome_Release.Version";

		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, species);
		ResultSet rs = stmt.executeQuery();

		ArrayList<Genome> genomeList = new ArrayList<Genome>();

		boolean foundAnything = rs.next();

        if (!foundAnything) {
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
	public ChainFiles getChainFiles(String fromVersion, String toVersion)
			throws SQLException {

        String query = "SELECT * FROM Chain_File NATURAL JOIN Chain_File_Files"
                + " WHERE (FromVersion ~~* ?)" + " AND (ToVersion ~~* ?)";
        PreparedStatement stmt = conn.prepareStatement(query);

		stmt.setString(1, fromVersion);
		stmt.setString(2, toVersion);
		ResultSet rs = stmt.executeQuery();

		ChainFiles cf = null;

		if (rs.next()) {
			cf = new ChainFiles(rs);
		}

		stmt.close();

		return cf;
	}

	/**
	 * Adds a chain file to database for conversions. Parameters: Oldversion,
	 * new version and filename. Returns: upload URL
	 *
	 * @param fromVersion version to convert from
	 * @param toVersion version to convert to
	 * @param fileName the name of the file
	 * @return the upload URL
	 * @throws SQLException
	 * @throws IOException
	 */
	public String addChainFile(String fromVersion, String toVersion,
			String fileName, String checkSumMD5) throws SQLException, IOException {

    	if(!FileValidator.fileNameCheck(fileName)){
    		throw new IOException("Invalid file name");
    	}

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

        String insertQuery2 = "INSERT INTO Chain_File_Files "
                + "(FromVersion, ToVersion, FileName, MD5) " + "VALUES (?, ?, ?, ?)";

        ChainFiles cf = getChainFiles(fromVersion, toVersion);
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
		stmt.setString(4, checkSumMD5);
		stmt.executeUpdate();
		stmt.close();

		String URL = ServerDependentValues.UploadURL;

		return URL + filePath;
	}

	/**
	 * Deletes a chain_file from the database and the physical files on the
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
	public int removeChainFiles(String fromVersion, String toVersion)
			throws SQLException {

		int resCount = 0;

		ChainFiles cf = getChainFiles(fromVersion, toVersion);

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

	/**
	 * Deletes a folder if it is empty, else open folder and repeat.
	 * @param folder the folder to delete
	 */
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

	/**
	 * Returns a list of all different species that have a genome in the
	 * database.
	 *
	 * @return List<String> - a list of all species
	 * @throws SQLException
	 *             - if the query does not succeed
	 */
	public List<String> getAllGenomeReleaseSpecies() throws SQLException {
		String query = "SELECT DISTINCT Species FROM Genome_Release";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		List<String> species = new ArrayList<String>();
		while (rs.next()) {
			species.add(rs.getString("Species"));
		}
		return species;
	}

	/**
	 * Returns a list of all genome releases in the database.
	 *
	 * @return List<Genome> - A list of genomes, if no genomes are found the
	 *         list is empty
	 * @throws SQLException
	 *             - if the query does not succeed
	 */
	public List<Genome> getAllGenomeReleases() throws SQLException {
		String query = "SELECT * FROM Genome_Release "
				+ "NATURAL JOIN Genome_Release_Files ";

		PreparedStatement stmt = conn.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();

		ArrayList<Genome> genomeList = new ArrayList<Genome>();

		if (rs.next()) {
			while (!rs.isAfterLast()) {
				Genome g = new Genome(rs);
				genomeList.add(g);
			}
		}

		stmt.close();
		return genomeList;
	}
}
