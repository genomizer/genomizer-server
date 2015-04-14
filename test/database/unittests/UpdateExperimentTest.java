package database.test.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.containers.Annotation;
import database.containers.Experiment;
import database.test.TestInitializer;

public class UpdateExperimentTest {

    private static DatabaseAccessor dbac;
    private static TestInitializer ti;

    @BeforeClass
    public static void setupBeforeClass() throws Exception {

        ti = new TestInitializer();
        dbac = ti.setup();
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException {

        ti.removeTuples();
    }

    @Test
    public void shouldUpdateExperimentFreetext() throws SQLException,
            IOException {

        String resBefore = localGetExperiment("Exp1", "Tissue");

        assertEquals(1,
                dbac.updateExperiment("Exp1", "Tissue", "Leg"));

        String resAfter = localGetExperiment("Exp1", "Tissue");
        assertFalse(resBefore.equals(resAfter));
    }

    @Test
    public void shouldUpdateExperimentDropDown() throws SQLException,
            IOException {

        String resBefore = localGetExperiment("Exp1", "Sex");
        assertEquals(1, dbac.updateExperiment("Exp1", "Sex", "Male"));

        String resAfter = localGetExperiment("Exp1", "Sex");
        assertFalse(resBefore.equals(resAfter));

        dbac.updateExperiment("Exp1", "Sex", "Unknown");
    }

    @Test
    public void shouldUpdateDDAnnotationDespiteWrongCase()
            throws Exception {

        String resBefore = localGetExperiment("Exp1", "Sex");
        assertEquals("Unknown", resBefore);

        assertEquals(1, dbac.updateExperiment("EXP1", "SEX", "MALE"));

        String resAfter = localGetExperiment("Exp1", "Sex");
        assertEquals("Male", resAfter);

        dbac.updateExperiment("Exp1", "Sex", "Unknown");
    }

    @Test
    public void shouldUpdateFTAnnotationDespiteWrongCase()
            throws Exception {

        dbac.updateExperiment("EXP1", "TISSUE", "UPPER CASE AND lowercase");

        Experiment e = dbac.getExperiment("EXP1");
        Annotation a = dbac.getAnnotationObject("TISSUE");
        String value = e.getAnnotations().get(a.label);

        assertEquals("UPPER CASE AND lowercase", value);
    }

    private String localGetExperiment(String expID, String label)
            throws SQLException {

        Experiment e = dbac.getExperiment(expID);
        HashMap<String, String> hm = e.getAnnotations();
        String res = hm.get(label);

        return res;
    }
}