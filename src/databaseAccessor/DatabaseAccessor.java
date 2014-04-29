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
import java.util.Map;
import java.util.Properties;

//may be changed
import database.SearchResult;
import database.ParsedPubMed;
import database.PubMedParser;

public class DatabaseAccessor {

    Connection conn;

    public DatabaseAccessor(String username, String password, String host,
            String database) throws SQLException {

        String url = "jdbc:postgresql://" + host + "/" + database;
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        conn = DriverManager.getConnection(url, props);

    }

    public boolean isConnected() {
        return conn != null;
    }

    public void addUser(String username, String password, String role)
            throws SQLException {
        String userString = "INSERT INTO User_Info "
                + "(Username, Password, Role) VALUES " + "(?, ?, ?)";
        PreparedStatement addUser = conn.prepareStatement(userString);
        addUser.setString(1, username);
        addUser.setString(2, password);
        addUser.setString(3, role);

        addUser.executeUpdate();
    }

    public ArrayList<String> getUsers() throws SQLException {
        ArrayList<String> users = new ArrayList<String>();
        String query = "SELECT Username FROM User_Info";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            users.add(rs.getString("Username"));
        }
        return users;
    }

    public void deleteUser(String username) throws SQLException {

        String statementStr = "DELETE FROM User_Info " + "WHERE (Username = ?)";
        PreparedStatement deleteUser = conn.prepareStatement(statementStr);
        deleteUser.setString(1, username);
        deleteUser.executeUpdate();
    }

    public void close() throws SQLException {
        conn.close();
    }

    public String getPassword(String testUser) throws SQLException {
        String query = "SELECT Password FROM User_Info "
                + "WHERE (Username = ?)";
        PreparedStatement getPassword = conn.prepareStatement(query);
        getPassword.setString(1, testUser);
        ResultSet rs = getPassword.executeQuery();
        if (rs.next()) {// TODO Auto-generated method stub
            return rs.getString("password");
        }
        return null;
    }

    public int resetPassword(String username, String newPassword)
            throws SQLException {
        String query = "UPDATE User_Info SET Password = ? "
                + "WHERE (Username = ?)";
        PreparedStatement resetPassword = conn.prepareStatement(query);
        resetPassword.setString(1, newPassword);
        resetPassword.setString(2, username);
        return resetPassword.executeUpdate();

    }

    public int setRole(String username, String role) throws SQLException {
        String query = "UPDATE User_Info SET Role = ? "
                + "WHERE (Username = ?)";
        PreparedStatement setRole = conn.prepareStatement(query);
        setRole.setString(1, role);
        setRole.setString(2, username);
        return setRole.executeUpdate();
    }

    public String getRole(String username) throws SQLException {
        String query = "SELECT Role FROM User_Info " + "WHERE (Username = ?)";
        PreparedStatement getRole = conn.prepareStatement(query);
        getRole.setString(1, username);
        ResultSet rs = getRole.executeQuery();
        if (rs.next()) {
            return rs.getString("Role");
        }
        return null;
    }

    public int addFreeTextAnnotation(String label) throws SQLException {
        String query = "INSERT INTO Annotation "
                + "(Label, DataType) VALUES (?, 'FreeText')";
        PreparedStatement addAnnotation = conn.prepareStatement(query);
        addAnnotation.setString(1, label);
        return addAnnotation.executeUpdate();

    }

    public Map<String, String> getAnnotations() throws SQLException {
        HashMap<String, String> annotations = new HashMap<String, String>();
        String query = "SELECT * FROM Annotation";
        Statement getAnnotations = conn.createStatement();
        ResultSet rs = getAnnotations.executeQuery(query);
        while (rs.next()) {
            annotations.put(rs.getString("Label"), rs.getString("DataType"));
        }

        return annotations;
    }

    public int deleteAnnotation(String label) throws SQLException {

        if (getAnnotationType(label) == null) {
            return 0;
        }

        if (getAnnotationType(label).equals("DropDown")) {
            removeChoices(label);
        }

        String statementStr = "DELETE FROM Annotation " + "WHERE (Label = ?)";
        PreparedStatement deleteAnnotation = conn
                .prepareStatement(statementStr);
        deleteAnnotation.setString(1, label);
        return deleteAnnotation.executeUpdate();
    }

    private int removeChoices(String label) throws SQLException {
        String statementStr = "DELETE FROM Annotation_Choices " + "WHERE (Label = ?)";
        PreparedStatement deleteAnnotation = conn.prepareStatement(statementStr);
        deleteAnnotation.setString(1, label);
        return deleteAnnotation.executeUpdate();
    }

    private String getAnnotationType(String label) throws SQLException {
        Map<String, String> annotations = getAnnotations();
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

        return tuplesInserted;

    }

    public ArrayList<String> getDropDownAnnotations(String label) throws SQLException {
        String query = "SELECT Value FROM Annotation_Choices " +
        		"WHERE (Label = ?)";
        ArrayList<String> dropDownStrings = new ArrayList<String>();
        PreparedStatement getDropDownStrings = conn.prepareStatement(query);
        getDropDownStrings.setString(1, label);
        ResultSet rs = getDropDownStrings.executeQuery();
        while (rs.next()) {
            dropDownStrings.add(rs.getString("Value"));
        }
        return dropDownStrings;
    }




    //NMP
    public SearchResult searchExperiment(String searchPubMed){

    	PreparedStatement pStatement;

    	String query = "SELECT * FROM File NATURAL JOIN Annotated_With " +
					   "WHERE (";

		//getting the where-statements from pubmed string to usable query.
		PubMedParser theParser = new PubMedParser();
		ParsedPubMed queryMaterial = theParser.parsePubMed(searchPubMed);

		query = query + queryMaterial.getWhereString() + ")";

System.out.println("asdasd: " + query);

		try {
			pStatement = conn.prepareStatement(query);
			for(int i = 0;i < queryMaterial.getValues().size();i++){
				pStatement.setString(i+1, queryMaterial.getValues().get(i));
			}

			ResultSet res = pStatement.executeQuery();

			SearchResult queryRes = new SearchResult(res);

			return queryRes;

		} catch (SQLException e) {
			System.out.println("Failed to send query to database\n");
		}
		return null;
    }

}
