package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import database.DatabaseAccessor;
import database.Experiment;

public class FileTableTests {

    private static DatabaseAccessor dbac;

    private String testPath;
    private String testName = "testFileName1";
    private String testType = "testFileType1";
    private String testAuthor = "testFileAuthor1";
    private String testUploader = "testUploader1";
    private String testMetaData = "testUploader1";
    private boolean testIsPrivate = false;
    private String testExpId = "testExpId1";
    private String testGRVersion = null;

    @BeforeClass
    public static void setupTestCase() throws Exception {
        dbac = new DatabaseAccessor(SearchDatabaseTests.username,
                SearchDatabaseTests.password, SearchDatabaseTests.host,
                SearchDatabaseTests.database);
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException {
        dbac.close();
    }

    @Before
    public void setup() throws SQLException, IOException {
        dbac.addExperiment(testExpId);
        testPath = dbac.addFile(testType, testName, testMetaData,
                testAuthor, testUploader, testIsPrivate, testExpId,
                testGRVersion);
    }

    @After
    public void teardown() throws SQLException {
        dbac.deleteFile(testPath);
        dbac.deleteExperiment(testExpId);
    }

    @Test
    public void testGetDeleteGetAddGetFile() throws Exception {

        Experiment e = dbac.getExperiment(testExpId);
        assertEquals(1, e.getFiles().size());
        assertEquals(testPath, e.getFiles().get(0).path);

        dbac.deleteFile(testPath);
        e = dbac.getExperiment(testExpId);
        assertEquals(0, e.getFiles().size());

        testPath = dbac.addFile(testType, testName, testMetaData,
                testAuthor, testUploader, testIsPrivate, testExpId,
                testGRVersion);
        e = dbac.getExperiment(testExpId);
        assertEquals(1, e.getFiles().size());
        assertEquals(testPath, e.getFiles().get(0).path);
    }

    @Test(expected = SQLException.class)
    public void shouldNotBeAbleToDeleteAnExperimentContainingAFile()
            throws Exception {

        try {
            dbac.deleteExperiment(testExpId);
        } catch (Exception e) {
            throw e;
        } finally {
            assertTrue(dbac.hasExperiment(testExpId));
        }
    }

}
