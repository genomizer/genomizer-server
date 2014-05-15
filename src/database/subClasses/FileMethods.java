package database.subClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.FilePathGenerator;
import database.FileTuple;
import database.ServerDependentValues;


/**
 * Class that contains all the methods for adding,changing, getting and
 * removing Files in the database. This class is a subClass of
 * databaseAcessor.java.
 *
 * date: 2014-05-14
 * version: 1.0
 */
public class FileMethods {

	private Connection conn;
	private FilePathGenerator fpg;

	/**
	 * Constructor for the fileMethod object.
	 * @param connection Connection, the connection to the database.
	 */
	public FileMethods(Connection connection, FilePathGenerator filePG){

		conn = connection;
		fpg = filePG;
	}

	 /**
     * @param expID String
     *            The unique name of the experiment. OBS! If not null,
     *            this must reference an experiment that has been
     *            previously added.
     * @param fileType int
     *            An Integer identifying the file type eg.
     *            FileTuple.RAW
     * @param fileName String
     * @param inputFileName String
     *            The name of the corresponding input file or null if
     *            there is no corresponding input file
     * @param metaData String
     *            The parameters used in file creation or null if not
     *            applicable
     * @param author String
     * @param uploader String
     * @param isPrivate boolean
     * @param genomeRelease String
     *            The genome release version identifyer (eg. "hg38")
     *            or null if not applicable. OBS! If not null, this
     *            must reference a genome release that has been
     *            previously uploaded.
     * @return FileTuple - The FileTuple inserted in the database or null if no
     *         file was entered into the database.
     * @throws SQLException
     *             If the query could not be executed. (Probably
     *             because the file already exists)
     */
    public FileTuple addNewFile(String expID, int fileType,
            String fileName, String inputFileName, String metaData,
            String author, String uploader, boolean isPrivate,
            String genomeRelease) throws SQLException {

        String path = fpg.generateFilePath(expID, fileType, fileName);

        String inputFilePath = fpg.generateFilePath(expID, fileType,
                inputFileName);

        String query = "INSERT INTO File "
                + "(Path, FileType, FileName, Date, MetaData, InputFilePath, "
                + "Author, Uploader, IsPrivate, ExpID, GRVersion) "
                + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, path);

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

        stmt.setString(3, fileName);
        stmt.setString(4, metaData);
        stmt.setString(5, inputFilePath);
        stmt.setString(6, author);
        stmt.setString(7, uploader);
        stmt.setBoolean(8, isPrivate);
        stmt.setString(9, expID);
        stmt.setString(10, genomeRelease);

        stmt.executeUpdate();
        stmt.close();

