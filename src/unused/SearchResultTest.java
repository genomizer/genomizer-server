package unused;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SearchResultTest {

	private static final String dbDriver = "org.postgresql.Driver";
	private static final String dbConString =
									"jdbc:postgresql://postgres/c5dv151_vt14" ;
	private static final String dbUser = "c5dv151_vt14";
	private static final String dbPass = "shielohh";

	private String query = "SELECT * FROM File NATURAL JOIN Annotated_With " +
			   "WHERE (Label = ? AND Value = ?)";

	private Connection connection = null;
	private SearchResult searchResult = null;

	@Before
	public void setup(){
		PreparedStatement pStatement;

		try{
			Class.forName(dbDriver);
			connection = DriverManager.getConnection(
					dbConString, dbUser, dbPass);
			pStatement = connection.prepareStatement(query);
			pStatement.setString(1, "Species");
			pStatement.setString(2, "Human");

			ResultSet res = pStatement.executeQuery();
			searchResult = new SearchResult(res);

			if(res != null){
				res.close();
			}
			pStatement.close();
		}
		catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
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
	public void testCreateResult() {
		assertNotNull(searchResult);
	}

	@Test
	public void testColCount() {
		assertEquals(searchResult.getColCount(), 12);
	}

	@Test
	public void testRowCount() {
		assertEquals(searchResult.getRowCount(), 1);
	}

	@Test
	public void testGetList() {
		assertNotNull(searchResult.getList());
	}

	@Test
	public void testSyncHeaderAndValue(){
		ArrayList<String> headers = searchResult.getColHeaders();
		ArrayList<String> values = searchResult.getRowValues(0);

		for (int i = 0; i < headers.size(); i++) {
			//System.out.print(headers.get(i) + ", ");
		}

		//System.out.println();

		for (int i = 0; i < values.size(); i++) {
			//System.out.print(values.get(i) + ", ");
		}
	}

	@Test
	public void testGetCorrectValueByAnnotation() {
		String value = searchResult.getValueByAnnotation(0, "uploader");
		assertEquals("Per", value);
	}

	@Test
	public void testAnnotationIncorrectValues() {
		String value = searchResult.getValueByAnnotation(0, "ghgh");
		assertNull(value);
	}

	@Test
	public void testAnnotationIncorrectValues2() {
		String value = searchResult.getValueByAnnotation(-1, "ghgh");
		assertNull(value);
	}

	@Test
	public void testAnnotationIncorrectValues3() {
		String value = searchResult.getValueByAnnotation(1, "ghgh");
		assertNull(value);
	}

	@Test
	public void testGetCorrectValueByIndex() {
		String value = searchResult.getValueByIndex(0, 7);
		assertEquals("Per", value);
	}

	@Test
	public void testAnnotationIncorrectIndex() {
		String value = searchResult.getValueByIndex(-1, 7);
		assertNull(value);
	}

	@Test
	public void testAnnotationIncorrectIndex2() {
		String value = searchResult.getValueByIndex(9999, 7);
		assertNull(value);
	}

	@Test
	public void testAnnotationIncorrectIndex3() {
		String value = searchResult.getValueByIndex(9999, -1);
		assertNull(value);
	}

	@Test
	public void testAnnotationIncorrectIndex4() {
		String value = searchResult.getValueByIndex(9999, 9999);
		assertNull(value);
	}
}
