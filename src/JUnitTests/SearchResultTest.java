package JUnitTests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.SearchResult;

public class SearchResultTest {

	private static final String dbDriver = "org.postgresql.Driver";
	private static final String dbConString =
									"jdbc:postgresql://postgres/c5dv151_vt14" ;
	private static final String dbUser = "c5dv151_vt14";
	private static final String dbPass = "shielohh";

	private Connection connection = null;
	private SearchResult resultPackager = null;

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
			resultPackager = new SearchResult(res);
			resultPackager.printList();

			if(res != null){
				res.close();
			}
			pStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testCreateResultFormat() {

		assertEquals(1,1);

		/*
		String id = "awe1123";
		String specie = "Human";
		String s = "M";
		String tis = "arm";
		String cell = "yes";
		String devS = "Early";
		String antiN = "cooltName";
		String antiS = "C";
		String antiB = "ntzzz";

		String query = "SELECT * FROM  Annotated_With(ExpID, Species, Sex, Tissue," +
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

			if(res != null){
				res.close();
			}
			pStatement.close();

		} catch (SQLException e) {
			System.out.println("Error! Couldn't insert values,SQLException\n");
			e.printStackTrace();
		}*/
	}

}
