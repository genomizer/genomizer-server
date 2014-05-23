package database.testSuite.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.Experiment;
import database.FilePathGenerator;
import database.FileTuple;
import database.testSuite.TestInitializer;

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

    private static String testFolderName =
    		"Genomizer Test Folder - Dont be afraid to delete me";
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
    public static void undoAllChanges() throws Exception {

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
        addMockFile(ft.getParentFolder(), ft.filename);
    }


    @After
    public void teardown() throws Exception {

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

        List<Experiment> experiments = dbac.search(ft.path + "[Path]");
        Experiment experiment = experiments.get(0);
        int fileID = experiment.getFiles().get(0).id;
        assertTrue(dbac.hasFile(fileID));
    }


    @Test
    public void shouldBeAbleToDeleteFileUsingFileID() throws Exception {

    	int fileID = ft.id;
        assertEquals(1, dbac.deleteFile(fileID));
        assertFalse(dbac.hasFile(fileID));

        dbac.addNewFile(testExpId, testFileType, testName,
                testInputFile, testMetaData, testAuthor, testUploader,
                testIsPrivate, testGRVersion);
    }


    @Test (expected = IOException.class)
    public void shouldReturnZeroIfFileToBeDeletedDoesNotExistInDatabase()
            throws Exception {

        dbac.deleteFile(212313);
    }


    @Test
    public void shouldRemoveFileFromDisk() throws Exception {

        addMockFile(ft.getParentFolder(), testName);
        File fileToDelete = new File(ft.path);
        assertTrue(fileToDelete.exists());
        assertEquals(1, dbac.deleteFile(ft.path));
        assertFalse(fileToDelete.exists());

        dbac.addNewFile(testExpId, testFileType, testName, testInputFile,
        		testMetaData, testAuthor, testUploader, testIsPrivate,
        		testGRVersion);
    }

    private void addMockFile(String folderPath, String filename1)
            throws IOException {

        File file1 = new File(folderPath + filename1);
        file1.createNewFile();
    }


    @Test
    public void shouldBeInProgressAfterAddition() throws Exception {

        Experiment e = dbac.getExperiment(testExpId);
        ft = e.getFiles().get(0);

        assertEquals("In Progress", ft.status);
    }


    @Test
    public void shouldBeDoneAfterCallingReadyForDownload() throws Exception {

        dbac.fileReadyForDownload(ft.id);
        Experiment e = dbac.getExperiment(testExpId);
        ft = e.getFiles().get(0);

        assertEquals("Done", ft.status);
    }


    @Test
    public void changeFileNameTest() throws SQLException, IOException,
            ParseException {

        dbac.addGenomeRelease("te34", "Dog", "te34.txt");
        dbac.addExperiment("expert1");
        FileTuple fileStore = dbac.addNewFile("expert1", 1, "temp1", "temp2",
                "-a -g", "Claes", "Claes", false, "te34");
        File temp1 = new File(fileStore.path);
        temp1.createNewFile();
        assertTrue(temp1.exists());

        List<Experiment> res = dbac.search("Claes[Uploader]");
        int rowCount = dbac.changeFileName(res.get(0).getFiles().get(0).id,
                "final1");
        assertEquals(1, rowCount);

        res = dbac.search("Claes[Uploader]");
        assertEquals("final1", res.get(0).getFiles().get(0).filename);
        assertEquals(testFolderPath + "expert1/raw/final1", res.get(0)
                .getFiles().get(0).path);
        assertFalse(res.get(0).getFiles().get(0).filename.equals("temp1"));

        dbac.deleteFile(res.get(0).getFiles().get(0).id);
        dbac.deleteExperiment("expert1");
        dbac.removeGenomeRelease("te34");
    }

}