package database.test.unittests;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import database.DatabaseAccessor;
import database.containers.Experiment;
import database.containers.FileTuple;
import database.test.TestInitializer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.List;

public class FileTableTests {


    private static TestInitializer ti;
    private static DatabaseAccessor dbac;
    private String testExpID1 = "Exp1";

    @BeforeClass
    public static void setupTestCase() throws Exception {
        ti = new TestInitializer();
        dbac = ti.setup();
    }


    @AfterClass
    public static void undoAllChanges() throws Exception {
        ti.removeTuples();
    }


    @Before
    public void setup() throws Exception {

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

        ti.removeTuples();
        ti.addTuples();
    }

    /*
    @Test
    public void shouldAddFile() throws Exception {
        Experiment e = dbac.getExperiment(testExpID1);

        // TODO Fix this test
        // Already tested in AddNewFileTests.java
    }
    */
    // TODO Rewrite the rest of the tests

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

        ti.removeTuples();
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

    

}