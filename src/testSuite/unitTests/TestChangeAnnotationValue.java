package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.Experiment;

public class TestChangeAnnotationValue {

    private static String addTestTuplesPath = "/testSuite/add_test_tuples.sql";
    private static String clearTablesPath = "/testSuite/clear_tables.sql";

    public static String username = "genomizer";
    public static String password = "genomizer";
    public static String host = "85.226.111.95";
    public static String database = "genomizer_testdb";

    private static DatabaseAccessor dbac;
    private static Connection conn;

    private static List<String> addTuplesSqlStrings;
    private static List<String> clearTablesSqlStrings;

    @BeforeClass
    public static void setupBeforeClass() throws Exception {
        dbac = new DatabaseAccessor(username, password, host, database);

        String url = "jdbc:postgresql://" + host + "/" + database;
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        conn = DriverManager.getConnection(url, props);

        addTuplesSqlStrings = buildSqlStringsFromFile(addTestTuplesPath);

        clearTablesSqlStrings = buildSqlStringsFromFile(clearTablesPath);

        for (String s : clearTablesSqlStrings) {
            Statement statement = conn.createStatement();
            statement.execute(s);
        }
    }

    /**
     * Builds a list of strings from a sql file so that they can be executed
     * with jdbc.
     *
     * @param path The path to the sql file
     * @return A list of sql strings from the file
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private static List<String> buildSqlStringsFromFile(String path)
            throws UnsupportedEncodingException, IOException {
        List<String> sqlStrings = new ArrayList<String>();
        URL sqlFileUrl = SearchDatabaseTests.class.getResource(path);
        if (sqlFileUrl != null) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    sqlFileUrl.openStream(), "UTF-8"));
            String line = in.readLine();
            StringBuilder statement = new StringBuilder();

            while (line != null) {
                statement.append(line);
                if (line.endsWith(";")) {
                    statement.deleteCharAt(statement.length() - 1);
                    sqlStrings.add(statement.toString());
                    statement.delete(0, statement.length());
                }
                line = in.readLine();
            }
        } else {
            throw new IOException("Could not find file: " + path);
        }
        return sqlStrings;
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException {
        for (String s : clearTablesSqlStrings) {
            Statement statement = conn.createStatement();
            statement.execute(s);
        }
        dbac.close();
    }

    @Before
    public void setup() throws SQLException {
    	for (String s : addTuplesSqlStrings) {
            Statement statement = conn.createStatement();
            statement.execute(s);
        }
    }

    @After
    public void teardown() throws SQLException {
        for (String s : clearTablesSqlStrings) {
            Statement statement = conn.createStatement();
            statement.execute(s);
        }
    }

    @Test
    public void shouldChangeAnnotationValueForAnnotatedWith() throws Exception {

    	Experiment exp1 = dbac.getExperiment("Exp1");

    	String label = "Sex";
    	String newValue = "Monkey";
    	String oldValue = exp1.getAnnotations().get(label);


    	dbac.changeAnnotationValue(label, oldValue, newValue);

    	exp1 = dbac.getExperiment("Exp1");
    	assertEquals(exp1.getAnnotations().get(label), newValue);
    	assertFalse(exp1.getAnnotations().get(label).equals(oldValue));

    }

    @Test
    public void shouldChangeAnnotationValueForAnnotationChoices() throws Exception {
    	Experiment exp1 = dbac.getExperiment("Exp1");

    	String label = "Sex";
    	String newValue = "Monkey";
    	String oldValue = exp1.getAnnotations().get(label);

    	dbac.changeAnnotationValue(label, oldValue, newValue);

    	ArrayList<String> choices = (ArrayList<String>) dbac.getChoices(label);
    	assertTrue(choices.contains(newValue));
    	assertFalse(choices.contains(oldValue));
	}

    @Test
    public void shouldChangeAnnotationValueForAnnotationTable() throws Exception {
    	Experiment exp1 = dbac.getExperiment("Exp1");

    	String label = "Sex";
    	String newValue = "Monkey";
    	String oldValue = exp1.getAnnotations().get(label);

    	dbac.changeAnnotationValue(label, oldValue, newValue);

    	assertEquals(dbac.getDefaultAnnotationValue(label), newValue);
	}

    @Test
    public void shouldBeAbleToChangeFreeTextValue() throws Exception {
    	Experiment exp1 = dbac.getExperiment("Exp1");

    	String label = "Tissue";
    	String newValue = "Monkey";
    	String oldValue = exp1.getAnnotations().get(label);

    	dbac.changeAnnotationValue(label, oldValue, newValue);

    	exp1 = dbac.getExperiment("Exp1");
    	assertEquals(exp1.getAnnotations().get(label), newValue);
    	assertFalse(exp1.getAnnotations().get(label).equals(oldValue));
	}

    @Test(expected = SQLException.class)
    public void shouldThrowAnExceptionWhenValueAlreadyExistsInChoices() throws Exception {
    	Experiment exp1 = dbac.getExperiment("Exp1");

    	String label = "Sex";
    	String newValue = "Female";
    	String oldValue = exp1.getAnnotations().get(label);

    	dbac.changeAnnotationValue(label, oldValue, newValue);
	}
}
