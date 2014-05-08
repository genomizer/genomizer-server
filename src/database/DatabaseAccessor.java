package database;

import java.io.IOException;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * PREREQUISITES: The construction parameters must reference a
 * postgresql database with the genomizer database tables preloaded.
 * This is done by running the genomizer_database_tables.sql.
 *
 * DatabaseAccessor manipulates the underlying postgresql database
 * using SQL commands.
 *
 * Developed by the Datastorage group for the Genomizer Project,
 * Software Engineering course at Umeå University 2014.
 *
 * @author dv12rwt, Ruaridh Watt
 * @author dv12kko, Kenny Kunto
 * @author dv12ann, André Niklasson
 * @author dv12can, Carl Alexandersson
 * @author yhi04jeo, Jonas Engbo
 * @author oi11mhn, Mattias Hinnerson
 *
 */
public class DatabaseAccessor {

    public static Integer FREETEXT = 1;
    public static Integer DROPDOWN = 2;

    private Connection conn;
    private PubMedToSQLConverter pm2sql;

    /**
     * Creates a databaseAccessor that opens a connection to a
     * database.
     *
     * @param username
     *            - The username to log in to the database as. Should
     *            be "c5dv151_vt14" as of now.
     * @param password
     *            - The password to log in to the database. Should be
     *            "shielohh" as of now.
     * @param host
     *            - The name of the database management system. Will
     *            problebly always be "postgres" unless the DMS is
     *            switched with something else.
     * @param database
     * @throws SQLException
     * @throws IOException
     */
    public DatabaseAccessor(String username, String password,
            String host, String database) throws SQLException {

        String url = "jdbc:postgresql://" + host + "/" + database;
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        conn = DriverManager.getConnection(url, props);

        pm2sql = new PubMedToSQLConverter();

    }

    /**
     * Public method to check if the instance of the class is
     * connected to a database.
     *
     * @return boolean, true if it is connected, otherwise false.
     */
    public boolean isConnected() {
        return conn != null;
    }

    /**
     * Method to add a new user to the database.
     *
     * @param String
     *            the username
     * @param String
     *            the password
     * @param String
     *            the role given to the user ie. "Admin"
     * @throws SQLException
     */
    public void addUser(String username, String password, String role)
            throws SQLException {
        String userString = "INSERT INTO User_Info "
                + "(Username, Password, Role) VALUES " + "(?, ?, ?)";
        PreparedStatement addUser = conn.prepareStatement(userString);
        addUser.setString(1, username);
        addUser.setString(2, password);
        addUser.setString(3, role);

        addUser.executeUpdate();
        addUser.close();
    }

