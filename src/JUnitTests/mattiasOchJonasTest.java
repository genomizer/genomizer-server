package JUnitTests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class mattiasOchJonasTest {

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
	public void testCreateResultFormat() {

		String id = "awe1123";
		String specie = "Human";
		String s = "M";
		String tis = "arm";
		String cell = "yes";
		String devS = "Early";
		String antiN = "cooltName";
		String antiS = "C";
		String antiB = "ntzzz";

		String query = "INSERT INTO Experiment(ExpID, Species, Sex, Tissue," +
					   " CellType, DevStage, AntiName, Antisymbol, Antibody)"+
					   " VALUES(?,?,?,?,?,?,?,?,?) RETURNING *";
		ResultSet res = null;

		try {
			PreparedStatement pStatement = connection.prepareStatement(query);
			pStatement.setString(1, id);
			pStatement.setString(2, specie);
			pStatement.setString(3, s);
			pStatement.setString(4, tis);
			pStatement.setString(5, cell);
			pStatement.setString(6, devS);
			pStatement.setString(7, antiN);
			pStatement.setString(8, antiS);
			pStatement.setString(9, antiB);

			res = pStatement.executeQuery();
	}

}
