package databaseAccessor;

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

import database.PubMedToSQLConverter;
//may be changed
import database.SearchResult;
import database.ParsedPubMed;
import database.PubMedParser;

public class DatabaseAccessor {

    private Connection conn;
    private PubMedToSQLConverter pm2sql;

    String username = "c5dv151_vt14";
    String password = "shielohh";
    String host = "postgres";
    String database = "c5dv151_vt14";

    public static Integer FREETEXT = 1;
    public static Integer DROPDOWN = 2;

    /**
     * Creates a databaseAccessor that opens a connection to a database.
     *
     * @param username
     *            - The username to log in to the database as. Should be
     *            "c5dv151_vt14" as of now.
     * @param password
     *            - The password to log in to the database. Should be "shielohh"
     *            as of now.
     * @param host
     *            - The name of the database management system. Will problebly
     *            always be "postgres" unless the DMS is switched with something
     *            else.
     * @param database
     * @throws SQLException
     * @throws IOException
     */
    public DatabaseAccessor(String username, String password, String host,
            String database) throws SQLException {

        String url = "jdbc:postgresql://" + host + "/" + database;
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        conn = DriverManager.getConnection(url, props);

        pm2sql = new PubMedToSQLConverter();

    }

    /**
     * Public method to check if the instance of the class is connected to a
     * database.
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
     *            , the username
     * @param String
     *            , the password
     * @param String
     *            , therole
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

    public ArrayList<String> getUsers() throws SQLException {
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

    public void deleteUser(String username) throws SQLException {

        String statementStr = "DELETE FROM User_Info " + "WHERE (Username = ?)";
        PreparedStatement deleteUser = conn.prepareStatement(statementStr);
        deleteUser.setString(1, username);
        deleteUser.executeUpdate();
        deleteUser.close();
    }

    public void close() throws SQLException {
        conn.close();
    }

    /**
     * Returns the password for the given user. Used for login.
     *
     * @param user
     *            - the username as stirng
     * @return String - the password
     * @throws SQLException
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

    public int resetPassword(String username, String newPassword)
            throws SQLException {
        String query = "UPDATE User_Info SET Password = ? "
                + "WHERE (Username = ?)";
        PreparedStatement resetPassword = conn.prepareStatement(query);
        resetPassword.setString(1, newPassword);
        resetPassword.setString(2, username);
        int res = resetPassword.executeUpdate();
        resetPassword.close();
        return res;

    }

    public int setRole(String username, String role) throws SQLException {
        String query = "UPDATE User_Info SET Role = ? "
                + "WHERE (Username = ?)";
        PreparedStatement setRole = conn.prepareStatement(query);
        setRole.setString(1, role);
        setRole.setString(2, username);
        int res = setRole.executeUpdate();
        setRole.close();
        return res;
    }

    public String getRole(String username) throws SQLException {
        String query = "SELECT Role FROM User_Info " + "WHERE (Username = ?)";
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

    public int addFreeTextAnnotation(String label) throws SQLException {
        String query = "INSERT INTO Annotation "
                + "(Label, DataType) VALUES (?, 'FreeText')";
        PreparedStatement addAnnotation = conn.prepareStatement(query);
        addAnnotation.setString(1, label);
        int res = addAnnotation.executeUpdate();
        addAnnotation.close();
        return res;
    }

    public Map<String, Integer> getAnnotations() throws SQLException {
        HashMap<String, Integer> annotations = new HashMap<String, Integer>();
        String query = "SELECT * FROM Annotation";
        Statement getAnnotations = conn.createStatement();
        ResultSet rs = getAnnotations.executeQuery(query);
        while (rs.next()) {
            if (rs.getString("DataType").equalsIgnoreCase("FreeText")) {
                annotations.put(rs.getString("Label"), FREETEXT);
            } else {
                annotations.put(rs.getString("Label"), DROPDOWN);
            }
        }
        getAnnotations.close();

        return annotations;
    }

    public int deleteAnnotation(String label) throws SQLException {

        String statementStr = "DELETE FROM Annotation " + "WHERE (Label = ?)";
        PreparedStatement deleteAnnotation = conn
                .prepareStatement(statementStr);
        deleteAnnotation.setString(1, label);
        int res = deleteAnnotation.executeUpdate();
        deleteAnnotation.close();
        return res;
    }

    public Integer getAnnotationType(String label) throws SQLException {
        Map<String, Integer> annotations = getAnnotations();
        return annotations.get(label);
    }

    public int addDropDownAnnotation(String label, ArrayList<String> choices)
            throws SQLException, IOException {

        if (choices.isEmpty()) {
            throw new IOException("Must specify at least one choice");
        }

        int tuplesInserted = 0;

        String annotationQuery = "INSERT INTO Annotation "
                + "(Label, DataType) VALUES (?, 'DropDown')";
        String choicesQuery = "INSERT INTO Annotation_Choices "
                + "(Label, Value) VALUES (?, ?)";
        PreparedStatement addAnnotation = null;
        PreparedStatement addChoices = null;

        addAnnotation = conn.prepareStatement(annotationQuery);
        addChoices = conn.prepareStatement(choicesQuery);
        addAnnotation.setString(1, label);
        tuplesInserted += addAnnotation.executeUpdate();

        addChoices.setString(1, label);
        for (String choice : choices) {
            addChoices.setString(2, choice);
            tuplesInserted += addChoices.executeUpdate();
        }
        addChoices.close();

        return tuplesInserted;

    }

    public ArrayList<String> getDropDownAnnotations(String label)
            throws SQLException {
        String query = "SELECT Value FROM Annotation_Choices "
                + "WHERE (Label = ?)";
        ArrayList<String> dropDownStrings = new ArrayList<String>();
        PreparedStatement getDropDownStrings = conn.prepareStatement(query);
        getDropDownStrings.setString(1, label);
        ResultSet rs = getDropDownStrings.executeQuery();
        while (rs.next()) {
            dropDownStrings.add(rs.getString("Value"));
        }
        getDropDownStrings.close();
        return dropDownStrings;
    }

    // KOLLA! Ska den kasta ett undantag?
    // Skriv fler tester!
    /**
     *
     * @param searchPubMed
     * @return null if failed, else a SearchResult object.
     */
    public SearchResult searchExperiment(String searchPubMed) {

        PreparedStatement pStatement;

        String query = "SELECT * FROM File NATURAL JOIN Annotated_With "
                + "WHERE (";

        // getting the where-statements from pubmed string to usable
        // query.
        PubMedParser theParser = new PubMedParser();
        ParsedPubMed queryMaterial = theParser.parsePubMed(searchPubMed);

        query = query + queryMaterial.getWhereString() + ")";

        try {
            pStatement = conn.prepareStatement(query);
            for (int i = 1; i < queryMaterial.getValues().size(); i++) {
                pStatement.setString(i, queryMaterial.getValues().get(i - 1));
            }

            ResultSet res = pStatement.executeQuery();

            SearchResult queryRes = new SearchResult(res);
            return queryRes;

        } catch (SQLException e) {
            System.out.println("Failed to send query to database\n");
        }
        return null;
    }

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

