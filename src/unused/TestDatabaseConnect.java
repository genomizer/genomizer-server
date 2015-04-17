package unused;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestDatabaseConnect {

	private static final String dbDriver = "org.postgresql.Driver";

	private static final String dbConString = "jdbc:postgresql://postgres/c5dv151_vt14" ;
	private static final String dbUser = "c5dv151_vt14";
	private static final String dbPass = "shielohh";

	private Connection conn=null;

	@Before
	public void connect() {

		conn=null;

		try {
			Class.forName(dbDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(
					dbConString, dbUser, dbPass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@After
	public void discinnect() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn = null;
	}

	@Test
	public void shouldConnect() {

		try {
			assertTrue(conn.isValid(5));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Andr�s tester
	 */
//	@Test
//	public void testSearchByAuthorLabel() {
//		String searchPubMed = "(((Sven[Author]) AND Arm[Tissue]) OR Human[Species])";
//
//		try {
//			SearchResult res = searchFile(searchPubMed);
//			printQResult(res);
//		} catch (SQLException e) {
//			System.out.println("testSearchByAuthorLabel fail");
//		}
//	}

//	@Test
//	public void testSearchByWrongLabel() {
//		String searchPubMed = "2014-04-29[Date]";
//		System.out.println("datum");
//
//		try {
//			SearchResult res = searchFile(searchPubMed);
//			printQResult(res);
//		} catch (SQLException e) {
//			System.out.println("testSearchByWrongLabel fail");
//		}
//	}

//	@Test
//	public void testSearchByNotExistLabel() {
//		String searchPubMed = "2[FileID]";
//
//		try {
//			SearchResult res = searchFile(searchPubMed);
//			printQResult(res);
//		} catch (SQLException e) {
//			System.out.println("testSearchByNotExist fail");
//		}
//	}

	@Test
	public void testValiedFileFail() {
		String path = "c:/doc/1";

		
		try {
			assertFalse(isValidFilePath(path));			
		} catch (SQLException e) {
			System.out.println("testSearchByNotExist fail");
		}
	}
	
	@Test
	public void testValiedFileues() {
		String path = "c:/moho";
		
		try {
			assertTrue(isValidFilePath(path));			
		} catch (SQLException e) {
			System.out.println("testSearchByNotExist fail");
		}
		
	}
	
	@Test
	public void testDifferentFile() {
		String path = "/TestPath/gkdbfalkfnvlankfl";
		
		try {
			assertTrue(isValidFilePath(path));			
		} catch (SQLException e) {
			System.out.println("testSearchByNotExist fail");
		}
		
	}
	
	@Test
	public void testAnotherFilePathFile() {
		String path = "/TestPath/gkdbfalkfn";
		
		//Filepath should be viable
		try {
			assertTrue(isValidFilePath(path));			
		} catch (SQLException e) {
			System.out.println("testSearchByNotExist fail");
		}
		
	}

//	private SearchResult searchFile(String searchPubMed) throws SQLException {
//
//		PreparedStatement pStatement = null;
//		ResultSet res = null;
//
//		PubMedParser theParser = new PubMedParser();
//		ParsedPubMed queryMaterial = theParser.parsePubMed(searchPubMed);
//
//		String query = queryMaterial.getWhereString();
//		System.out.println(query);
//		pStatement = conn.prepareStatement(query);
//		ArrayList<String> valueList = queryMaterial.getValues();
//
//		for(int i = 1;i <= valueList.size();i++){
//
//			if(queryMaterial.getValues().get(i-1).equals("Date")) {
//				valueList.remove(i -1);
//				java.sql.Date date = java.sql.Date.valueOf(queryMaterial.getValues().get(i-1));
//				pStatement.setDate(i,date);
//
//			} else if(isInteger(queryMaterial.getValues().get(i-1))) {
//				pStatement.setInt(i, Integer.parseInt(queryMaterial.getValues().get(i-1)));
//			} else {
//				System.out.println(queryMaterial.getValues().get(i-1));
//				pStatement.setString(i, queryMaterial.getValues().get(i-1));
//			}
//		}
//		res = pStatement.executeQuery();
//		return new SearchResult(res);
//	}

	/**
	 * kanske fixa, vet ej om det bara �r str�ngar eller om det finns int
	 *
	 * @param searchPubMed
	 * @return
	 * @throws SQLException
	 */
//	private SearchResult searchExperiment(String searchPubMed) throws SQLException {
//
//		PreparedStatement pStatement = null;
//		ResultSet res = null;
//		String query = "SELECT * FROM Annotated_With " +
//				"WHERE (";
//
//		PubMedParser theParser = new PubMedParser();
//		ParsedPubMed queryMaterial = theParser.parsePubMed(searchPubMed);
//
//		query = query + queryMaterial.getWhereString() + ")";
//		System.out.println("\n\n" + query + "\n");
//		pStatement = conn.prepareStatement(query);
//
//		for(int i = 1;i <= queryMaterial.getValues().size();i++){
//
//			if(isInteger(queryMaterial.getValues().get(i-1))) {
//				pStatement.setInt(i, Integer.parseInt(queryMaterial.getValues().get(i-1)));
//			} else {
//				pStatement.setString(i, queryMaterial.getValues().get(i-1));
//			}
//		}
//		res = pStatement.executeQuery();
//		return new SearchResult(res);
//	}

//	private void printQResult(SearchResult queryRes) throws SQLException {
//
//		ArrayList<String> result = queryRes.getRowValues(0);
//		ArrayList<String> resultHeader = queryRes.getColHeaders();
//System.out.println("\n\n");
//		for(int i=0;i<resultHeader.size();i++){
//			System.out.print(resultHeader.get(i) + "	|");
//		}
//		System.out.println("\n-----------------------------------------------------------------------------------");
//		if(result != null){
//			for(int i=0;i<result.size();i++){
//				System.out.print(result.get(i) + "	|");
//			}
//		}
//		System.out.println("\n");
//	}

//	private boolean isInteger(String s) {
//		try {
//			Integer.parseInt(s);
//		} catch(NumberFormatException e) {
//	        return false;
//	    }
//	    return true;
//	}

	private boolean isValidFilePath(String filePath) throws SQLException {

		PreparedStatement pStatement = null;

		String query = "SELECT * FROM File Where (Path = ?)";
		
		pStatement = conn.prepareStatement(query);
		pStatement.setString(1, filePath);

		ResultSet res = pStatement.executeQuery();
		
        while (res.next()) {
            return false;
        }

		return true;
	}
}
