package database.test.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        HashMap<String, String> annotations = new HashMap<>();
        annotations.put("Tissue", "Leg");
        assertEquals(1,
                dbac.updateExperiment("Exp1", annotations));

        String resAfter = localGetExperiment("Exp1", "Tissue");
        assertFalse(resBefore.equals(resAfter));
    }

    @Test
    public void shouldUpdateExperimentDropDown() throws SQLException,
            IOException {

        String resBefore = localGetExperiment("Exp1", "Sex");
        HashMap<String, String> annotations = new HashMap<>();
        annotations.put("Sex", "Male");
        assertEquals(1, dbac.updateExperiment("Exp1", annotations));

        String resAfter = localGetExperiment("Exp1", "Sex");
        assertFalse(resBefore.equals(resAfter));

        annotations.put("Sex", "Unknown");
        dbac.updateExperiment("Exp1", annotations);
    }

    @Test
    public void shouldUpdateDDAnnotationDespiteWrongCase()
            throws Exception {

        String resBefore = localGetExperiment("Exp1", "Sex");
        assertEquals("Unknown", resBefore);

        HashMap<String, String> annotations = new HashMap<>();
        annotations.put("SEX", "MALE");
        assertEquals(1, dbac.updateExperiment("EXP1", annotations));

        String resAfter = localGetExperiment("Exp1", "Sex");
        assertEquals("Male", resAfter);

        annotations.put("Sex", "Unknown");
        dbac.updateExperiment("Exp1", annotations);
    }

    @Test
    public void shouldUpdateFTAnnotationDespiteWrongCase()
            throws Exception {

        HashMap<String, String> annotations = new HashMap<>();
        annotations.put("TISSUE", "UPPER CASE AND lowercase");
        dbac.updateExperiment("EXP1", annotations);

        Experiment e = dbac.getExperiment("EXP1");
        Annotation a = dbac.getAnnotationObject("TISSUE");
        String value = e.getAnnotations().get(a.label);

        assertEquals("UPPER CASE AND lowercase", value);
        annotations.put("TISSUE", "Arm");
        dbac.updateExperiment("EXP1", annotations);
    }

    @Test
    public void shouldUpdateSingleAnnotation() throws Exception {

        String exp1 = "Exp1";

        HashMap<String, String> orig = dbac.getExperiment(exp1).getAnnotations();
        HashMap<String, String> kv = new HashMap<>();

        String rat = "Rat";
        kv.put("Species", rat);
        dbac.updateExperiment(exp1, kv);
        Experiment e = dbac.getExperiment(exp1);
        assertTrue(e.getAnnotations().get("Species").equals(rat));

        dbac.updateExperiment(exp1, orig);
    }

    @Test
    public void shouldUpdateLotsOfAnnotations() throws Exception {

        String exp1 = "Exp1";

        HashMap<String, String> orig = dbac.getExperiment(exp1).getAnnotations();
        HashMap<String, String> kv = new HashMap<>();

        String rat = "Rat";
        String male = "Male";
        String hair = "Hair";
        String child = "Child";

        kv.put("Species", rat);
        kv.put("Sex", male);
        kv.put("Tissue", hair);
        kv.put("Development_Stage", child);

        dbac.updateExperiment(exp1, kv);
        Experiment e = dbac.getExperiment(exp1);

        assertTrue(e.getAnnotations().get("Species").equals(rat));
        assertTrue(e.getAnnotations().get("Sex").equals(male));
        assertTrue(e.getAnnotations().get("Tissue").equals(hair));
        assertTrue(e.getAnnotations().get("Development_Stage").equals(child));

        dbac.updateExperiment(exp1, orig);
    }

    @Test
    public void shouldAddAnnotationWhenNotPresentOnMultipleUpdate() throws Exception {

        String exp3 = "Exp3";

        HashMap<String, String> orig = dbac.getExperiment(exp3).getAnnotations();
        HashMap<String, String> kv = new HashMap<>();

        String species = "Human";
        String sex = "Male";
        String devStage = "Adult";

        kv.put("Species", species);
        kv.put("Sex", sex);
        kv.put("Development_Stage", devStage);

        dbac.updateExperiment(exp3, kv);
        Experiment e = dbac.getExperiment(exp3);

        assertTrue(e.getAnnotations().get("Species").equals(species));
        assertTrue(e.getAnnotations().get("Sex").equals(sex));
        assertTrue(e.getAnnotations().get("Development_Stage").equals(devStage));

        dbac.updateExperiment(exp3, orig);
    }

    @Test
    public void shouldNotUpdateNonExistentAnnotations() throws Exception {

        String exp1 = "Exp1";

        HashMap<String, String> orig = dbac.getExperiment(exp1).getAnnotations();
        HashMap<String, String> kv = new HashMap<>();

        String rat = "Rat";
        String male = "Male";
        String hair = "Hair";
        String nonexistent = "Rat";

        kv.put("Species", rat);
        kv.put("Sex", male);
        kv.put("Tissue", hair);
        kv.put("Species2", nonexistent);
        try {
            dbac.updateExperiment(exp1, kv);
        } catch (IOException ignored) { }

        Experiment e = dbac.getExperiment(exp1);

        assertTrue(e.getAnnotations().get("Species").equals("Human"));
        assertTrue(e.getAnnotations().get("Sex").equals("Unknown"));
        assertTrue(e.getAnnotations().get("Tissue").equals("Arm"));
        assertTrue(e.getAnnotations().get("Development_Stage").equals("Adult"));
        dbac.updateExperiment(exp1, orig);
    }

    @Test (expected = IOException.class)
    public void shouldThrowUpdateWhenInvalidAnnotationValue() throws Exception {

        String exp1 = "Exp1";
        HashMap<String, String> kv = new HashMap<>();

        String rat = "Rat";
        String male = "Male";
        String hair = "Hair";

        kv.put("Species", rat);
        kv.put("Sex", male);
        kv.put("Tissue", hair);
        kv.put("Development_Stage", null);

        dbac.updateExperiment(exp1, kv);
    }

    @Test
    public void shouldNotUpdateOthersWhenInvalidAnnotationValue() throws Exception {

        String exp1 = "Exp1";

        HashMap<String, String> orig = dbac.getExperiment(exp1).getAnnotations();
        HashMap<String, String> kv = new HashMap<String, String>();

        String rat = "Rat";
        String male = "Male";
        String hair = "Hair";

        kv.put("Species", rat);
        kv.put("Sex", male);
        kv.put("Tissue", hair);
        kv.put("Development_Stage", null);

        try {
            dbac.updateExperiment(exp1, kv);
        } catch (IOException ignored) {}
        Experiment e = dbac.getExperiment(exp1);

        assertFalse(e.getAnnotations().get("Species").equals(rat));
        assertFalse(e.getAnnotations().get("Sex").equals(male));
        assertFalse(e.getAnnotations().get("Tissue").equals(hair));
        assertFalse(e.getAnnotations().get("Development_Stage").equals(null));

        dbac.updateExperiment(exp1, orig);
    }

    @Test
    public void shouldNotUpdateNonExistentMultichoice() throws Exception {

        String exp1 = "Exp1";

        HashMap<String, String> orig = dbac.getExperiment(exp1).getAnnotations();
        HashMap<String, String> kv = new HashMap<>();

        String alien = "Alien";
        String male = "Male";
        String hair = "Hair";
        String nonexistent = "Rat";

        kv.put("Species", alien);
        kv.put("Sex", male);
        kv.put("Tissue", hair);
        kv.put("Species2", nonexistent);
        try {
            dbac.updateExperiment(exp1, kv);
        } catch (IOException ignored) { }

        Experiment e = dbac.getExperiment(exp1);

        assertTrue(e.getAnnotations().get("Species").equals("Human"));
        assertTrue(e.getAnnotations().get("Sex").equals("Unknown"));
        assertTrue(e.getAnnotations().get("Tissue").equals("Arm"));
        assertTrue(e.getAnnotations().get("Development_Stage").equals("Adult"));
        dbac.updateExperiment(exp1, orig);
    }

    @Test
    public void shouldReturnCorrectAmountOfUpdates() throws Exception {

        String exp1 = "Exp1";

        HashMap<String, String> orig = dbac.getExperiment(exp1).getAnnotations();
        HashMap<String, String> kv = new HashMap<>();

        String human = "Human";
        String male = "Male";
        String hair = "Hair";
        String adult = "Adult";

        kv.put("Species", human);
        kv.put("Sex", male);
        kv.put("Tissue", hair);
        kv.put("Development_Stage", adult);


        int res = dbac.updateExperiment(exp1, kv);
        assertTrue(res == 4);

        dbac.updateExperiment(exp1, orig);
    }



    private String localGetExperiment(String expID, String label)
            throws SQLException {

        Experiment e = dbac.getExperiment(expID);
        HashMap<String, String> hm = e.getAnnotations();

        return hm.get(label);
    }
}