    public int addExperiment(String expID) throws SQLException {
        String query = "INSERT INTO Experiment " + "(ExpID) VALUES (?)";
        PreparedStatement addExp = conn.prepareStatement(query);
        addExp.setString(1, expID);

        int res = addExp.executeUpdate();
        addExp.close();
        return res;
    }

    public boolean hasExperiment(String expID) throws SQLException {
        String query = "SELECT ExpID FROM Experiment " + "WHERE ExpID = ?";
        PreparedStatement hasExp = conn.prepareStatement(query);
        hasExp.setString(1, expID);
        ResultSet rs = hasExp.executeQuery();

        boolean res = rs.next();
        hasExp.close();
        return res;
    }

    public int deleteExperiment(String expId) throws SQLException {
        String statementStr = "DELETE FROM Experiment " + "WHERE (ExpID = ?)";
        PreparedStatement deleteExperiment = conn
                .prepareStatement(statementStr);
        deleteExperiment.setString(1, expId);
        int res = deleteExperiment.executeUpdate();
        deleteExperiment.close();
        return res;

    }

    public int tagExperiment(String expID, String label, String value)
            throws SQLException, IOException {

        if (!isValidAnnotationValue(label, value)) {
            throw new IOException(value
                    + " is not a valid choice for the annotation type " + label);
        }

        String query = "INSERT INTO Annotated_With " + "VALUES (?, ?, ?)";
        PreparedStatement tagExp = conn.prepareStatement(query);
        tagExp.setString(1, expID);
        tagExp.setString(2, label);
        tagExp.setString(3, value);

        int res = tagExp.executeUpdate();
        tagExp.close();
        return res;
    }

    private boolean isValidAnnotationValue(String label, String value)
            throws SQLException {
        return getAnnotationType(label) == FREETEXT
                || getChoices(label).contains(value);
    }

    public int deleteTag(String expID, String label) throws SQLException {
        String statementStr = "DELETE FROM Annotated_With "
                + "WHERE (ExpID = ? AND Label = ?)";
        PreparedStatement deleteTag = conn.prepareStatement(statementStr);
        deleteTag.setString(1, expID);
        deleteTag.setString(2, label);
        int res = deleteTag.executeUpdate();
        deleteTag.close();
        return res;
    }

    // Too many parameters. Could take a JSONObject instead.
    public int addFile(String path, String type, String metaData,
            String author, String uploader, boolean isPrivate, String expID,
            String grVersion) throws SQLException {

        String query = "INSERT INTO File "
                + "(Path, FileType, Date, MetaData, Author, Uploader, IsPrivate, ExpID, GRVersion) VALUES (?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?)";
        PreparedStatement tagExp = conn.prepareStatement(query);

        tagExp.setString(1, path);
        tagExp.setString(2, type);
        tagExp.setString(3, metaData);
        tagExp.setString(4, author);
        tagExp.setString(5, uploader);
        tagExp.setBoolean(6, isPrivate);
        tagExp.setString(7, expID);
        tagExp.setString(8, grVersion);

        int res = tagExp.executeUpdate();
        tagExp.close();
        return res;
    }

    public int deleteFile(String path) throws SQLException {
        String statementStr = "DELETE FROM File " + "WHERE (Path = ?)";
        PreparedStatement deleteFile = conn.prepareStatement(statementStr);
        deleteFile.setString(1, path);
        int res = deleteFile.executeUpdate();
        deleteFile.close();
        return res;
    }

    public Experiment getExperiment(String expID) throws SQLException {
        String query = "SELECT ExpID FROM Experiment " + "WHERE ExpID = ?";
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

    private Experiment fillAnnotations(Experiment e) throws SQLException {
        String query = "SELECT Label, Value FROM Annotated_With "
                + "WHERE ExpID = ?";
        PreparedStatement getExpAnnotations = conn.prepareStatement(query);
        getExpAnnotations.setString(1, e.getID());
        ResultSet rs = getExpAnnotations.executeQuery();

        while (rs.next()) {
            e.addAnnotation(rs.getString("Label"), rs.getString("Value"));
        }
        getExpAnnotations.close();
        return e;
    }

    public List<Experiment> search(String pubMedString) throws IOException,
            SQLException {
        String query = pm2sql.convert(pubMedString);

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

    private PreparedStatement bind(PreparedStatement query, List<String> params)
            throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            query.setString(i + 1, params.get(i));
        }
        return query;
    }

}
