package testSuite.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;
import database.Experiment;
import database.FilePathGenerator;
import database.FileTuple;

public class FileTableTests {

    private static DatabaseAccessor dbac;

    private String testName = "testFileName1";
    private String testInputFile = "testInputFile";
    private int testFileType = FileTuple.RAW;
    private String testAuthor = "testFileAuthor1";
    private String testUploader = "testUploader1";
    private String testMetaData = "testMetaData";
    private boolean testIsPrivate = false;
    private static String testExpId = "testExpId2";
    private String testGRVersion = null;
    private static FileTuple ft;

    private static String testFolderName = "Genomizer Test Folder - Dont be afraid to delete me";
    private static File testFolder;
    private static String testFolderPath;
    private static FilePathGenerator fpg;

    private static TestInitializer ti;

    @BeforeClass
    public static void setupTestCase() throws Exception {
        dbac = new DatabaseAccessor(TestInitializer.username,
                TestInitializer.password, TestInitializer.host,
                TestInitializer.database);

        testFolderPath = System.getProperty("user.home") + File.separator
                + testFolderName + File.separator;

        testFolder = new File(testFolderPath);

        if (!testFolder.exists()) {
            testFolder.mkdirs();
        }

        fpg = dbac.getFilePathGenerator();
        fpg.setRootDirectory(testFolderPath);

        dbac.addExperiment(testExpId);

        ti = new TestInitializer();
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException {
        if (dbac.hasFile(ft.id)) {
            dbac.deleteFile(ft.id);
        }
        dbac.deleteExperiment(testExpId);
        dbac.close();
        ti.recursiveDelete(testFolder);
    }

    @Before
    public void setup() throws SQLException, IOException {
        ft = dbac.addNewFile(testExpId, testFileType, testName, testInputFile,
                testMetaData, testAuthor, testUploader, testIsPrivate,
                testGRVersion);
    }

    @After
    public void teardown() throws SQLException {
        dbac.deleteFile(ft.path);
    }

    @Test
    public void testGetDeleteGetAddGetFile() throws Exception {

        Experiment e = dbac.getExperiment(testExpId);
        assertEquals(1, e.getFiles().size());
        assertEquals(ft.path, e.getFiles().get(0).path);

        dbac.deleteFile(ft.path);
        e = dbac.getExperiment(testExpId);
        assertEquals(0, e.getFiles().size());

        FileTuple ft = dbac.addNewFile(testExpId, testFileType, testName,
                testInputFile, testMetaData, testAuthor, testUploader,
                testIsPrivate, testGRVersion);
        e = dbac.getExperiment(testExpId);
        assertEquals(1, e.getFiles().size());
        ft = e.getFiles().get(0);
        assertEquals(fpg.getRawFolderPath(testExpId) + testName, ft.path);
    }

    @Test(expected = SQLException.class)
    public void shouldNotBeAbleToDeleteAnExperimentContainingAFile()
            throws SQLException {

        try {
            dbac.deleteExperiment(testExpId);
        } catch (Exception e) {
            throw e;
        } finally {
            assertTrue(dbac.hasExperiment(testExpId));
        }
    }

    @Test
    public void shouldBeAbleToCheckIfFileExistsInDatabase() throws Exception {
        ArrayList<Experiment> experiments = (ArrayList<Experiment>) dbac
                .search(ft.path + "[Path]");
        Experiment experiment = experiments.get(0);
        int fileID = experiment.getFiles().get(0).id;
        assertTrue(dbac.hasFile(fileID));
    }

    @Test
    public void shouldBeAbleToDeleteFileUsingFileID() throws Exception {
        int fileID = ft.id;
        assertEquals(1, dbac.deleteFile(fileID));
        assertFalse(dbac.hasFile(fileID));
    }

    @Test
    public void shouldReturnZeroIfFileToBeDeletedDoesNotExistInDatabase()
            throws Exception {
        if (dbac.hasFile(123)) {
            fail("Use another ID for the test, this one"
                    + "exists in the database.");
        }
        assertEquals(0, dbac.deleteFile(123));
    }

    @Test
    public void shouldRemoveFileFromDisk() throws Exception {
        addMockFile(ft.getParentFolder(), testName);
        File fileToDelete = new File(ft.path);
        assertTrue(fileToDelete.exists());
        assertEquals(1, dbac.deleteFile(ft.path));
        assertFalse(fileToDelete.exists());
    }

    private void addMockFile(String folderPath, String filename1)
            throws IOException {
        File file1 = new File(folderPath + filename1);
        file1.createNewFile();
    }
}
