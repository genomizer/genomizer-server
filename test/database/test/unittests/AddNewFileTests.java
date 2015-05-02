package database.test.unittests;

import static org.junit.Assert.*;


import database.containers.FileTupleBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


import database.DatabaseAccessor;
import database.FilePathGenerator;
import database.containers.Experiment;
import database.containers.FileTuple;
import database.test.TestInitializer;

import java.io.File;

public class AddNewFileTests {

    private static DatabaseAccessor dbac;

    private static FilePathGenerator fpg;

    private static String testFileName = "testFileName1.txt";
    private static int testFileType = FileTuple.RAW;
    private static String testFileTypeS = "Raw";
    private static String testAuthor = "test File Author1";
    private static String testUploader = "test Uploader 1";
    private static String testMetaData = "test Meta Data 1";
    private static String testInputFileName = "testInputFileName.txt";
    private static boolean testIsPrivate = false;
    private static String testExpId = "testExpId1";
    private static TestInitializer ti;

    static FileTuple ft;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    	ti = new TestInitializer();
    	dbac = ti.setup();

        fpg = new FilePathGenerator(DatabaseAccessor.DATAFOLDER);
        dbac.addExperiment(testExpId);
        ft = dbac.addNewFile(testExpId, testFileType, testFileName,
                testInputFileName, testMetaData, testAuthor, testUploader,
                testIsPrivate, null, null);

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    	ti.removeTuples();
        dbac.close();

    }

    /**
     * Very basic test to ensure that our filetuple construction maskes sense.
     * @throws Exception
     */
    @Test
    public void testGetDeleteGetAddGetFileFileUsingTuples() throws Exception {

        String expectedFilePath = fpg.generateFilePath(testExpId,
                testFileType, testFileName);
        System.out.println(expectedFilePath);

        FileTuple ft = dbac.getFileTuple(expectedFilePath);

        assertEquals(expectedFilePath, ft.getPath());

        Experiment e = dbac.getExperiment(testExpId);
        ft = e.getFiles().get(0);
        assertEquals(expectedFilePath, ft.getPath());

        dbac.deleteFile(ft.getId());
        assertNull(dbac.getFileTuple(expectedFilePath));
        e = dbac.getExperiment(testExpId);
        assertEquals(0, e.getFiles().size());

        FileTuple ftt = (new FileTupleBuilder()).fileTuple()
                .withExpId(testExpId)
                .withAuthor(testAuthor)
                .withInputFilePath(ft.getParentFolder() + testInputFileName)
                .withMetaData(testMetaData)
                .withFilename(testFileName)
                .withUploader(testUploader)
                .withType(testFileTypeS)
                .isPrivate(testIsPrivate)
                .withPath(ft.getParentFolder())
                .build();
        dbac.addNewFile(ftt);
        ft = dbac.getFileTuple(expectedFilePath);
        assertEquals(expectedFilePath, ft.getPath());

        e = dbac.getExperiment(testExpId);
        ft = e.getFiles().get(0);
        assertEquals(expectedFilePath, ft.getPath());
    }


    /**
     * Tests that file is in database as expected after setup() method. Deletes
     * file and tests that it no longer is in the database. Adds file again and
     * tests that it is in database again.
     *
     * @throws Exception
     */
    @Test
    public void testGetDeleteGetAddGet() throws Exception{
        String expectedFilePath = fpg.generateFilePath(testExpId,
                testFileType, testFileName);
        System.out.println(expectedFilePath);

        FileTuple ft = dbac.getFileTuple(expectedFilePath);

        assertEquals(expectedFilePath, ft.getPath());

        Experiment e = dbac.getExperiment(testExpId);
        ft = e.getFiles().get(0);
        assertEquals(expectedFilePath, ft.getPath());

        dbac.deleteFile(ft.getId());
        assertNull(dbac.getFileTuple(expectedFilePath));
        e = dbac.getExperiment(testExpId);
        assertEquals(0, e.getFiles().size());

        ft = dbac.addNewFile(testExpId, testFileType, testFileName,
                testInputFileName, testMetaData, testAuthor, testUploader,
                testIsPrivate, null, null);
        ft = dbac.getFileTuple(expectedFilePath);
        assertEquals(expectedFilePath, ft.getPath());

        e = dbac.getExperiment(testExpId);
        ft = e.getFiles().get(0);
        assertEquals(expectedFilePath, ft.getPath());
    }

    @Test
    public void shouldContainRightAttributes() throws Exception {
        String expectedFilePath = fpg.generateFilePath(testExpId,
                testFileType, testFileName);

        String expectedInputFilePath = fpg.generateFilePath(testExpId,
                testFileType, testInputFileName);

        FileTuple ft = dbac.getFileTuple(expectedFilePath);

        assertEquals(expectedFilePath, ft.getPath());
        assertEquals(testExpId, ft.getExpId());
        assertEquals("Raw", ft.getType());
        assertEquals(testFileName, ft.getFilename());
        assertEquals(expectedInputFilePath, ft.getInputFilePath());
        assertEquals(testMetaData, ft.getMetaData());
        assertEquals(testAuthor, ft.getAuthor());
        assertEquals(testUploader, ft.getUploader());
    }

}
