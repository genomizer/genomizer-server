package testSuite.unitTests;

import static org.junit.Assert.assertEquals;

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
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;

public class TestChainFiles {

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
    public static void setup() throws Exception {
        dbac = new DatabaseAccessor(username, password, host, database);

        String url = "jdbc:postgresql://" + host + "/" + database;
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        conn = DriverManager.getConnection(url, props);

        addTuplesSqlStrings = buildSqlStringsFromFile(addTestTuplesPath);

        for (String s : addTuplesSqlStrings) {
            Statement statement = conn.createStatement();
            statement.execute(s);
        }

        clearTablesSqlStrings = buildSqlStringsFromFile(clearTablesPath);
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






    public void shouldAddandGetAndRemoveChainFile () throws SQLException {
    	addChain_file();
    	removeChainFile();
	}



    @Test
    public void addChain_file() throws SQLException {

    	String fromVersion = "hg18";
    	String toVersion = "hg38";
    	String fileName = "chainHuman";

    	String filePath = dbac.addChainFile(fromVersion, toVersion, fileName);

    	assertEquals("http://scratchy.cs.umu.se:8000/upload.php?path=/var/www/data/genome_releases/Human/chain_files/chainHuman",filePath);


    }

    @Test
    public void removeChainFile() throws SQLException {
    	String fromVersion = "hg19";
    	String toVersion = "hg38";

    	assertEquals(1,dbac.removeChainFile(fromVersion, toVersion));

    }

    @Test
    public void getChainFIle() throws SQLException {
    	String fromVersion = "rn3";
    	String toVersion = "rn4";

    	String filePath = dbac.getChainFile(fromVersion, toVersion);

    	assertEquals("/var/www/data/Chain_File/Rat/rn3-rn4.fasta",filePath);
    }
}