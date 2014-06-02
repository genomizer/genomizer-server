package database.testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import database.DatabaseAccessor;
import database.FilePathGenerator;
import database.containers.Experiment;
import database.containers.FileTuple;
import database.testSuite.TestInitializer;

public class AddNewFileTests {

    private static DatabaseAccessor dbac;

    private static FilePathGenerator fpg;

    private static String testFileName = "testFileName1.txt";
    private static int testFileType = FileTuple.RAW;
    private static String testAuthor = "test File Author1";
    private static String testUploader = "test Uploader 1";
    private static String testMetaData = "test Meta Data 1";
    private static String testInputFileName = "testInputFileName.txt";
    private static boolean testIsPrivate = false;
    private static String testExpId = "testExpId1";
    private static TestInitializer ti;

    private static FileTuple ft;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    	ti = new TestInitializer();
    	dbac = ti.setup();

        fpg = new FilePathGenerator(DatabaseAccessor.DATAFOLDER);
        dbac.addExperiment(testExpId);
        ft = dbac.addNewFile(testExpId, testFileType, testFileName,
                testInputFileName, testMetaData, testAuthor, testUploader,
                testIsPrivate, null);

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    	ti.removeTuples();
        dbac.close();

    }

    /**
     * Tests that file is in database as expected after setup() method. Deletes
     * file and tests that it no longer is in the database. Adds file again and
     * tests that it is in database again.
     *
     * @throws Exception
     */
    @Test
    public void testGetDeleteGetAddGetFile() throws Exception {

        String expectedFilePath = fpg.generateFilePath(testExpId,
                testFileType, testFileName);
        System.out.println(expectedFilePath);

        FileTuple ft = dbac.getFileTuple(expectedFilePath);

        assertEquals(expectedFilePath, ft.path);

        Experiment e = dbac.getExperiment(testExpId);
        ft = e.getFiles().get(0);
        assertEquals(expectedFilePath, ft.path);

        dbac.deleteFile(ft.id);
        assertNull(dbac.getFileTuple(expectedFilePath));
        e = dbac.getExperiment(testExpId);
        assertEquals(0, e.getFiles().size());

        ft = dbac.addNewFile(testExpId, testFileType, testFileName,
                testInputFileName, testMetaData, testAuthor, testUploader,
                testIsPrivate, null);
        ft = dbac.getFileTuple(expectedFilePath);
        assertEquals(expectedFilePath, ft.path);

        e = dbac.getExperiment(testExpId);
        ft = e.getFiles().get(0);
        assertEquals(expectedFilePath, ft.path);
    }

    @Test
    public void shouldContainRightAttributes() throws Exception {
        String expectedFilePath = fpg.generateFilePath(testExpId,
                testFileType, testFileName);

        String expectedInputFilePath = fpg.generateFilePath(testExpId,
                testFileType, testInputFileName);

        FileTuple ft = dbac.getFileTuple(expectedFilePath);

        assertEquals(expectedFilePath, ft.path);
        assertEquals(testExpId, ft.expId);
        assertEquals("Raw", ft.type);
        assertEquals(testFileName, ft.filename);
        assertEquals(expectedInputFilePath, ft.inputFilePath);
        assertEquals(testMetaData, ft.metaData);
        assertEquals(testAuthor, ft.author);
        assertEquals(testUploader, ft.uploader);
    }

}
