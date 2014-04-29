package JUnitTests;

import static org.junit.Assert.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.SearchResult;

public class DatabaseQueryTests {

	private static final String dbDriver = "org.postgresql.Driver";
	private static final String dbConString =
									"jdbc:postgresql://postgres/c5dv151_vt14" ;
	private static final String dbUser = "c5dv151_vt14";
	private static final String dbPass = "shielohh";

	private Connection connection = null;

	@Before
	public void setup(){

		try{
			Class.forName(dbDriver);
		}
		catch(ClassNotFoundException e) {

			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			return;

		}
		try{

			connection = DriverManager.getConnection(
						dbConString, dbUser, dbPass);
		}
		catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}
	}

	@After
	public void tearDown(){
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("Failed to close the connection!!\n");
		}
	}

	@Test
	public void testStartUpConnectionDatabase() {
		assertNotNull(connection);
	}

	//databse must contain values for following test to succeed.ArrayList
		@Test
	public void testSimpleSearchByAnnotation(){

		String query = "SELECT * FROM File NATURAL JOIN Annotated_With " +
					   "WHERE (Label = ? AND Value = ?)";

		PreparedStatement pStatement;
		try {
			pStatement = connection.prepareStatement(query);
			pStatement.setString(1, "Species");
			pStatement.setString(2, "Human");

			ResultSet res = pStatement.executeQuery();


			if(res != null){
				res.close();
			}
			pStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
