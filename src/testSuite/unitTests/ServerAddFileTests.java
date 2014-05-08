package testSuite.unitTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.FilePathGenerator;
import database.FileTuple;

public class ServerAddFileTests {

    private static DatabaseAccessor dbac;

    private String testFileName = "testFileName1";
    private int testFileType = FileTuple.RAW;
    private String testAuthor = "testFileAuthor1";
    private String testUploader = "testUploader1";
    private String testMetaData = "testMetaData1";
    private String testInputFileName = "testInputFileName";
    private boolean testIsPrivate = false;
    private String testExpId = "testExpId1";

    private FileTuple ft;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        dbac = new DatabaseAccessor(SearchDatabaseTests.username,
                SearchDatabaseTests.password, SearchDatabaseTests.host,
                SearchDatabaseTests.database);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        dbac.close();
    }

    @Before
    public void setUp() throws Exception {
        dbac.addExperiment(testExpId);
        ft = dbac.addNewFile(testExpId, testFileType, testFileName, testInputFileName, testMetaData, testAuthor, testUploader, testIsPrivate, null);
    }

    @After
    public void tearDown() throws Exception {
        dbac.deleteFile(ft.path);
        dbac.deleteExperiment(testExpId);
    }

    @Test
    public void testGetDeleteGetAddGetFile() throws Exception {
        FileTuple ft = dbac.getExperiment(testExpId).getFiles().get(0);
        assertEquals(FilePathGenerator.GenerateFilePath(testExpId, testFileType, testFileName), ft.path);
    }

}
