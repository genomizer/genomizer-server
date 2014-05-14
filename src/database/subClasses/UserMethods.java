package database.subClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserMethods {

	private Connection conn;


	public UserMethods(Connection connection){

		conn = connection;
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

        PreparedStatement resetPassword = conn.prepareStatement(query);
        resetPassword.setString(1, newPassword);
        resetPassword.setString(2, username);
        int res = resetPassword.executeUpdate();
        resetPassword.close();

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

}
