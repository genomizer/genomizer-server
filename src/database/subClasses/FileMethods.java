package database.subClasses;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.FilePathGenerator;
import database.FileValidator;
import database.containers.Experiment;
import database.containers.FileTuple;
import database.containers.FileTupleTemplate;
import database.containers.FileTupleTemplateBuilder;

/**
 * Class that contains all the methods for adding,changing, getting and removing
 * Files in the database. This class is a subClass of databaseAcessor.java.
 *
 * date: 2014-05-14 version: 1.0
 */
public class FileMethods {

	private Connection conn;
	private FilePathGenerator fpg;
	private ExperimentMethods expMethods;

	/**
	 * Constructor for the fileMethod object.
	 *
	 * @param connection
	 *            Connection, the connection to the database.
	 */
	public FileMethods(Connection connection, FilePathGenerator filePG,
			ExperimentMethods expMethods) {

		conn = connection;
		fpg = filePG;
		this.expMethods = expMethods;
	}

	/**
	 * @param expID
	 *            String The unique name of the experiment. OBS! If not null,
	 *            this must reference an experiment that has been previously
	 *            added.
	 * @param fileType
	 *            int An Integer identifying the file type eg. FileTuple.Raw
	 * @param fileName
	 *            String
	 * @param inputFileName
	 *            String The name of the corresponding input file or null if
	 *            there is no corresponding input file
	 * @param metaData
	 *            String The parameters used in file creation or null if not
	 *            applicable
	 * @param author
	 *            String
	 * @param uploader
	 *            String
	 * @param isPrivate
	 *            boolean
	 * @param genomeRelease
	 *            String The genome release version identifyer (eg. "hg38") or
	 *            null if not applicable. OBS! If not null, this must reference
	 *            a genome release that has been previously uploaded.
	 * @param checkSumMD5
	 *            MD5 checksum of the file. Can be null.
	 * @return FileTuple - The FileTuple inserted in the database or null if no
	 *         file was entered into the database.
	 * @throws SQLException
	 *             If the query could not be executed. Possible reasons:
	 *             Duplicate file, Does not reference a valid GenomeRelease.
	 * @throws IOException
	 *             If the experiment does not exist.
	 */
	public FileTuple addNewFile(String expID, int fileType, String fileName,
			String inputFileName, String metaData, String author,
			String uploader, boolean isPrivate, String genomeRelease,
			String checkSumMD5)
			throws SQLException, IOException {


        return this.addNewFile((new FileTupleTemplateBuilder())
                        .fromType(fileType)
                        .withExpId(expID)
                        .withIsPrivate(isPrivate)
                        .withAuthor(author)
                        .withGrVersion(genomeRelease)
                        .withMetaData(metaData)
                        .withUploader(uploader)
                        .build(),
                fileName,
                inputFileName,
                checkSumMD5);

	}

    public FileTuple addNewFile(FileTupleTemplate ft,
                                String fileName,
                                String inputFileName,
                                String MD5checksum)
            throws SQLException, IOException {

        if (!FileValidator.fileNameCheck(fileName)) {
            throw new IOException("Invalid filename");
        }

        if (inputFileName != null
                && !FileValidator.fileNameCheck(inputFileName)) {
            throw new IOException("Invalid input filename:" + inputFileName );

        }

        Experiment e = expMethods.getExperiment(ft.getExpId());

        if (e == null) {
            throw new IOException("The experiment " + ft.getExpId()
                    + " does not exist!");
        }
        // TODO Check: is this needed? -NG
        String expID = e.getID(); // Correct expID for in case sensitivity

        if (ft.getType() == FileTuple.Type.Raw && e.getNrRawFiles() >= 2) {
            throw new IOException(
                    "There are already two raw files for this experiment!");
        }

        FileTuple ftp = getProfile(e, ft.getMetaData());
        String path;
        if (ftp == null) {
            path = fpg.generateFilePath(expID, ft.getType(), fileName);
        } else {
            path = ftp.getFolderPath() + fileName;
            File profileToAdd = new File(path);
            if (profileToAdd.exists()) {
                throw new IOException(fileName + " with the parameters "
                        + ft.getMetaData() + " already exists!");
            }
        }

        String inputFilePath = null;
        if (inputFileName != null) {
            inputFilePath = getParentFolder(path) + inputFileName;
        }

        String query = "INSERT INTO File "
                + "(Path, FileType, FileName, Date, MetaData, InputFilePath, "
                + "Author, Uploader, IsPrivate, ExpID, GRVersion, MD5) "
                + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?)";
        String genomeRelease = ft.getGrVersion();
        try(PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, path);

