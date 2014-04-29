package databaseAccessor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;


public class DatabaseAccessor {

    Connection conn;

    public DatabaseAccessor(String username, String password, String host, String database) throws SQLException {

        String url = "jdbc:postgresql://" + host + "/" + database;
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);


        conn = DriverManager.getConnection(url, props);

    }

    public boolean isConnected() {
        return conn != null;
    }

    public void addUser(String username, String password, String role) throws SQLException {
        String userString = "INSERT INTO User_Info " +
                "(Username, Password, Role) VALUES " +
                "(?, ?, ?)";
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

        String statementStr = "DELETE FROM User_Info " +
                "WHERE (Username = ?)";
        PreparedStatement deleteUser = conn.prepareStatement(statementStr);
        deleteUser.setString(1, username);
        deleteUser.executeUpdate();
    }

    public void close() throws SQLException {
        conn.close();
    }

    public String getPassword(String testUser) throws SQLException {
        String query = "SELECT Password FROM User_Info " +
        		"WHERE (Username = ?)";
        PreparedStatement getPassword = conn.prepareStatement(query);
        getPassword.setString(1, testUser);
        ResultSet rs = getPassword.executeQuery();
        if(rs.next()) {
            return rs.getString("password");
        }
        return null;
    }

    public int resetPassword(String username, String newPassword) throws SQLException {
        String query = "UPDATE User_Info SET Password = ? " +
        		"WHERE (Username = ?)";
        PreparedStatement resetPassword = conn.prepareStatement(query);
        resetPassword.setString(1, newPassword);
        resetPassword.setString(2, username);
        return resetPassword.executeUpdate();

    }

    public int setRole(String username, String role) throws SQLException {
        String query = "UPDATE User_Info SET Role = ? " +
                "WHERE (Username = ?)";
        PreparedStatement setRole = conn.prepareStatement(query);
        setRole.setString(1, role);
        setRole.setString(2, username);
        return setRole.executeUpdate();
    }

    public String getRole(String username) throws SQLException {
        String query = "SELECT Role FROM User_Info " +
                "WHERE (Username = ?)";
        PreparedStatement getRole = conn.prepareStatement(query);
        getRole.setString(1, username);
        ResultSet rs = getRole.executeQuery();
        if(rs.next()) {
            return rs.getString("Role");
        }
        return null;
    }

}