    /**
     * Returns an ArrayList which contains the usernames of all the
     * users in the database in the form of strings.
     *
     * @return an ArrayList of usernames.
     * @throws SQLException
     *             if the query does not succeed
     */
    public List<String> getUsers() throws SQLException {
        ArrayList<String> users = new ArrayList<String>();
        String query = "SELECT Username FROM User_Info";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            users.add(rs.getString("Username"));
        }
        stmt.close();
        return users;
    }

    /**
     * Deletes a user from the database.
     *
     * @param username
     *            the username of the user to be deleted.
     * @throws SQLException
     *             if the query does not succeed
     */
    public void deleteUser(String username) throws SQLException {

        String statementStr = "DELETE FROM User_Info "
                + "WHERE (Username = ?)";
        PreparedStatement deleteUser = conn
                .prepareStatement(statementStr);
        deleteUser.setString(1, username);
        deleteUser.executeUpdate();
        deleteUser.close();
    }

    /**
     * Closes the connection to the database, releasing all resources
     * it uses.
     *
     * @throws SQLException
     *             if a database access error occurs
     */
    public void close() throws SQLException {
        conn.close();
    }

    /**
     * Returns the password for the given user. Used for login.
     *
     * @param user
     *            - the username as string
     * @return String - the password
     * @throws SQLException
     *             if the query does not succeed
     */
    public String getPassword(String user) throws SQLException {
        String query = "SELECT Password FROM User_Info "
                + "WHERE (Username = ?)";
        PreparedStatement getPassword = conn.prepareStatement(query);
        getPassword.setString(1, user);
        ResultSet rs = getPassword.executeQuery();
        String pass = null;
        if (rs.next()) {
            pass = rs.getString("password");
        }
        getPassword.close();
        return pass;
    }

    /**
     * Changes the password for a user.
     *
     * @param username
     *            the user to change the password for.
     * @param newPassword
     *            the new password.
     * @return the number of tuples updated in the database.
     * @throws SQLException
     *             if the query does not succeed
     */
    public int resetPassword(String username, String newPassword)
            throws SQLException {
        String query = "UPDATE User_Info SET Password = ? "
                + "WHERE (Username = ?)";
        PreparedStatement resetPassword = conn
                .prepareStatement(query);
        resetPassword.setString(1, newPassword);
        resetPassword.setString(2, username);
        int res = resetPassword.executeUpdate();
        resetPassword.close();
        return res;

    }

    /**
     * Sets the role (permissions) for the user.
     *
     * @param username
     *            the user to set the role for.
     * @param role
     *            the role to set for the user.
     * @return returns the number of tuples updated in the database.
     * @throws SQLException
     *             if the query does not succeed
     */
    public int setRole(String username, String role)
            throws SQLException {
        String query = "UPDATE User_Info SET Role = ? "
                + "WHERE (Username = ?)";
        PreparedStatement setRole = conn.prepareStatement(query);
        setRole.setString(1, role);
        setRole.setString(2, username);
        int res = setRole.executeUpdate();
        setRole.close();
        return res;
    }

    /**
     * Gets the role (permissions) for a user.
     *
     * @param username
     *            the user to get the role for.
     * @return the role as a string.
     * @throws SQLException
     *             if the query does not succeed
     */
    public String getRole(String username) throws SQLException {
        String query = "SELECT Role FROM User_Info "
                + "WHERE (Username = ?)";
        PreparedStatement getRole = conn.prepareStatement(query);
        getRole.setString(1, username);
        ResultSet rs = getRole.executeQuery();
        String role = null;
        if (rs.next()) {
            role = rs.getString("Role");
        }
        getRole.close();
        return role;
    }

    /**
     * Adds a free text annotation to the list of possible
     * annotations.
     *
     * @param label
     *            the name of the annotation.
     * @param required
     *            if the annotation should be forced or not
     * @param defaultValue
     *            the default value this field should take or null if
     *            a default value is not required
     * @return the number of tuples updated in the database.
     * @throws SQLException
     *             if the query does not succeed
     */
    public int addFreeTextAnnotation(String label,
            String defaultValue, boolean required)
            throws SQLException {
        String query = "INSERT INTO Annotation "
                + "VALUES (?, 'FreeText', ?, ?)";
        PreparedStatement addAnnotation = conn
                .prepareStatement(query);
        addAnnotation.setString(1, label);
        addAnnotation.setString(2, defaultValue);
        addAnnotation.setBoolean(3, required);
        int res = addAnnotation.executeUpdate();
        addAnnotation.close();
        return res;
    }

    /**
     * Gets all the annotation possibilities from the database.
     *
     * @return a Map with the label string as key and datatype as
     *         value.
     *
     *         The possible datatypes are FREETEXT and DROPDOWN.
     * @throws SQLException
     *             if the query does not succeed
     */
    public Map<String, Integer> getAnnotations() throws SQLException {
        HashMap<String, Integer> annotations = new HashMap<String, Integer>();
        String query = "SELECT * FROM Annotation";
        Statement getAnnotations = conn.createStatement();
        ResultSet rs = getAnnotations.executeQuery(query);
        while (rs.next()) {
            if (rs.getString("DataType").equalsIgnoreCase("FreeText")) {
                annotations.put(rs.getString("Label"),
                        Annotation.FREETEXT);
            } else {
                annotations.put(rs.getString("Label"),
                        Annotation.DROPDOWN);
            }
        }
        getAnnotations.close();

        return annotations;
    }

    /**
     * Deletes an annotation from the list of possible annotations.
     *
     * @param label
     *            the label of the annotation to delete.
     * @return the number of tuples deleted in the database.
     * @throws SQLException
     *             if the query does not succeed
     */
    public int deleteAnnotation(String label) throws SQLException {

        String statementStr = "DELETE FROM Annotation "
                + "WHERE (Label = ?)";
        PreparedStatement deleteAnnotation = conn
                .prepareStatement(statementStr);
        deleteAnnotation.setString(1, label);
        int res = deleteAnnotation.executeUpdate();
        deleteAnnotation.close();
        return res;
    }

    /**
     * Gets the datatype of a given annotation.
     *
     * @param label
     *            annotation label.
     * @return the annotation's datatype (FREETEXT or DROPDOWN).
     *
     * @throws SQLException
     *             if the query does not succeed
     */
    public Integer getAnnotationType(String label)
            throws SQLException {
        Map<String, Integer> annotations = getAnnotations();
        return annotations.get(label);
    }

    /**
     * Adds a drop down annotation to the list of possible
     * annotations.
     *
     * @param label
     *            the name of the annotation.
     * @param choices
     *            the possible values for the annotation.
     * @return the number of tuples inserted into the database.
     * @throws SQLException
     *             if the query does not succeed
     * @throws IOException
     *             if the choices are invalid
     */
    public int addDropDownAnnotation(String label,
            List<String> choices, int defaultValueIndex,
            boolean required) throws SQLException, IOException {

        if (choices.isEmpty()) {
            throw new IOException("Must specify at least one choice");
        }

        if (defaultValueIndex < 0
                || defaultValueIndex >= choices.size()) {
            throw new IOException("Invalid default value index");
        }

        int tuplesInserted = 0;

        String annotationQuery = "INSERT INTO Annotation "
                + "VALUES (?, 'DropDown', ?, ?)";

        String choicesQuery = "INSERT INTO Annotation_Choices "
                + "(Label, Value) VALUES (?, ?)";

        PreparedStatement addAnnotation = conn
                .prepareStatement(annotationQuery);

        addAnnotation.setString(1, label);
        addAnnotation.setString(2, choices.get(defaultValueIndex));
        addAnnotation.setBoolean(3, required);
        tuplesInserted += addAnnotation.executeUpdate();
        addAnnotation.close();

        PreparedStatement addChoices = conn
                .prepareStatement(choicesQuery);
        addChoices.setString(1, label);
        for (String choice : choices) {
            addChoices.setString(2, choice);
            try {
                tuplesInserted += addChoices.executeUpdate();
            } catch (SQLException e) {
                /*
                 * Ignore and try adding next choice. This is probably
                 * due to the list of choices containing a duplicate.
                 */
            }
        }
        addChoices.close();

        return tuplesInserted;

    }

    /**
     * Gets all the choices for a drop down annotation. Deprecated,
     * use {@link #getChoices(String) getChoices} instead.
     *
     * @param label
     *            the drop down annotation to get the choice for.
     * @return the choices.
     * @throws SQLException
     *             if the query does not succeed
     */
    @Deprecated
    public ArrayList<String> getDropDownAnnotations(String label)
            throws SQLException {
        String query = "SELECT Value FROM Annotation_Choices "
                + "WHERE (Label = ?)";
        ArrayList<String> dropDownStrings = new ArrayList<String>();
        PreparedStatement getDropDownStrings = conn
                .prepareStatement(query);
        getDropDownStrings.setString(1, label);
        ResultSet rs = getDropDownStrings.executeQuery();
        while (rs.next()) {
            dropDownStrings.add(rs.getString("Value"));
        }
        getDropDownStrings.close();
        return dropDownStrings;
    }

    /**
     * Gets all the choices for a drop down annotation.
     *
     * @param label
     *            the drop down annotation to get the choice for.
     * @return the choices.
     * @throws SQLException
     *             if the query does not succeed
     */
    public List<String> getChoices(String label) throws SQLException {
        String query = "SELECT Value FROM Annotation_Choices "
                + "WHERE Label = ?";
        List<String> choices = new ArrayList<String>();
        PreparedStatement getChoices = conn.prepareStatement(query);
        getChoices.setString(1, label);
        ResultSet rs = getChoices.executeQuery();
        while (rs.next()) {
            choices.add(rs.getString("Value"));
        }
        getChoices.close();

        return choices;
    }

    /**
     * Adds an experiment ID to the database.
     *
     * @param expID
     *            the ID for the experiment.
     * @return the number of tuples inserted in the database.
     * @throws SQLException
     *             if the query does not succeed
     */
    public int addExperiment(String expID) throws SQLException {
        String query = "INSERT INTO Experiment "
                + "(ExpID) VALUES (?)";
        PreparedStatement addExp = conn.prepareStatement(query);
        addExp.setString(1, expID);

        FilePathGenerator.GenerateExperimentFolders(expID);

        int res = addExp.executeUpdate();
        addExp.close();
        return res;
    }

    /**
     * Checks if a given experiment ID exists in the database.
     *
     * @param expID
     *            the experiment ID to look for.
     * @return true if the experiment exists in the database, else
     *         false.
     * @throws SQLException
     *             if the query does not succeed
     */
    public boolean hasExperiment(String expID) throws SQLException {
        String query = "SELECT ExpID FROM Experiment "
                + "WHERE ExpID = ?";
        PreparedStatement hasExp = conn.prepareStatement(query);
        hasExp.setString(1, expID);
        ResultSet rs = hasExp.executeQuery();

        boolean res = rs.next();
        hasExp.close();
        return res;
    }

    /**
     * Deletes an experiment from the database.
     *
     * @param expId
     *            the experiment ID.
     * @return the number of tuples deleted.
     * @throws SQLException
     *             if the query does not succeed. Occurs if Experiment
     *             contains at least one file. (All files relating to
     *             an experiment must be deleted first before an
     *             experiment can be deleted from the database)
     */
    public int deleteExperiment(String expId) throws SQLException {
        String statementStr = "DELETE FROM Experiment "
                + "WHERE (ExpID = ?)";
        PreparedStatement deleteExperiment = conn
                .prepareStatement(statementStr);
        deleteExperiment.setString(1, expId);
        int res = deleteExperiment.executeUpdate();
        deleteExperiment.close();
        return res;

    }

    /**
     * Annotates an experiment with the given label and value. Checks
     * so that the value is valid if it is a drop down annotation.
     *
     * @param expID
     *            the name of the experiment to annotate.
     * @param label
     *            the annotation to set.
     * @param value
     *            the value of the annotation.
     * @return the number of tuples updated in the database.
     * @throws SQLException
     *             if the query does not succeed
     * @throws IOException
     *             if the value is invalid for the annotation type.
     */
    public int annotateExperiment(String expID, String label,
            String value) throws SQLException, IOException {

        if (!isValidAnnotationValue(label, value)) {
            throw new IOException(
                    value
                            + " is not a valid choice for the annotation type "
                            + label);
        }

        String query = "INSERT INTO Annotated_With "
                + "VALUES (?, ?, ?)";
        PreparedStatement tagExp = conn.prepareStatement(query);
        tagExp.setString(1, expID);
        tagExp.setString(2, label);
        tagExp.setString(3, value);

        int res = tagExp.executeUpdate();
        tagExp.close();
        return res;
    }

    /**
     * Checks so that the annotation value is valid.
     *
     * @param label
     *            the annotation name.
     * @param value
     *            the value to be evaluated.
     * @return true if the value is valid, else false.
     * @throws SQLException
     *             if the query does not succeed
     */
    private boolean isValidAnnotationValue(String label, String value)
            throws SQLException {
        return getAnnotationType(label) == Annotation.FREETEXT
                || getChoices(label).contains(value);
    }

    /**
     * Deletes an annotation from an experiment.
     *
     * @param expID
     *            the experiment to delete the annotation from.
     * @param label
     *            the name of the annotation.
     * @return the number of tuples deleted from the database.
     * @throws SQLException
     *             if the query does not succeed
     */
    public int removeExperimentAnnotation(String expID, String label)
            throws SQLException {
        String statementStr = "DELETE FROM Annotated_With "
                + "WHERE (ExpID = ? AND Label = ?)";
        PreparedStatement deleteTag = conn
                .prepareStatement(statementStr);
        deleteTag.setString(1, expID);
        deleteTag.setString(2, label);
        int res = deleteTag.executeUpdate();
        deleteTag.close();
        return res;
    }

	/**
	 * Checks if a given annotation is required to be filled by the user.
	 * @param annotationLabel the name of the annotation to check
	 * @return true if it is required, else false
	 * @throws SQLException
	 */
	public boolean isRequierd(String annotationLabel) throws SQLException {

		String query = "SELECT Required FROM Annotation WHERE Label = ?";

		PreparedStatement ps = conn.prepareStatement(query);

		ps.setString(1, annotationLabel);

		ResultSet rs = ps.executeQuery();

		boolean isRequired = false;



		while(rs.next()){
			isRequired =  rs.getBoolean("Required");
		}
		return isRequired;
	}

	/**
	 * Gets the default value for a annotation if there is one, If not
	 * it returns NULL.
	 * @param annotationLabel the name of the annotation to check
	 * @return The defult value or NULL.
	 * @throws SQLException
	 */
	public String getDefaultValue(String annotationLabel) throws SQLException {

		String query = "SELECT DefaultValue FROM Annotation WHERE Label = ?";

		PreparedStatement ps = conn.prepareStatement(query);

		ps.setString(1, annotationLabel);

		ResultSet rs = ps.executeQuery();

		while(rs.next()){
			return rs.getString("DefaultValue");
		}

		return null;
	}



    // Too many parameters. Should take a JSONObject or FileTuple
    // instead.
    /**
     * Adds a file to the database.
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
    public String addFile(String fileType, String fileName,
            String metaData, String author, String uploader,
            boolean isPrivate, String expID, String grVersion)
            throws SQLException {

        String path = FilePathGenerator.GenerateFilePath(expID,
                fileType, fileName);

        String query = "INSERT INTO File "
                + "(Path, FileType, FileName, Date, MetaData, Author, Uploader, IsPrivate, ExpID, GRVersion) VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?)";
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
        return path;
    }

    /**
     * Adds a file to the database with URL.
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
    public String addFileURL(String fileType, String fileName,
            String metaData, String author, String uploader,
            boolean isPrivate, String expID, String grVersion)
            throws SQLException {

        String path = FilePathGenerator.GenerateFilePath(expID,
                fileType, fileName);
        String URL = ServerDependentValues.UploadURL;

        String query = "INSERT INTO File "
                + "(Path, FileType, FileName, Date, MetaData, Author, Uploader, IsPrivate, ExpID, GRVersion) VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?)";
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
        String statementStr = "DELETE FROM File "
                + "WHERE (Path = ?)";
        PreparedStatement deleteFile = conn
                .prepareStatement(statementStr);
        deleteFile.setString(1, path);
        int res = deleteFile.executeUpdate();
        deleteFile.close();
        return res;
    }


	public ArrayList<String> process(String fileID, String fileType, String fileName,
			String metaData, String uploader, String grVersion, String expID)
					throws SQLException {

		ArrayList<String> pathList = new ArrayList<String>();

		String ToPath;

        String SelectQuery = "SELECT Path, Author, IsPrivate FROM File WHERE (FileID = ?)";
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
        }else {
        	throw new SQLException("Not a valid fileID");
        }

        ToPath = addFile(fileType, fileName, metaData, author, uploader, isPrivate, expID, grVersion);
        ps.close();

        pathList.add(fromPath);
        pathList.add(ToPath);

        return pathList;
    }


    /**
     * Gets an experiment from the database.
     *
     * @param expID
     *            the ID of the experiment.
     * @return an Experiment object.
     * @throws SQLException
     *             if the query does not succeed
     */
    public Experiment getExperiment(String expID) throws SQLException {
        String query = "SELECT ExpID FROM Experiment "
                + "WHERE ExpID = ?";
        PreparedStatement getExp = conn.prepareStatement(query);
        getExp.setString(1, expID);
        ResultSet rs = getExp.executeQuery();

        Experiment e = null;
        if (rs.next()) {
            e = new Experiment(rs.getString("ExpID"));
            e = fillAnnotations(e);
            e = fillFiles(e);
        }
        getExp.close();

        return e;
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
     * Adds all the files that belong to the experiment to an
     * Experiment object.
     *
     * @param e
     *            the experiment to add files to.
     * @return the Experiment object containing all its files.
     * @throws SQLException
     *             if the query does not succeed
     */
    private Experiment fillFiles(Experiment e) throws SQLException {
        String query = "SELECT * FROM File " + "WHERE ExpID = ?";
        PreparedStatement getFiles = conn.prepareStatement(query);
        getFiles.setString(1, e.getID());
        ResultSet rs = getFiles.executeQuery();

        while (rs.next()) {
            e.addFile(new FileTuple(rs));
        }
        getFiles.close();
        return e;
    }

    /**
     * Fill an Experiment object with all annotations that exists for
     * that experiment.
     *
     * @param e
     *            the Experiment object.
     * @return the Experiment object containing all it's annotations.
     * @throws SQLException
     *             if the query does not succeed
     */
    private Experiment fillAnnotations(Experiment e)
            throws SQLException {
        String query = "SELECT Label, Value FROM Annotated_With "
                + "WHERE ExpID = ?";
        PreparedStatement getExpAnnotations = conn
                .prepareStatement(query);
        getExpAnnotations.setString(1, e.getID());
        ResultSet rs = getExpAnnotations.executeQuery();

        while (rs.next()) {
            e.addAnnotation(rs.getString("Label"),
                    rs.getString("Value"));
        }
        getExpAnnotations.close();
        return e;
    }

    /**
     * Searches the database for Experiments. The search criteria are
     * specified in a String that has the same format as that used by
     * PubMed:
     *
     * <Value>[<Label>] <AND|OR> <Value>[<Label>] ...
     *
     * Round brackets should be used to disambiguate the logical
     * expression.
     *
     * Example:
     *
     * "(Human[Species] OR Fly[Species]) AND Joe Bloggs[Uploader]"
     *
     * @param pubMedString
     *            The String containing the search criteria in PubMed
     *            format.
     * @return A List of experiments containing file that fullfill the
     *         criteria specifies in the pubMedString.
     * @throws IOException
     *             If the pubMedString is not in the right format
     * @throws SQLException
     *             if the query does not succeed
     */
    public List<Experiment> search(String pubMedString)
            throws IOException, SQLException {

        if (pm2sql.hasFileConstraint(pubMedString)) {
            return searchFiles(pubMedString);
        }
        return searchExperiments(pubMedString);
    }

    private List<Experiment> searchExperiments(String pubMedString)
            throws IOException, SQLException {

        String query = pm2sql.convertExperimentSearch(pubMedString);

        List<String> params = pm2sql.getParameters();

        PreparedStatement getFiles = conn.prepareStatement(query);
        getFiles = bind(getFiles, params);

        ResultSet rs = getFiles.executeQuery();

        ArrayList<Experiment> experiments = new ArrayList<Experiment>();

        while (rs.next()) {
            Experiment exp = new Experiment(rs.getString("ExpID"));
            exp = fillAnnotations(exp);
            exp = fillFiles(exp);
            experiments.add(exp);
        }
        return experiments;
    }

    private List<Experiment> searchFiles(String pubMedString)
            throws IOException, SQLException {

        String query = pm2sql.convertFileSearch(pubMedString);

        List<String> params = pm2sql.getParameters();

        PreparedStatement getFiles = conn.prepareStatement(query);
        getFiles = bind(getFiles, params);

        ResultSet rs = getFiles.executeQuery();

        ArrayList<Experiment> experiments = new ArrayList<Experiment>();

        if (!rs.next()) {
            return experiments;
        }

        String expId = rs.getString("ExpId");
        Experiment exp = new Experiment(expId);
        exp = fillAnnotations(exp);
        exp.addFile(new FileTuple(rs));

        while (rs.next()) {
            expId = rs.getString("ExpId");

            if (exp.getID().equals(expId)) {
                exp.addFile(new FileTuple(rs));
            } else {
                experiments.add(exp);
                exp = new Experiment(expId);
                exp = fillAnnotations(exp);
                exp.addFile(new FileTuple(rs));
            }
        }

        experiments.add(exp);

        return experiments;
    }

    private PreparedStatement bind(PreparedStatement query,
            List<String> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            query.setString(i + 1, params.get(i));
        }
        return query;
    }

    /**
     * Finds all annotationLabels that exist in the database, example of labels:
     * sex, tissue, etc...
     * @return ArrayList<String> annotationLabels
     */
    public ArrayList<String> getAllAnnotationLabels(){
    	ArrayList<String> allAnnotationlabels = new ArrayList<>();

    	String findAllLabelsQuery = "SELECT Label FROM Annotation";
    	PreparedStatement ps;

    	try {
			ps = conn.prepareStatement(findAllLabelsQuery);
			ResultSet res = ps.executeQuery();
			while(res.next()){
				allAnnotationlabels.add(res.getString("Label"));
			}
			res.close();
    	} catch (SQLException e) {
			e.printStackTrace();
    		return null;
		}
    	return allAnnotationlabels;
    }


    /**
     * Changes the annotation Label value.
     * @param String oldLabel
     * @param string newLabel
     * @return boolean true if changed succeeded,
     * 				   false if it failed.
     */
    public boolean changeAnnotationLabel(String oldLabel, String newLabel){

    	String changeLblQuery = "UPDATE Annotation SET Label = ? WHERE (Label =?)";

    	PreparedStatement lblExp;

		try {
			lblExp = conn.prepareStatement(changeLblQuery);

			lblExp.setString(1, newLabel);
			lblExp.setString(2, oldLabel);
			lblExp.execute();
			return true;

		} catch (SQLException e) {

			System.out.println("Failed to Create changeLabel query");
			return false;
		}
    }

    /**
     * Method for getting all stored versions of genome Releases in the
     * database
     * @return ArrayList<String> genome versions.
     * @throws SQLException
     */
    public ArrayList<String> getStoredGenomeVersions() throws SQLException{

    	ArrayList<String> allVersions = new ArrayList<String>();

    	String getGenVerQuery = "SELECT * FROM Genome_Release";
    	PreparedStatement ps = conn.prepareStatement(getGenVerQuery);

    	ResultSet res = ps.executeQuery();

    	while(res.next()){
    		allVersions.add(res.getString("Version"));
    	}
    	return allVersions;
    }

    /**
     * Add one genomerelease to the database.
     *
     * @param String genomeVersion.
     * @param String specie.
     * @return String filePath, the path where the genome Version file should
     * be saved.
     * @throws SQLException
     */
    public String addGenomeRelease(String genomeVersion, String specie)
    	   throws SQLException{

    	String filePath = "";

    	String path = FilePathGenerator.GeneratePathForGenomeFiles(
    										genomeVersion, specie);

    	String insertGenRelQuery = "INSERT INTO Genome_Release " +
    							   "(Version,Species,FilePath) " +
    							   "VALUES (?,?,?)";

    	PreparedStatement ps = conn.prepareStatement(insertGenRelQuery);

    	ps.setString(1, genomeVersion);
    	ps.setString(2, specie);
    	ps.setString(3, path);

    	ps.execute();
    	return filePath;
    }

    /**
     * Removes one genome version stored in the database.
     * @param version, the genome version.
     * @param specie.
     * @return boolean, true if succeded, false if failed.
     */
    public boolean removeGenomeRelease(String genomeVersion, String specie){

    	String removeQuery = "DELETE FROM Genome_Release WHERE (Version = ? AND Species = ?)";

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

    /** Creates an Annotation object from an annotation label.
     *
     *  @param label the name of the annotation to create the object for.
     *  @return the Annotation object. If the label does not exist, then null
     *  will be returned.
     *  @throws SQLException if the query does not succeed.
     */
     public Annotation getAnnotationObject(String label) throws SQLException {

    	 String query = "SELECT * FROM Annotation "
    			 		+ "LEFT JOIN Annotation_Choices "
    			 		+ "ON (Annotation.Label = Annotation_Choices.Label) "
    			 		+ "WHERE Annotation.Label = ?";

    	 PreparedStatement stmt = conn.prepareStatement(query);
    	 stmt.setString(1, label); ResultSet rs = stmt.executeQuery();

    	 if(rs.next()) {
    		 return new Annotation(rs);
    	 }
    	 else {
    		 return null;
    	 }
    }

    /*
     * Changes the value of an annotation corresponding to it's label.
     * Parameters: label of annotation, the old value and the new value to
     * change to. Throws an SQLException if the new value already exists in the
     * choices table (changing all males to female, and female is already in the
     * table)
     *
     * @param String label
     * @param String oldValue
     * @param String newValue
     * @throws SQLException
     */
	public void changeAnnotationValue(String label, String oldValue,
			String newValue) throws SQLException {


    	String query = "UPDATE Annotation_Choices " +
    			"SET Value = ? " +
    			"WHERE Label = ? and Value = ?";

    	String query2 = "UPDATE Annotated_With "
    			+ "SET Value = ? "
                + "WHERE Label = ? and Value = ?";

    	String query3 = "UPDATE Annotation " +
    			"SET DefaultValue = ? " +
    			"WHERE Label = ? and DefaultValue = ?";

    	PreparedStatement statement = conn
                .prepareStatement(query);
    	ArrayList<String> parameters = new ArrayList<String>();
    	parameters.add(newValue);
    	parameters.add(label);
    	parameters.add(oldValue);
    	statement = bind(statement, parameters);
    	statement.executeUpdate();
    	statement.close();

    	statement = conn.prepareStatement(query2);
    	statement = bind(statement, parameters);
    	statement.executeUpdate();
    	statement.close();

    	statement = conn.prepareStatement(query3);
    	statement = bind(statement, parameters);
    	statement.executeUpdate();
    	statement.close();
	}
}