            switch (ft.getType()) {
                case Raw:
                    stmt.setString(2, "Raw");
                    genomeRelease = null;
                    break;
                case Profile:
                    stmt.setString(2, "Profile");
                    break;
                case Region:
                    stmt.setString(2, "Region");
                    break;
                default:
                    stmt.setString(2, "Other");
                    break;
            }

            stmt.setString(3, fileName);
            stmt.setString(4, ft.getMetaData());
            stmt.setString(5, inputFilePath);
            stmt.setString(6, ft.getAuthor());
            stmt.setString(7, ft.getUploader());
            stmt.setBoolean(8, ft.isPrivate());
            stmt.setString(9, expID);
            stmt.setString(10, genomeRelease);
            stmt.setString(11, MD5checksum);

            stmt.executeUpdate();
        }

        String querygetID = "SELECT FileID FROM File "
                + "WHERE Path = ?";
        int id;
        try (PreparedStatement stmt = conn.prepareStatement(querygetID)) {
            stmt.setString(1, path);
            ResultSet rss = stmt.executeQuery();
            rss.next();
            id = rss.getInt(1);

        }


        String query2 = "INSERT INTO Parent "
                + "(FileID, ParentID) "
                + "VALUES (?, ?) ";
        for (Integer i : ft.getParents()) {
            try (PreparedStatement stmt = conn.prepareStatement(query2)) {
                stmt.setInt(1, id);
                stmt.setInt(2, i);
                stmt.executeUpdate();
            }
        }

        return ft.toFileTuple(id, path, inputFilePath);
    }


	private FileTuple getProfile(Experiment e, String metaData) {

        for (FileTuple ft : e.getFiles()) {

            if (ft.getType().name().equalsIgnoreCase("profile")) {

                if (metaData == null && ft.getMetaData() == null) {
                    return ft;
                }

                if (ft.getMetaData() != null) {
                    if (ft.getType().name().equalsIgnoreCase("profile")
                            && ft.getMetaData().equals(metaData)) {
                        return ft;
                    }
                }
            }

        }
        return null;
    }

	private String getParentFolder(String filePath) {

		int filenameIndex = filePath.lastIndexOf(File.separator);
		return filePath.substring(0, filenameIndex + 1);
	}

	private String getFileName(String filePath) {

		int filenameIndex = filePath.lastIndexOf(File.separator);
		return filePath.substring(filenameIndex + 1);
	}

	/**
	 * Returns the FileTuple object associated with the given filePath.
	 *
	 * @param filePath
	 *            String
	 * @return FileTuple - The corresponding FileTuple or null if no such file
	 *         exists
	 * @throws SQLException
	 *             If the query could not be executed.
	 */
	public FileTuple getFileTuple(String filePath) throws SQLException {

		String query = "SELECT * FROM File WHERE Path = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, filePath);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return  new FileTuple(rs);
            }
        }
        return null;
    }

	/**
	 * Returns the FileTuple object associated with the given fileID.
	 *
	 * @param fileID
	 *            int
	 * @return FileTuple - The corresponding FileTuple or null if no such file
	 *         exists
	 * @throws SQLException
	 *             If the query could not be executed.
	 */
	public FileTuple getFileTuple(int fileID) throws SQLException {

        String query = "SELECT * FROM File WHERE FileID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, fileID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new FileTuple(rs);
            }
        }
        return null;
    }

    public List<Integer> getParentIDs(int fileID) throws
            SQLException {
        ArrayList<Integer> parids = new ArrayList<>();

        String query = "SELECT ParentID FROM Parent " +
                "WHERE FileID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, fileID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                parids.add(rs.getInt(1));
            }
        }

        return parids;

    }
    /**
     * Deletes a file from the database and the disk. Should throw an
     * IOException if the method failed to delete the file from disk.
     *
     * @param path
     *            String - the path to the file.
     * @return int - the number of deleted tuples in the database.
     * @throws SQLException
     *             if the query does not succeed
     * @throws IOException
     */
    public int deleteFile(String path) throws SQLException, IOException {

        FileTuple ft = getFileTuple(path);

        if (ft == null) {
            throw new IOException("Could not find file at path " + path);
        }

        File fileToDelete = new File(path);
        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }

        File parentFolder = new File(ft.getFolderPath());
        if (ft.getType().name().equalsIgnoreCase("profile") && isEmptyFolder(parentFolder)) {
            parentFolder.delete();
        }
        int resCount;
        String statementStr = "DELETE FROM File " + "WHERE (Path ~~* ?)";
        try (PreparedStatement deleteFile = conn.prepareStatement(statementStr)) {
            deleteFile.setString(1, path);
            resCount = deleteFile.executeUpdate();
        }

        return resCount;
    }

	/**
	 * Deletes a file from the database and the disk using the fileID. Should
	 * throw an IOException if the method failed to delete the file from disk.
	 *
	 * @param fileID
	 *            int - the fileID of the file to be deleted.
	 * @return 1 if deletion was successful, else 0.
	 * @throws SQLException
	 *             If the database could not be contacted
	 * @throws IOException
	 *             If the FileID does not exist in the database
	 */
	public int deleteFile(int fileID) throws SQLException, IOException {

		FileTuple ft = getFileTuple(fileID);

		if (ft == null) {
			throw new IOException("Could not find file with ID " + fileID);
		}

        File fileToDelete = new File(ft.getFullPath());

        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }

        File parentFolder = new File(ft.getFolderPath());
        if (ft.getType().name().equalsIgnoreCase("profile") && isEmptyFolder(parentFolder)) {
            parentFolder.delete();
        }

        String query = "DELETE FROM File " + "WHERE FileID = ?";
        int res;
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, fileID);
            res = stmt.executeUpdate();
        }
        return res;
    }

	private boolean isEmptyFolder(File f) {

        return f.exists() && f.listFiles() != null && f.listFiles().length == 0;

    }

	/**
	 * Recursively deletes a folder with all it's subfolders and files.
	 *
	 * @param folder
	 *            the folder to delete.
	 */
	public void recursiveDelete(File folder) {

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
	 * Checks if the file with the specified fileID exists in the database.
	 *
	 * @param fileID
	 *            int - the fileID of the file.
	 * @return true if the file exists, else false.
	 * @throws SQLException
	 */
	public boolean hasFile(int fileID) throws SQLException {

		String query = "SELECT fileID FROM File " + "WHERE fileID = ?";
        boolean hasResult;
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, fileID);

            ResultSet rs = stmt.executeQuery();
            hasResult = rs.next();

            if (rs.next()) {
                hasResult = false;
            }

        }

        return hasResult;
	}

	/**
	 * Adds a parent to a file. I.e. mark that the file with parentId as FileID
	 * was used in the processing to generate the file with fileId as its FileID.
	 *
	 * @param fileId - ID of file to add parent to.
	 * @param parentId - ID of the file to use as parent.
	 * @throws SQLException if the query did not succeed
	 */
	public void addParent(int fileId, int parentId) throws SQLException {
		String query = "INSERT INTO Parent "
                + "(FileID, ParentID) "
                + "VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, String.valueOf(fileId));
            stmt.setString(2, String.valueOf(parentId));
            stmt.executeUpdate();
        }
	}

	/**
	 * Sets the status of a file to "Done".
	 *
	 * @param fileID
	 *            the ID of the file to set to "Done".
	 * @return the number of tuples updated.
	 * @throws SQLException
	 */
	public int fileReadyForDownload(int fileID) throws SQLException {

		String statusUpdateString = "UPDATE File SET Status = 'Done' "
				+ "WHERE FileID = ?";
        int resCount;
		try(PreparedStatement statusUpdate = conn
				.prepareStatement(statusUpdateString)) {
            statusUpdate.setInt(1, fileID);

            resCount = statusUpdate.executeUpdate();
        }

		return resCount;
	}

	/**
	 * Changes the Filename for a specific file with given fileID. This method
	 * affects bothe the saved file name, but also the entries path and fileName
	 * in database.
	 *
	 * @return resCount int, the number of rows affected by the change.
	 * @throws SQLException
	 *             if failed to send query,
	 * @throws IOException
	 *             if the chosen new file name already exist as a stored file.
	 */
    public int changeFileName(int fileID, String newFileName)
            throws SQLException, IOException {

        String oldFilePath;

        // search for current filepath.
        String searchPathQuery = "SELECT Path FROM File WHERE fileID = ?";
        try (PreparedStatement pathFind = conn.prepareStatement(searchPathQuery)) {
            pathFind.setInt(1, fileID);
            ResultSet res = pathFind.executeQuery();

            if (res.next()) {
                oldFilePath = res.getString("Path");
            } else {
                throw new IOException("No file with ID " + fileID);
            }
        }

        String folderPath = getParentFolder(oldFilePath);
        String newFilePath = folderPath + newFileName;

        // change name on the actual stored file.
        File oldfile = new File(oldFilePath);
        File newFile = new File(newFilePath);

        if (newFile.exists())
            throw new java.io.IOException("New file exists");

        if (!oldfile.exists())
            throw new java.io.IOException("Old file " + "does not exists");

        String chFileNameQuery = "UPDATE File SET FileName = ? "
                + "WHERE FileID = ?";
        int resCount;
        try (PreparedStatement nameUpdate = conn.prepareStatement(chFileNameQuery)) {
            nameUpdate.setString(1, newFileName);
            nameUpdate.setInt(2, fileID);
            resCount = nameUpdate.executeUpdate();
        }
        oldfile.renameTo(newFile);

        // change filepath entry in database.
        String chFilePathQuery = "UPDATE File SET Path = ? "
                + "WHERE FileID = ?";
        try (PreparedStatement pathUpdate = conn.prepareStatement(chFilePathQuery)) {
            pathUpdate.setString(1, newFilePath);
            pathUpdate.setInt(2, fileID);
            pathUpdate.executeUpdate();
        }
        return resCount;
    }

	public FileTuple addGeneratedFile(String expId, int fileType,
			String filePath, String inputFileName, String metaData,
			String uploader, boolean isPrivate, String grVersion,
			String checkSumMD5)
			throws SQLException, IOException {

		Experiment e = expMethods.getExperiment(expId);

		if (e == null) {
			throw new IOException("The experiment " + expId
					+ " does not exist!");
		}

		expId = e.getID();
		String inputFilePath = null;

		if (inputFileName != null) {
			inputFilePath = getParentFolder(filePath) + inputFileName;
		}

		String query = "INSERT INTO File "
				+ "(Path, FileType, FileName, Date, MetaData, InputFilePath, "
				+ "Author, Uploader, IsPrivate, ExpID, GRVersion, Status, MD5) "
				+ "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, 'Genomizer',"
				+ " ?, ?, ?, ?, 'Done', ?)";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
		stmt.setString(1, filePath);

		switch (fileType) {
		case FileTuple.RAW:
			stmt.setString(2, "Raw");
			break;
		case FileTuple.PROFILE:
			stmt.setString(2, "Profile");
			break;
		case FileTuple.REGION:
			stmt.setString(2, "Region");
			break;
		default:
			stmt.setString(2, "Other");
			break;
		}

		stmt.setString(3, getFileName(filePath));
		stmt.setString(4, metaData);
		stmt.setString(5, inputFilePath);
		stmt.setString(6, uploader);
		stmt.setBoolean(7, isPrivate);
		stmt.setString(8, expId);
		stmt.setString(9, grVersion);
		stmt.setString(10, checkSumMD5);

		stmt.executeUpdate();
		}

		return getFileTuple(filePath);
	}

    public FileTuple addGeneratedFile(FileTupleTemplate ftt,
                                      String filename,
                                      String inputFileName)
            throws SQLException, IOException {

        return this.addNewFile(ftt, filename, inputFileName, null);

    }
}
