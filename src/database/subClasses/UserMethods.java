package database.subClasses;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserMethods {

	private Connection conn;

	public UserMethods(Connection connection) {

		conn = connection;
	}

	/**
	 * Returns an ArrayList which contains the usernames of all the users in the
	 * database in the form of strings.
	 *
	 * @return an ArrayList of usernames.
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public List<String> getUsers() throws SQLException {

		ArrayList<String> usersList = new ArrayList<String>();
		String query = "SELECT Username FROM User_Info";

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			usersList.add(rs.getString("Username"));
		}

		stmt.close();

		return usersList;
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
	 * @throws IOException if an argument string is empty
	 */
	public void addUser(String username, String password, String role,
			String fullName, String email) throws SQLException, IOException {

		isValidArgument(username);

		String query = "INSERT INTO User_Info (Username, Password, Role) "
				+ "VALUES " + "(?, ?, ?)";

		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, username);
		stmt.setString(2, password);
		stmt.setString(3, role);
		stmt.executeUpdate();
		stmt.close();
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

		String query = "DELETE FROM User_Info " + "WHERE (Username = ?)";

		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, username);
		stmt.executeUpdate();
		stmt.close();
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

		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, user);
		ResultSet rs = stmt.executeQuery();
		String pass = null;

		if (rs.next()) {
			pass = rs.getString("password");
		}

		stmt.close();

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

		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, newPassword);
		stmt.setString(2, username);
		int resCount = stmt.executeUpdate();
		stmt.close();

		return resCount;
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

		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, username);
		ResultSet rs = stmt.executeQuery();
		String role = null;

		if (rs.next()) {
			role = rs.getString("Role");
		}

		stmt.close();

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
	public int setRole(String username, String role) throws SQLException {

		String query = "UPDATE User_Info SET Role = ? "
				+ "WHERE (Username = ?)";

		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, role);
		stmt.setString(2, username);

		int resCount = stmt.executeUpdate();
		stmt.close();

		return resCount;
	}

	private void isValidArgument(String arg) throws IOException {
    	if (arg.contentEquals("") || arg == null) {
    		throw new IOException("Invalid argument(s)");
    	}
    }
}
