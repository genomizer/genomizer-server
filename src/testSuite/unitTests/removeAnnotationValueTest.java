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

public class removeAnnotationValueTest {

	private static Connection conn;
	private static DatabaseAccessor dbac;

	private static String testLabel1 = "Sex";
	private static String testLabel2 = "Species";
	private static String testDefValue1 = "Male";
	private static String testDefValue2 = "Human";
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
    }

    @After
    public void teardown() throws SQLException {
        dbac.deleteAnnotation(testLabel1);
        dbac.deleteAnnotation(testLabel2);
    }

    @Test
    public void testConnection() {
    	assertNotNull(conn);
    }

    @Test
    public void testRemoveDefaultValue() {
    	boolean shouldBeTrue = false;
    	try {
			removeAnnotationValue(testLabel1, testDefValue1);
		} catch (SQLException e) {
			System.out.println("testRemovedefaultValue");
		} catch (IOException e) {
			shouldBeTrue = true;
		}
    	assertTrue(shouldBeTrue);
    }

    @Test
    public void testRemoveAnnotationValue() {
    	try {
			assertEquals(1, removeAnnotationValue(testLabel1, "Unknown"));
		} catch (SQLException e) {
			System.out.println("testRemoveAnnotationValue");
		} catch (IOException e) {
			System.out.println("testRemoveAnnotationValue tried to remove defaultvalue");
		}
    }

    @Test
    public void testRemoveNonExistentValue() {
    	try {
			assertEquals(0, removeAnnotationValue(testLabel1, "Somethingthatdoesntexist"));
		} catch (SQLException e) {
			System.out.println("testRemoveNonExistentValue");
		} catch (IOException e) {
			System.out.println("testRemoveNonExistentValue tried to remove defaultvalue");
		}
    }

    @Test
    public void testRemoveDefaultValue2() {
    	boolean shouldBeTrue = false;
    	try {
			removeAnnotationValue(testLabel2, testDefValue2);
		} catch (SQLException e) {
			System.out.println("testRemovedefaultValue2");
		} catch (IOException e) {
			shouldBeTrue = true;
		}
    	assertTrue(shouldBeTrue);
    }

    @Test
    public void testRemoveAnnotationValue2() {
    	try {
			assertEquals(1, removeAnnotationValue(testLabel2, "Monkey"));
		} catch (SQLException e) {
			System.out.println("testRemoveAnnotationValue");
		} catch (IOException e) {
			System.out.println("testRemoveAnnotationValue tried to remove defaultvalue");
		}
    }

    @Test
    public void testRemoveNonExistentValue2() {
    	try {
			assertEquals(0, removeAnnotationValue(testLabel2, "Somethingthatdoesntexist"));
		} catch (SQLException e) {
			System.out.println("testRemoveNonExistentValue");
		} catch (IOException e) {
			System.out.println("testRemoveNonExistentValue tried to remove defaultvalue");
		}
    }

    /**
     * Method to remove a given annotation of a dropdown- annotation.
     *
     * @param label, the label of the chosen annotation
     * @param the value of the chosen annotation.
     * @return boolean, true if a value was removed, false if not. This could be
     *         if no matching value was found in the database.
     * @throws SQLException
     * @throws IOException, throws an IOException if the chosen value to be
     * 			removed is the active DefaultValue of the chosen label.
     *
     */
    private int removeAnnotationValue(String label, String value)
    		throws SQLException, IOException {

    	String statementStr = "SELECT * FROM Annotation WHERE " +
    			"(label = ? AND defaultvalue = ?)";

        PreparedStatement checkTag = conn
                .prepareStatement(statementStr);
        checkTag.setString(1, label);
        checkTag.setString(2, value);

        ResultSet rs = checkTag.executeQuery();

        boolean res = rs.next();
        checkTag.close();

        if(res) {
        	throw new IOException("The chosen value of the label is a default " +
        			"value. Change the default value of the label and run this " +
        			"method again.");
        } else {

        	statementStr = "DELETE FROM Annotation_Choices "
        			+ "WHERE (label = ? AND value = ?)";

        	PreparedStatement deleteTag = conn
        			.prepareStatement(statementStr);
        	deleteTag.setString(1, label);
        	deleteTag.setString(2, value);
        	int ress = deleteTag.executeUpdate();
        	deleteTag.close();

        	return ress;
        }
    }
}
