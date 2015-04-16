package database.test;

import java.io.BufferedReader;
import java.io.File;
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
import java.lang.System;

import org.junit.Ignore;

import database.DatabaseAccessor;
import database.test.unittests.SearchDatabaseTests;
/** */
/**
 * Create an instance of this class if you want to use the test tuples in
 * add_test_tuples.sql.
 * If you don't need the test tuples for your test, you should still use
 * the public strings to connect to the database. This is to make sure that
 * all tests are using the same database.
 */
@Ignore
public class TestInitializer {
    //Use these strings to connect to the test server.
    public static String username;
    public static String password;
    public static String host;
    public static String database;

    static {
      /*if(System.getenv("TRAVIS").equals("true")) {
        // Running on Travis.
        username = "postgres";
        password = "";
        host     = "localhost";
        database = "c5dv151_vt14";
      }*/
      //else {
        // Running in a CS lab.
        username = "c5dv119_vt15_dv13esn";
        password = "";
        host     = "postgres";
        database = "c5dv119_vt15_dv13esn";
      //}
    }

//    public static String username = "genomizer";
//    public static String password = "genomizer";
//    public static String host = "85.226.111.95";
//    public static String database = "genomizer_testdb";

    private String addTestTuplesPath = "sql/add_test_tuples.sql";
    private String clearTablesPath = "sql/clear_tables.sql";

    private DatabaseAccessor dbac;
    private Connection conn;

    private List<String> addTuplesSqlStrings;
    private List<String> clearTablesSqlStrings;

    /**
     * Adds the test tuples to the test database. Should be called in
     * BeforeClass. Don't use this method to re-add tuples during the test.
     * If you have to re-add tuples, use {@link #addTuples() addTuples()} instead.
     * @return the DatabaseAccessor object.
     * @throws Exception
     */
    public DatabaseAccessor setup() throws Exception {
        dbac = new DatabaseAccessor(TestInitializer.username,
                TestInitializer.password, TestInitializer.host,
                TestInitializer.database);

        String url = "jdbc:postgresql://" + TestInitializer.host +
                "/" + TestInitializer.database;
        Properties props = new Properties();
        props.setProperty("user", TestInitializer.username);
        props.setProperty("password", TestInitializer.password);

        conn = DriverManager.getConnection(url, props);

        addTuplesSqlStrings = buildSqlStringsFromFile(addTestTuplesPath);

        addTuples();

        clearTablesSqlStrings = buildSqlStringsFromFile(clearTablesPath);

        return dbac;
    }

    public DatabaseAccessor setupWithoutAddingTuples() throws Exception {
        dbac = new DatabaseAccessor(TestInitializer.username,
                TestInitializer.password, TestInitializer.host, database);

        String url = "jdbc:postgresql://" + TestInitializer.host +
                "/" + database;
        Properties props = new Properties();
        props.setProperty("user", TestInitializer.username);
        props.setProperty("password", TestInitializer.password);

        conn = DriverManager.getConnection(url, props);

        addTuplesSqlStrings = buildSqlStringsFromFile(addTestTuplesPath);

        clearTablesSqlStrings = buildSqlStringsFromFile(clearTablesPath);

        return dbac;
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
    private List<String> buildSqlStringsFromFile(String path)
            throws UnsupportedEncodingException, IOException {
        List<String> sqlStrings = new ArrayList<String>();
        URL sqlFileUrl = new File(path).toURI().toURL();
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

    /**
     * Removes all test tuples from the test database.
     * Should generally only be called once in AfterClass.
     * @throws SQLException
     */
    public void removeTuples() throws SQLException {
        for (String s : clearTablesSqlStrings) {
            Statement statement = conn.createStatement();
            statement.execute(s);
        }
        dbac.close();
    }

    public void removeTuplesKeepConnection() throws SQLException {
        for (String s : clearTablesSqlStrings) {
            Statement statement = conn.createStatement();
            statement.execute(s);
        }
    }

    /**
     * Adds all test tuples to the test database.
     * Should generally not be called manually, because
     * it will make the tests run very slow. {@link #setup() setup()}
     * will call this method.
     * @throws SQLException
     */
    public void addTuples() throws SQLException {
        for (String s : addTuplesSqlStrings) {
            Statement statement = conn.createStatement();
            statement.execute(s);
        }
    }

    /**
     * Recursively deletes a folder with all it's subfolders and files.
     * @param folder the folder to delete.
     */
    public void recursiveDelete(File folder) {
        File[] contents = folder.listFiles();
        if (contents == null || contents.length == 0) {
            folder.delete();
        } else {
            for (File f : contents) {
                recursiveDelete(f);
            }
        }
        folder.delete();
    }
}
