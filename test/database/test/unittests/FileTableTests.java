package database.test.unittests;

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

    @Test
    public void shouldAddFile() throws Exception {
        Experiment e = dbac.getExperiment(testExpID1);
        // TODO Fix this test
    }

    // TODO Rewrite the rest of the tests
}