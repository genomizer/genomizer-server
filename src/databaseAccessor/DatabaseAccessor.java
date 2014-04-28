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
        String userString = "INSERT INTO UserInfo " +
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
        String query = "SELECT Username FROM UserInfo";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            users.add(rs.getString("Username"));
        }
        return users;
    }

    public void deleteUser(String username) throws SQLException {
        
        String statementStr = "DELETE FROM UserInfo " +
                "WHERE (Username = ?)";
        PreparedStatement deleteUser = conn.prepareStatement(statementStr);
        deleteUser.setString(1, username);
        deleteUser.executeUpdate();
    }

    public void close() throws SQLException {
        conn.close();
    }

}
