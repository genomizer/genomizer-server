package JUnitTests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDatabaseConnect {

	private static final String dbDriver = "org.postgresql.Driver";

	private static final String dbConString = "jdbc:postgresql://postgres/c5dv151_vt14" ;
	private static final String dbUser = "c5dv151_vt14";
	private static final String dbPass = "shielohh";

	private Connection dbCon=null;

	@Before
	public void connect() {

		dbCon=null;

		try {
			Class.forName(dbDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			dbCon = DriverManager.getConnection(
					dbConString, dbUser, dbPass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@After
	public void discinnect() {
		try {
			dbCon.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbCon = null;
	}

	@Test
	public void shouldConnect() {

		try {
			assertTrue(dbCon.isValid(5));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void shouldExQuery() {

		Statement statement = null;
		String selectTableSQL = "SELECT Name FROM Workspace";
		ResultSet rs = null;

		try {
			statement = dbCon.createStatement();
			rs = statement.executeQuery(selectTableSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			while (rs.next()) {
				String name = rs.getString("Name");
				System.out.println("Name : " + name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFindSpecific(){

		Statement statement = null;
		String selectTableSQL = "SELECT * FROM experiment WHERE (tissue='toe')";
		ResultSet rs = null;

		try {
			statement = dbCon.createStatement();
			rs = statement.executeQuery(selectTableSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			while (rs.next()) {
				String name = rs.getString("tissue");
				System.out.println("Name : " + name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPrepStatement() {
		
		PreparedStatement statement = null;
		String selectTableSQL = "SELECT ExpID FROM Experiment WHERE (species = ?)";
		try {
			statement = dbCon.prepareStatement(selectTableSQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			statement.setString(1, "fish");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ResultSet rs = null;
		try {
			rs = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			while (rs.next()) {
				String name = rs.getString("ExpID");
				System.out.println("Name : " + name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
