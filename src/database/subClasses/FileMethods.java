package database.subClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.FilePathGenerator;
import database.FileTuple;
import database.ServerDependentValues;



public class FileMethods {

	private Connection conn;
	private FilePathGenerator fpg;

	public FileMethods(Connection connection, FilePathGenerator filePG){

			conn = connection;
			fpg = filePG;
	}

	 /**
     * @param expID
     *            The unique name of the experiment. OBS! If not null,
     *            this must reference an experiment that has been
     *            previously added.
     * @param fileType
     *            An Integer identifying the file type eg.
     *            FileTuple.RAW
     * @param fileName
     * @param inputFileName
     *            The name of the corresponding input file or null if
     *            there is no corresponding input file
     * @param metaData
     *            The parameters used in file creation or null if not
     *            applicable
     * @param author
     * @param uploader
     * @param isPrivate
     * @param genomeRelease
     *            The genome release version identifyer (eg. "hg38")
     *            or null if not applicable. OBS! If not null, this
     *            must reference a genome release that has been
     *            previously uploaded.
     * @return The FileTuple inserted in the database or null if no
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
        PreparedStatement addFile = conn.prepareStatement(query);
        addFile.setString(1, path);

        switch (fileType) {
        case FileTuple.RAW:
            addFile.setString(2, "Raw");
            break;
        case FileTuple.PROFILE:
            addFile.setString(2, "Profile");
            break;
        case FileTuple.REGION:
            addFile.setString(2, "Region");
            break;
        default:
            addFile.setString(2, "Other");
            break;
        }

        addFile.setString(3, fileName);
        addFile.setString(4, metaData);
        addFile.setString(5, inputFilePath);
        addFile.setString(6, author);
        addFile.setString(7, uploader);
        addFile.setBoolean(8, isPrivate);
        addFile.setString(9, expID);
        addFile.setString(10, genomeRelease);

        addFile.executeUpdate();
        addFile.close();

        return getFileTuple(path);
    }

    /**
     * Returns the FileTuple object associated with the given
     * filePath.
     *
     * @param filePath
     * @return The corresponding FileTuple or null if no such file exists
     * @throws SQLException
     *             If the query could not be executed.
     */
    public FileTuple getFileTuple(String filePath)
            throws SQLException {
        String query = "SELECT * FROM File WHERE Path = ?";
        PreparedStatement getFile = conn.prepareStatement(query);
        getFile.setString(1, filePath);
        ResultSet rs = getFile.executeQuery();
        if (rs.next()) {
            return new FileTuple(rs);
        }
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
        PreparedStatement addFile = conn.prepareStatement(query);

        addFile.setString(1, path);
        addFile.setString(2, fileType);
        addFile.setString(3, fileName);
        addFile.setString(4, metaData);
        addFile.setString(5, author);
        addFile.setString(6, uploader);
        addFile.setBoolean(7, isPrivate);
        addFile.setString(8, expID);
        addFile.setString(9, grVersion);

        addFile.executeUpdate();
        addFile.close();

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
        PreparedStatement tagExp = conn.prepareStatement(query);
        tagExp.setString(1, path);
        tagExp.setString(2, fileType);
        tagExp.setString(3, fileName);
        tagExp.setString(4, metaData);
        tagExp.setString(5, author);
        tagExp.setString(6, uploader);
        tagExp.setBoolean(7, isPrivate);
        tagExp.setString(8, expID);
        tagExp.setString(9, grVersion);

        tagExp.executeUpdate();
        tagExp.close();

        return URL + path;
    }

    /**
     * Deletes a file from the database.
     *
     * @param path
     *            the path to the file.
     * @return the number of deleted tuples in the database.
     * @throws SQLException
     *             if the query does not succeed
     */
    public int deleteFile(String path) throws SQLException {

    	String statementStr = "DELETE FROM File " + "WHERE (Path = ?)";
        PreparedStatement deleteFile = conn.prepareStatement(statementStr);

        deleteFile.setString(1, path);
        int res = deleteFile.executeUpdate();
        deleteFile.close();

        return res;
    }

    /**
     * Deletes a file from the database using the fileID.
     *
     * @param fileID
     *            the fileID of the file to be deleted.
     * @return 1 if deletion was successful, else 0.
     * @throws SQLException
     */
    public int deleteFile(int fileID) throws SQLException {

        String query = "DELETE FROM File " + "WHERE FileID = ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, fileID);

        return stmt.executeUpdate();
    }

    /**
     * Checks if the file with the specified fileID exists in the
     * database.
     *
     * @param fileID
     *            the fileID of the file.
     * @return true if the file exists, else false.
     * @throws SQLException
     */
    public boolean hasFile(int fileID) throws SQLException {

    	String query = "SELECT fileID FROM File " + "WHERE fileID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, fileID);

        ResultSet rs = stmt.executeQuery();
        boolean res = rs.next();

        if (rs.next()) {
            res = false;
        }

        stmt.close();

        return res;
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

        PreparedStatement pStatement = null;
        String query = "SELECT * FROM File Where (Path = ?)";

        pStatement = conn.prepareStatement(query);
        pStatement.setString(1, filePath);

        ResultSet rs = pStatement.executeQuery();

        boolean res = rs.next();
        pStatement.close();

        return res;
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
        String ToPath;

        String SelectQuery = "SELECT Path, Author, IsPrivate FROM File"
                + " WHERE (FileID = ?)";
        PreparedStatement ps = conn.prepareStatement(SelectQuery);
        int fID = Integer.parseInt(fileID);
        ps.setInt(1, fID);

        ResultSet rs = ps.executeQuery();

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

        ToPath = addFile(fileType, fileName, metaData, author,
                uploader, isPrivate, expID, grVersion);
        ps.close();

        pathList.add(fromPath);
        pathList.add(ToPath);

        return pathList;
    }

}
