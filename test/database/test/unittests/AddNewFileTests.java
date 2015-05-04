package database.test.unittests;

import static org.junit.Assert.*;


import database.containers.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


import database.DatabaseAccessor;
import database.FilePathGenerator;
import database.test.TestInitializer;

public class AddNewFileTests {

    private static DatabaseAccessor dbac;

    private static FilePathGenerator fpg;

    private static String testFileName = "testFileName1.txt";
    private static FileTuple.Type testFileType = AbstractFileTuple.Type.Raw;
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
        ft = dbac.addNewFile(testExpId, testFileType.val, testFileName,
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
    @Ignore
    public void testGetDeleteGetAddGetFileFileUsingTuples() throws Exception {

        String expectedFilePath = fpg.generateFilePath(testExpId,
                testFileType, testFileName);
        System.out.println(expectedFilePath);

        FileTuple ft = dbac.getFileTuple(expectedFilePath);

        assertEquals(expectedFilePath, ft.getFullPath());

        Experiment e = dbac.getExperiment(testExpId);
        ft = e.getFiles().get(0);
        assertEquals(expectedFilePath, ft.getFullPath());

        dbac.deleteFile(ft.getFileId());
        assertNull(dbac.getFileTuple(expectedFilePath));
        e = dbac.getExperiment(testExpId);
        assertEquals(0, e.getFiles().size());

        FileTupleTemplate ftt = (new FileTupleTemplateBuilder()).fromType(testFileType)
                .withExpId(testExpId)
                .withAuthor(testAuthor)
                .withMetaData(testMetaData)
                .withIsPrivate(testIsPrivate)
                .build();
        //dbac.addNewFile(ftt,testFileName,testInputFileName,testUploader);
        ft = dbac.getFileTuple(expectedFilePath);
        assertEquals(expectedFilePath, ft.getFullPath());

        e = dbac.getExperiment(testExpId);
        ft = e.getFiles().get(0);
        assertEquals(expectedFilePath, ft.getFullPath());
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

        assertEquals(expectedFilePath, ft.getFullPath());

        Experiment e = dbac.getExperiment(testExpId);
        ft = e.getFiles().get(0);
        assertEquals(expectedFilePath, ft.getFullPath());

        dbac.deleteFile(ft.getFileId());
        assertNull(dbac.getFileTuple(expectedFilePath));
        e = dbac.getExperiment(testExpId);
        assertEquals(0, e.getFiles().size());

        ft = dbac.addNewFile(testExpId, testFileType.val, testFileName,
                testInputFileName, testMetaData, testAuthor, testUploader,
                testIsPrivate, null, null);
        ft = dbac.getFileTuple(expectedFilePath);
        assertEquals(expectedFilePath, ft.getFullPath());

        e = dbac.getExperiment(testExpId);
        ft = e.getFiles().get(0);
        assertEquals(expectedFilePath, ft.getFullPath());
    }

    @Test
    public void shouldContainRightAttributes() throws Exception {
        String expectedFilePath = fpg.generateFilePath(testExpId,
                testFileType, testFileName);

        String expectedInputFilePath = fpg.generateFilePath(testExpId,
                testFileType, testInputFileName);

        FileTuple ft = dbac.getFileTuple(expectedFilePath);

        assertEquals(expectedFilePath, ft.getFullPath());
        assertEquals(testExpId, ft.getExpId());
        assertEquals("Raw", ft.getType().name());
        assertEquals(testFileName, ft.getFileName());
        assertEquals(expectedInputFilePath, ft.getInputFilePath());
        assertEquals(testMetaData, ft.getMetaData());
        assertEquals(testAuthor, ft.getAuthor());
        assertEquals(testUploader, ft.getUploader());
    }

}
