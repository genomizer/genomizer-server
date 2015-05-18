package database.test.unittests;

import database.DatabaseAccessor;
import database.containers.Experiment;
import database.test.TestInitializer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dv13esn on 2015-05-18.
 */
public class DatabaseAccessorThreadTest2 {

    private static TestInitializer ti;
    private static DatabaseAccessor dbac;
    private static String expID;

    @BeforeClass
    public static void setupBeforeClass() throws Exception {
        ti = new TestInitializer();
        dbac = ti.setup();
        expID = "Expname";
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ti.removeTuples();
        dbac.close();
    }

    @Test
    public void shouldAddExperiment() throws Exception {
        dbac.addExperiment(expID);
        List<Experiment> experimentList = dbac.search(expID + "[ExpID]");
        int actualSize = experimentList.size();
        int expectedSize = 1;
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void shouldAddFile() throws Exception {
        dbac.
    }
}