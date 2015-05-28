package database.test.unittests;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import database.DatabaseAccessor;
import database.containers.Experiment;
import database.containers.FileTuple;
import database.test.TestInitializer;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FileTableTests {


    private static TestInitializer ti;
    private static DatabaseAccessor dbac;
    private String testExpID1 = "Exp1";
    private FileTuple testTuple;
    private int fileID;
    private String originalMeta;
    private String originalAuthor;
    private String originalGRVersion;

    @BeforeClass
    public static void setupTestCase() throws Exception {
        ti = new TestInitializer();
        dbac = ti.setup();
    }


    @AfterClass
    public static void undoAllChanges() throws Exception {
        ti.removeTuples();
        dbac.close();
    }


    @Before
    public void setup() throws Exception {
        Experiment e = dbac.getExperiment(testExpID1);
        testTuple = e.getFiles().get(0);
        fileID = testTuple.id;
        originalMeta = testTuple.metaData;
        originalAuthor = testTuple.author;
        originalGRVersion = testTuple.grVersion;
    }


    @After
    public void teardown() throws Exception {

    }

    @Test
    public void shouldGetExperimentFiles() throws Exception {
        Experiment e = dbac.getExperiment(testExpID1);
        int expected = 2;
        int actual = e.getFiles().size();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldDeleteFile() throws Exception {
        Experiment e = dbac.getExperiment(testExpID1);
        FileTuple ft = e.getFiles().get(0);
        dbac.deleteFile(ft.path);
        e = dbac.getExperiment(testExpID1);
        int expected = 1;
        int actual = e.getFiles().size();
        assertEquals(expected, actual);

        ti.removeTuplesKeepConnection();
        ti.addTuples();
    }

    @Test (expected = IOException.class)
    public void shouldNotBeAbleToDeleteExperimentContainingAFile() throws Exception {
        try {
            dbac.deleteExperiment(testExpID1);
        } catch (Exception e) {
            throw e;
        } finally {
            assertTrue(dbac.hasExperiment(testExpID1));
        }
    }

    @Test
    public void shouldBeAbleToCheckIfFileExistsInDatabase() throws Exception {
        Experiment e = dbac.getExperiment(testExpID1);
        int fileID = e.getFiles().get(0).id;
        assertTrue(dbac.hasFile(fileID));
    }

    @Test
    public void shouldNotFindFileThatIsNotInDB() throws Exception {
        List<Experiment> experiments = dbac.search("");
        int nonExistentID = 0;
        for (Experiment e : experiments) {
            for (FileTuple currentFile : e.getFiles()) {
                nonExistentID += currentFile.id;
            }
        }
        assertFalse(dbac.hasFile(nonExistentID));
    }

    @Test
    public void shouldBeAbleToDeleteFileUsingFileID() throws Exception {
        Experiment e = dbac.getExperiment(testExpID1);
        FileTuple ft = e.getFiles().get(0);
        dbac.deleteFile(ft.id);
        e = dbac.getExperiment(testExpID1);
        int expected = 1;
        int actual = e.getFiles().size();
        assertEquals(expected, actual);

        ti.removeTuplesKeepConnection();
        ti.addTuples();
    }

    @Test
    public void shouldReturnZeroIfFileToBeDeletedDoesNotExist() throws Exception {
        List<Experiment> experiments = dbac.search("");
        int nonExistentID = 0;
        for (Experiment e : experiments) {
            for (FileTuple currentFile : e.getFiles()) {
                nonExistentID += currentFile.id;
            }
        }
        assertEquals(0, dbac.deleteFile(nonExistentID));
    }

    @Test
    public void shouldRemoveFileFromDisk() throws Exception {
        FileTuple addedFile = null;
        String testFolderPath = TestInitializer.createScratchDir();
        String testFileName = "shouldRemoveFileFromDiskTest.txt";
        String root = dbac.getFilePathGenerator().getRootDirectory();
        dbac.getFilePathGenerator().setRootDirectory(testFolderPath);
        addedFile = dbac.addNewFile(
                testExpID1,
                FileTuple.PROFILE,
                testFileName,
                "input.file",
                "meta",
                "author",
                "uploader",
                false,
                null,
                null);
        File fileToBeDeleted = new File(addedFile.path);
        addMockFile(addedFile.path);
        if (!(new File(addedFile.path).exists()))
            fail("Test file could not be created.");
        dbac.deleteFile(addedFile.path);

        assertFalse(fileToBeDeleted.exists());
        dbac.getFilePathGenerator().setRootDirectory(root);

        ti.recursiveDelete(new File(testFolderPath));
    }

    private void addMockFile(String filepath)
            throws IOException {

        File file = new File(filepath);
        file.createNewFile();
    }

    @Test
    public void shouldBeInProgressAfterAdd() throws Exception {
        FileTuple addedFile = null;
        String testFolderPath = TestInitializer.createScratchDir();
        String testFileName = "shouldBeInProgressAfterAdd.txt";
        String root = dbac.getFilePathGenerator().getRootDirectory();
        dbac.getFilePathGenerator().setRootDirectory(testFolderPath);
        addedFile = dbac.addNewInProgressFile(
                testExpID1,
                FileTuple.PROFILE,
                testFileName,
                "input.file",
                "meta",
                "author",
                "uploader",
                false,
                null,
                null);
        File fileToAdd = new File(addedFile.path);
        addMockFile(addedFile.path);
        if (!(new File(addedFile.path).exists()))
            fail("Test file could not be created.");

        assertTrue(addedFile.status.equals("In Progress"));

        dbac.deleteFile(addedFile.path);
        dbac.getFilePathGenerator().setRootDirectory(root);
        ti.recursiveDelete(new File(testFolderPath));
    }

    @Test
    public void shouldChangeFileName() throws SQLException, IOException {
        FileTuple addedFile = null;
        String testFolderPath = TestInitializer.createScratchDir();
        String testFileName = "shouldBeInProgressAfterAdd.txt";
        String root = dbac.getFilePathGenerator().getRootDirectory();
        dbac.getFilePathGenerator().setRootDirectory(testFolderPath);
        addedFile = dbac.addNewFile(
                testExpID1,
                FileTuple.PROFILE,
                testFileName,
                "input.file",
                "meta",
                "author",
                "uploader",
                false,
                null,
                null);
        File fileToAdd = new File(addedFile.path);
        addMockFile(addedFile.path);
        if (!(new File(addedFile.path).exists()))
            fail("Test file could not be created.");

        assertEquals(dbac.changeFileName(addedFile.id, "abcd.fastq"), 1);

        dbac.deleteFile(addedFile.path);
        dbac.getFilePathGenerator().setRootDirectory(root);
        ti.recursiveDelete(new File(testFolderPath));
    }

    @Test
    public void shouldChangeFileNameString() throws SQLException, IOException {
        FileTuple addedFile = null;
        String testFolderPath = TestInitializer.createScratchDir();
        String testFileName = "shouldBeInProgressAfterAdd.txt";
        String root = dbac.getFilePathGenerator().getRootDirectory();
        dbac.getFilePathGenerator().setRootDirectory(testFolderPath);
        addedFile = dbac.addNewFile(
                testExpID1,
                FileTuple.PROFILE,
                testFileName,
                "input.file",
                "meta",
                "author",
                "uploader",
                false,
                null,
                null);
        File fileToAdd = new File(addedFile.path);
        addMockFile(addedFile.path);
        if (!(new File(addedFile.path).exists()))
            fail("Test file could not be created.");

        dbac.changeFileName(addedFile.id, "abcd.fastq");
        addedFile = dbac.getFileTuple(addedFile.id);
        assertTrue(addedFile.filename.equals("abcd.fastq"));

        dbac.deleteFile(addedFile.path);
        dbac.getFilePathGenerator().setRootDirectory(root);
        ti.recursiveDelete(new File(testFolderPath));
    }

    @Test
    public void shouldChangeFileType() throws SQLException {
        Experiment e = dbac.getExperiment(testExpID1);
        FileTuple testTuple = e.getFiles().get(0);
        String originalTypeString = testTuple.type;
        int originalTypeInt = (originalTypeString == "Raw") ? FileTuple.RAW : FileTuple.PROFILE;
        dbac.changeFileType(testTuple.id, FileTuple.REGION);
        testTuple = dbac.getFileTuple(testTuple.id);
        assertTrue(testTuple.type.equals("Region"));
        dbac.changeFileType(testTuple.id, originalTypeInt);
    }

    @Test
    public void shouldChangeMetaData() throws SQLException {
        String newMeta = "-new meta";
        dbac.changeFileMetaData(fileID, newMeta);
        assertTrue(dbac.getFileTuple(fileID).metaData.equals(newMeta));
        dbac.changeFileMetaData(fileID, originalMeta);
    }

    @Test
    public void shouldChangeAuthor() throws SQLException {
        String newAuthor = "Gandalf the grey";
        dbac.changeFileAuthor(fileID, newAuthor);
        assertTrue(dbac.getFileTuple(fileID).author.equals(newAuthor));
        dbac.changeFileAuthor(fileID, originalAuthor);
    }

    @Test
    public void shouldChangeGRVersion() throws SQLException {
        String newGRVersion = "hg19";
        dbac.changeFileGrVersion(fileID, newGRVersion);
        assertTrue(dbac.getFileTuple(fileID).grVersion.equals(newGRVersion));
        dbac.changeFileGrVersion(fileID, originalGRVersion);
    }

    @Test (expected = SQLException.class)
    public void shouldNotChangeInvalidGRVersion() throws SQLException {
        String newGRVersion = "raketost";
        dbac.changeFileGrVersion(fileID, newGRVersion);
        assertTrue(dbac.getFileTuple(fileID).grVersion.equals(newGRVersion));
        dbac.changeFileGrVersion(fileID, originalGRVersion);
    }
}