package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;

import database.DatabaseAccessor;

public class AddDropDownValueTest {

	private static Connection conn;
	private static DatabaseAccessor dbac;

	private static String testLabel1 = "Sex";
	private static String testLabel2 = "Species";
	private static String testLabel3 = "Tissue";
	private static String testDefValue1 = "tjo";
	private static String testDefValue3 = "Arm";
	private static ArrayList<String> testChoices1 = new ArrayList<String>();
	private static ArrayList<String> testChoices2 = new ArrayList<String>();

	@BeforeClass
	public static void setupTestCase() throws Exception {

        dbac = new DatabaseAccessor(TestInitializer.username,
                TestInitializer.password, TestInitializer.host,
                TestInitializer.database);
		String url = "jdbc:postgresql://" + TestInitializer.host +
				"/" + TestInitializer.database;
		Properties props = new Properties();
		props.setProperty("user", TestInitializer.username);
		props.setProperty("password", TestInitializer.password);

		conn = DriverManager.getConnection(url, props);

		testChoices1.add("Male");
        testChoices1.add("Female");
        testChoices1.add("Unknown");
        testChoices1.add("moho");

        testChoices2.add("Donkey");
        testChoices2.add("Monkey");
        testChoices2.add("Human");
        testChoices2.add("Yuri");

	}

	@AfterClass
	public static void undoAllChanges() throws SQLException {
		conn.close();
	}

    @Before
    public void setup() throws SQLException, IOException {

        dbac.addDropDownAnnotation(testLabel1, testChoices1, 0, false);
        dbac.addDropDownAnnotation(testLabel2, testChoices2, 2, false);
        dbac.addFreeTextAnnotation(testLabel3, testDefValue3, false);
    }

    @After
    public void teardown() throws SQLException {
        dbac.deleteAnnotation(testLabel1);
        dbac.deleteAnnotation(testLabel2);
        dbac.deleteAnnotation(testLabel3);
    }

    @Test
    public void testConnection() {
    	assertNotNull(conn);
    }

    @Test
    public void testIfCorrectDataType() {

    	int ress = 0;
    	try {
			ress = addDropDownAnnotationValue(testLabel1, testDefValue1);
		} catch (SQLException e) {
		} catch (IOException e) {


		}
    	assertEquals(ress, 1);
    }

    @Test
    public void shouldThrowSQLException() {
    	boolean shouldBeTrue = false;
    	try {
			addDropDownAnnotationValue(testLabel1, testChoices1.get(0));
		} catch (SQLException e) {
			shouldBeTrue = true;
		} catch (IOException e) {

		}
    	assertTrue(shouldBeTrue);
    }

    @Test
    public void shouldThrowIOException() {
    	boolean shouldBeTrue = false;
    	try {
			addDropDownAnnotationValue(testLabel3, "something");
		} catch (SQLException e) {
		} catch (IOException e) {
			shouldBeTrue = true;
		}
    	assertTrue(shouldBeTrue);
    }

    /**
     * Method to add a value to a existing DropDown annotation.
     *
     * @param label, the label of the chosen DropDown annotation.
     * @param value, the value that will be added to the DropDown annotation.
     * @return, Integer, how many rows that were added to the database.
     * @throws SQLException, if the value already exist or another SQL error.
     * @throws IOException, if the chosen label does not represent a DropDown
     * annotation.
     */
    private int addDropDownAnnotationValue(String label, String value) throws SQLException, IOException {

    	String statementStr = "SELECT * FROM Annotation WHERE " +
    			"(label = ? AND datatype = 'DropDown')";

        PreparedStatement checkTag = conn
                .prepareStatement(statementStr);
        checkTag.setString(1, label);

        ResultSet rs = checkTag.executeQuery();
        boolean res = rs.next();
        checkTag.close();

        if(!res) {
        	throw new IOException("The annotation of the chosen label is not of type DropDown");
        } else {

        	statementStr = "INSERT INTO Annotation_Choices (label , value) "
        			+ "VALUES (?,?)";

        	PreparedStatement insertTag = conn
        			.prepareStatement(statementStr);
        	insertTag.setString(1, label);
        	insertTag.setString(2, value);
        	int ress = insertTag.executeUpdate();
        	insertTag.close();

        	return ress;
        }
    }
}