        return getFileTuple(path);
    }

    /**
     * Returns the FileTuple object associated with the given
     * filePath.
     *
     * @param filePath String
     * @return FileTuple - The corresponding FileTuple or null if no such file
     * 					   exists
     * @throws SQLException
     *             If the query could not be executed.
     */
    public FileTuple getFileTuple(String filePath)
            throws SQLException {

        String query = "SELECT * FROM File WHERE Path = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, filePath);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
        	FileTuple fileTuple = new FileTuple(rs);
            stmt.close();
        	return fileTuple;
        }
        stmt.close();
        return null;
    }

    // Too many parameters. Should take a JSONObject or FileTuple
    // instead.
    /**
     * Adds a file to the database. Users should migrate to
     * serverAddFile(...) which returns the FileTuple added to the
     * database.
     *
     * @param fileType
     * @param fileName
     * @param metaData
     * @param author
     * @param uploader
     * @param isPrivate
     * @param expID
     * @param grVersion
     * @return the number if tuples inserted to the database.
     * @throws SQLException
     *             if the query does not succeed
     */
    @Deprecated
    public String addFile(String fileType, String fileName,
            String metaData, String author, String uploader,
            boolean isPrivate, String expID, String grVersion)
            throws SQLException {

        String path = fpg.generateFilePath(expID, fileType, fileName);

        String query = "INSERT INTO File "
                + "(Path, FileType, FileName, Date, MetaData, InputFilePath, "
                + "Author, Uploader, IsPrivate, ExpID, GRVersion) "
                + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, NULL, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setString(1, path);
        stmt.setString(2, fileType);
        stmt.setString(3, fileName);
        stmt.setString(4, metaData);
        stmt.setString(5, author);
        stmt.setString(6, uploader);
        stmt.setBoolean(7, isPrivate);
        stmt.setString(8, expID);
        stmt.setString(9, grVersion);

        stmt.executeUpdate();
        stmt.close();

        return path;
    }

    /**
     * Adds a file to the database with URL. Use clientAddFile(...)
     *
     * @param fileType
     * @param fileName
     * @param metaData
     * @param author
     * @param uploader
     * @param isPrivate
     * @param expID
     * @param grVersion
     * @return the number if tuples inserted to the database.
     * @throws SQLException
     *             if the query does not succeed
     */
    @Deprecated
    public String addFileURL(String fileType, String fileName,
            String metaData, String author, String uploader,
            boolean isPrivate, String expID, String grVersion)
            throws SQLException {

        String path = fpg.generateFilePath(expID, fileType, fileName);
        String URL = ServerDependentValues.UploadURL;

        String query = "INSERT INTO File "
                + "(Path, FileType, FileName, Date, MetaData, InputFilePath, "
                + "Author, Uploader, IsPrivate, ExpID, GRVersion) "
                + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, NULL, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, path);
        stmt.setString(2, fileType);
        stmt.setString(3, fileName);
        stmt.setString(4, metaData);
        stmt.setString(5, author);
        stmt.setString(6, uploader);
        stmt.setBoolean(7, isPrivate);
        stmt.setString(8, expID);
        stmt.setString(9, grVersion);

        stmt.executeUpdate();
        stmt.close();

        return URL + path;
    }

    /**
     * Deletes a file from the database.
     *
     * @param path String - the path to the file.
     * @return int - the number of deleted tuples in the database.
     * @throws SQLException
     *             if the query does not succeed
     */
    public int deleteFile(String path) throws SQLException {

    	String stmt = "DELETE FROM File " + "WHERE (Path = ?)";
        PreparedStatement deleteFile = conn.prepareStatement(stmt);

        deleteFile.setString(1, path);
        int resCount = deleteFile.executeUpdate();
        deleteFile.close();

        return resCount;
    }

    /**
     * Deletes a file from the database using the fileID.
     *
     * @param fileID int - the fileID of the file to be deleted.
     * @return 1 if deletion was successful, else 0.
     * @throws SQLException
     */
    public int deleteFile(int fileID) throws SQLException {

        String query = "DELETE FROM File " + "WHERE FileID = ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, fileID);

        int resCount = stmt.executeUpdate();
        stmt.close();

        return resCount;
    }

    /**
     * Checks if the file with the specified fileID exists in the
     * database.
     *
     * @param fileID int - the fileID of the file.
     * @return true if the file exists, else false.
     * @throws SQLException
     */
    public boolean hasFile(int fileID) throws SQLException {

    	String query = "SELECT fileID FROM File " + "WHERE fileID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, fileID);

        ResultSet rs = stmt.executeQuery();
        boolean hasResult = rs.next();

        if (rs.next()) {
            hasResult = false;
        }

        stmt.close();

        return hasResult;
    }

    /**
     * Checks if the file path is a valid file path. Not used.
     *
     * @param filePath
     * @return
     * @throws SQLException
     *             if the query does not succeed
     */
    @Deprecated
    public boolean isValidFilePath(String filePath)
            throws SQLException {

        PreparedStatement stmt = null;
        String query = "SELECT * FROM File Where (Path = ?)";

        stmt = conn.prepareStatement(query);
        stmt.setString(1, filePath);
        ResultSet rs = stmt.executeQuery();

        boolean resCount = rs.next();
        stmt.close();

        return resCount;
    }

    /**
     * Deprecated: Use ProcessRawToProfile(...)
     *
     * Method to convert from raw data to profile data. Returns a list
     * of filepaths
     *
     * @param fileID
     * @param fileType
     * @param fileName
     * @param metaData
     * @param uploader
     * @param grVersion
     * @param expID
     * @return ArrayList<String>
     * @throws SQLException
     */
    @Deprecated
    public ArrayList<String> process(String fileID, String fileType,
            String fileName, String metaData, String uploader,
            String grVersion, String expID) throws SQLException {

        ArrayList<String> pathList = new ArrayList<String>();
        String toPath;

        String query = "SELECT Path, Author, IsPrivate FROM File"
                + " WHERE (FileID = ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        int fID = Integer.parseInt(fileID);
        stmt.setInt(1, fID);

        ResultSet rs = stmt.executeQuery();

        String fromPath = null;
        boolean isPrivate = false;
        String author = null;

        if (rs.next()) {
            fromPath = rs.getString("Path");
            author = rs.getString("Author");
            isPrivate = rs.getBoolean("IsPrivate");
        } else {
            throw new SQLException("Not a valid fileID");
        }

        toPath = addFile(fileType, fileName, metaData, author,
                uploader, isPrivate, expID, grVersion);
        stmt.close();

        pathList.add(fromPath);
        pathList.add(toPath);

        return pathList;
    }

}
