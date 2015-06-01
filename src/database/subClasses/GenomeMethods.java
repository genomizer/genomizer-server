package database.subClasses;

import database.FilePathGenerator;
import database.FileValidator;
import database.constants.ServerDependentValues;
import database.containers.ChainFile;
import database.containers.ChainFiles;
import database.containers.Genome;
import database.containers.GenomeFile;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains all the methods for adding,changing, getting and removing
 * genome releases and chain files. This class is a subClass of
 * databaseAcessor.java.
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

		String species;
		String folderPath;
		String file;
		ArrayList<String> fileList = new ArrayList<String>();

		String query = "SELECT * FROM Genome_Release AS R "
				+ "JOIN Genome_Release_Files AS F "
				+ "ON (R.Version = F.Version) "
				+ "WHERE (R.Version ~~* ?)";

		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, genomeVersion);
		ResultSet rs = stmt.executeQuery();

		//Get the initial info
		if(rs.next()) {
			species = rs.getString("Species");
			folderPath = rs.getString("FolderPath");
			file = rs.getString("FileName");
			if(file != null) {
				fileList.add(rs.getString("FileName"));
			}
		} else {
			//The genome did not exist
			return null;
		}

		//Loop through files.
		while (rs.next()) {
			file = rs.getString("FileName");
			if(file != null) {
				fileList.add(rs.getString("FileName"));
			}
		}

		stmt.close();

		return new Genome(genomeVersion, species, folderPath, fileList);
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
    public String addGenomeReleaseWithStatus(String genomeVersion, String species,
            String filename, String checkSumMD5, String status) throws SQLException, IOException {
    	if(!FileValidator.fileNameCheck(filename)){
    		throw new IOException("Invalid file name");
    	}
		String folderPath = fpg.generateGenomeReleaseFolder(genomeVersion,
				species);
		Genome genomeRelease = getGenomeRelease(genomeVersion);
		boolean genomeRelaseFileExists = genomeReleaseFileExists(genomeVersion, filename);

		String grQuery =
				"INSERT INTO Genome_Release " +
				"(Version, Species, FolderPath) VALUES (?, ?, ?)";
		String grFilesQuery =
				"INSERT INTO Genome_Release_Files " +
				"(Version, FileName, MD5, Status) VALUES (?, ?, ?, ?)";


		conn.setAutoCommit(false);

		try (PreparedStatement grStmt = conn.prepareStatement(grQuery);
			 PreparedStatement grFilesStmt = conn.prepareStatement(grFilesQuery);) {
			if (genomeRelease == null) {
				grStmt.setString(1, genomeVersion);
				grStmt.setString(2, species);
				grStmt.setString(3, folderPath);
				grStmt.executeUpdate();
			}

			if (genomeRelaseFileExists) {
				throw new IOException(filename + " already exists for this " +
						"genome release");
			}

			grFilesStmt.setString(1, genomeVersion);
			grFilesStmt.setString(2, filename);
			grFilesStmt.setString(3, checkSumMD5);
			grFilesStmt.setString(4, status);
			grFilesStmt.executeUpdate();

			conn.commit();

		} catch (SQLException e) {
			conn.rollback();
			throw e;
		} finally {
			conn.setAutoCommit(true);
		}

		return ServerDependentValues.UploadURL + folderPath + filename;
	}

	/**
	 * Given a filesystem path,
	 * retrieve the corresponding genome release file record.
	 *
	 * @param  file          file name.
	 * @throws SQLException  if something went wrong.
	 */
	public GenomeFile getGenomeReleaseFile (String file) throws SQLException {
		return getGenomeReleaseFileWithStatus(file, "Done");
	}

	/**
	 * Given a filesystem path and a status string,
	 * retrieve the corresponding genome release file record.
	 *
	 * @param  file          file name.
	 * @param  status        file status.
	 * @throws SQLException  if something went wrong.
	 */
	public GenomeFile getGenomeReleaseFileWithStatus (String file, String status)
			throws SQLException {
		File fileFile   = new File(file);
		String fileName = fileFile.getName();
		String fileDir  = fileFile.getParent();
		if (!fileDir.endsWith(File.separator))
			fileDir += File.separator;

		// Given a directory and a file name, get genome release version...
		try (PreparedStatement stmt = conn.prepareStatement(
				"SELECT * FROM Genome_Release_Files "
				+ "NATURAL JOIN Genome_Release "
				+ "WHERE FolderPath ~~* ? AND FileName ~~* ? AND Status = ?")) {
			stmt.setString(1, fileDir);
			stmt.setString(2, fileName);
			stmt.setString(3, status);
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
		return getChainFileWithStatus(file, "Done");
	}

	/**
	 * Given a filesystem path, retrieve the corresponding chain file record.
	 *
	 * @param  file          file name.
	 * @throws SQLException  if something went wrong.
	 */
	public ChainFile getChainFileWithStatus (String file, String status) throws SQLException {
		File fileFile   = new File(file);
		String fileName = fileFile.getName();
		String fileDir  = fileFile.getParent();
		if (!fileDir.endsWith(File.separator))
			fileDir += File.separator;

		try (PreparedStatement stmt = conn.prepareStatement(
				"SELECT * FROM Chain_File_Files "
				+ "NATURAL JOIN Chain_File "
				+ "WHERE FolderPath ~~* ? AND FileName ~~* ? AND Status = ?")) {
			stmt.setString(1, fileDir);
			stmt.setString(2, fileName);
			stmt.setString(3, status);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new ChainFile(rs);
			}
			else {
				return null;
			}
		}
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
	public int markReadyForDownload(String version, String fileName)
			throws SQLException {

        String statusUpdateString = "UPDATE Genome_Release_Files SET Status = 'Done' "
                + "WHERE Version = ? AND FileName = ?";


        try(PreparedStatement statusUpdate = conn
                .prepareStatement(statusUpdateString)) {
			statusUpdate.setString(1, version);
			statusUpdate.setString(2, fileName);


			return statusUpdate.executeUpdate();
		}
	}

	/**
	 * Sets the status for a chain file to "Done".
	 *
	 * @return    the number of tuples updated.
	 * @throws    SQLException
	 */
	public int markReadyForDownload(String fromVersion, String toVersion, String fileName)
			throws SQLException {

		String statusUpdateString = "UPDATE Chain_File_Files SET Status = 'Done' "
				+ "WHERE FromVersion = ? AND ToVersion = ? AND FileName = ?";

		try (PreparedStatement statusUpdate = conn
				.prepareStatement(statusUpdateString)) {
			statusUpdate.setString(1, fromVersion);
			statusUpdate.setString(2, toVersion);
			statusUpdate.setString(3, fileName);

			return statusUpdate.executeUpdate();
		}
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

		try (PreparedStatement stmt = conn.prepareStatement(
				"DELETE FROM Genome_Release "
						+ "WHERE Version ~~* ?")) {
			stmt.setString(1, genomeVersion);
			return (stmt.executeUpdate() > 0);
		}
	}

	public boolean removeGenomeReleaseFile (String genomeVersion,
											String filePath)
		throws SQLException, IOException {
		String fileName = new File(filePath).getName();

		try (PreparedStatement stmt = conn.prepareStatement(
				"DELETE FROM Genome_Release_Files"
				+ "WHERE FileName ~~* ?"
				+ "AND Version ~~* ?")) {
			stmt.setString(1, fileName);
			stmt.setString(2, genomeVersion);

			return (stmt.executeUpdate() > 0);
		}
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

        String query = "SELECT * FROM Genome_Release AS R "
                + "JOIN Genome_Release_Files AS F "
                + "ON (R.Version = F.Version) "
                + "WHERE (R.Species ~~* ? AND F.Status = 'Done') ORDER BY R.Version";

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
                + " WHERE (FromVersion ~~* ?)" + " AND (ToVersion ~~* ?)"
				+ " AND (Status = 'Done')";
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
	public String addChainFileWithStatus(String fromVersion, String toVersion,
										 String fileName, String checkSumMD5, String status) throws SQLException, IOException {

		if(!FileValidator.fileNameCheck(fileName)) {
			throw new IOException("Invalid file name");
		}

		String filePath = null;
		String speciesQuery =
				"SELECT Species FROM Genome_Release " +
				"WHERE (version ~~* ?)";
		String chainFileQuery =
				"INSERT INTO Chain_File " +
				"(FromVersion, ToVersion, FolderPath) " +
				"VALUES (?, ?, ?)";
		String chainFileFilesQuery =
				"INSERT INTO Chain_File_Files " +
				"(FromVersion, ToVersion, FileName, MD5, Status) " +
				"VALUES (?, ?, ?, ?, ?)";

		PreparedStatement selectSpecies = null;

		// Query 1: Retrieve species
		selectSpecies = conn.prepareStatement(speciesQuery);
		selectSpecies.setString(1, fromVersion);
		selectSpecies.addBatch();
		ResultSet rs = selectSpecies.executeQuery();
		String specie = "";
		if (rs.next()) {
			specie = rs.getString("Species");
		}
		selectSpecies.close();

		// Query 2: Get ChainFiles
		filePath = fpg.generateChainFolder(specie, fromVersion, toVersion);
		ChainFiles cf = getChainFiles(fromVersion, toVersion);

		conn.setAutoCommit(false);
		try (PreparedStatement insertChainFile = conn.prepareStatement(chainFileQuery);
			 PreparedStatement insertChainFileFiles = conn.prepareStatement(chainFileFilesQuery);) {
			// Query 3: Insert Chain File
			if (cf == null) {
				insertChainFile.setString(1, fromVersion);
				insertChainFile.setString(2, toVersion);
				insertChainFile.setString(3, filePath);
				insertChainFile.executeUpdate();
			}

			// Query 4: Insert Chain file Files
			insertChainFileFiles.setString(1, fromVersion);
			insertChainFileFiles.setString(2, toVersion);
			insertChainFileFiles.setString(3, fileName);
			insertChainFileFiles.setString(4, checkSumMD5);
			insertChainFileFiles.setString(5, status);
			insertChainFileFiles.executeUpdate();

			conn.commit();

		} catch (SQLException e) {
			conn.rollback();
			throw e;
		} finally {
			conn.setAutoCommit(true);
		}

		return ServerDependentValues.UploadURL + filePath;
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
		String query = "SELECT DISTINCT version FROM Genome_Release ";

		PreparedStatement stmt = conn.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();

		ArrayList<Genome> genomeList = new ArrayList<Genome>();
		String version;
		Genome tmpGenome;
		while(rs.next()) {
			version = rs.getString("version");
			tmpGenome = getGenomeRelease(version);
			genomeList.add(tmpGenome);
		}

		stmt.close();
		return genomeList;
	}
}
