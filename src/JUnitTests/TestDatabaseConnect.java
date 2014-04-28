package JUnitTests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
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
	public void testParameter(){
/*
		Statement statement = null;
		String selectTableSQL = "SELECT Name FROM Workspace WHERE ";
		ResultSet rs = null;

		try {
			statement = dbCon.createStatement();
			rs = statement.executeQuery(selectTableSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
*/
	}


}
