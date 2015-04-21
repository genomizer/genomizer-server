package database.subClasses;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains all the methods for adding,changing, getting and removing
 * Users in the database. This class is a subClass of databaseAcessor.java
 *
 * date: 2014-05-14 version: 1.0
 */
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
	 * @param username the username
	 * @param passwordHash password
	 * @param passwordSalt salt used with password
	 * @param role role given to the user ie. "Admin"
	 * @throws SQLException
	 * @throws IOException if an argument string is empty
	 */
	public void addUser(String username, String passwordHash, String passwordSalt, String role,
			String fullName, String email) throws SQLException, IOException {

		isValidArgument(username);
		isValidArgument(passwordHash);
		isValidArgument(passwordSalt);
		isValidArgument(role);
		isValidArgument(fullName);

		String query = "INSERT INTO User_Info (Username, PasswordHash, PasswordSalt, Role) "
				+ "VALUES " + "(?, ?, ?, ?)";

		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, username);
		stmt.setString(2, passwordHash);
		stmt.setString(3, passwordSalt);
		stmt.setString(4, role);
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
	public int deleteUser(String username) throws SQLException {

		String query = "DELETE FROM User_Info " + "WHERE (Username = ?)";

		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, username);
		int res = stmt.executeUpdate();
		stmt.close();
		return res;
	}

	/**
	 * Returns the password hash for the given user. Used for login.
	 *
	 * @param user
	 *            - the username as string
	 * @return String - the password hash
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public String getPasswordHash(String user) throws SQLException {

		String query = "SELECT PasswordHash FROM User_Info "
				+ "WHERE (Username = ?)";

		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, user);
		ResultSet rs = stmt.executeQuery();
		String pass = null;

		if (rs.next()) {
			pass = rs.getString("passwordHash");
		}

		stmt.close();

		return pass;
	}

	/**
	 * Returns the password salt for the given user. Used for login.
	 *
	 * @param user
	 *            - the username as string
	 * @return String - the password salt
	 * @throws SQLException
	 *             if the query does not succeed
	 */
	public String getPasswordSalt(String user) throws SQLException {

		String query = "SELECT PasswordSalt FROM User_Info "
				+ "WHERE (Username = ?)";

		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, user);
		ResultSet rs = stmt.executeQuery();
		String pass = null;

		if (rs.next()) {
			pass = rs.getString("passwordSalt");
		}

		stmt.close();

		return pass;
	}

	/**
	 * Changes the password for a user.
	 *
	 * @param username the user to change the password for.
	 * @param newPassword the new password.
	 * @param newSalt the salt for the new password.
	 * @return the number of tuples updated in the database.
	 * @throws SQLException if the query does not succeed
	 * @throws IOException
	 */
	public int resetPassword(String username, String newPassword, String newSalt)
			throws SQLException, IOException {

		if (username == null || username.contentEquals("") ||
				newPassword == null || newPassword.contentEquals("")) {
			throw new IOException("Invalid arguments");
		}

		String query = "UPDATE User_Info SET PasswordHash = ?, PasswordSalt = ?"
				+ "WHERE (Username = ?)";

		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, newPassword);
		stmt.setString(2, newSalt);
		stmt.setString(3, username);
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

    	if (arg == null || arg.contentEquals("")) {
    		throw new IOException("Invalid argument(s)");
    	}
    }
}
