package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;

import database.DatabaseAccessor;
import database.Experiment;

public class MockUserTests {

    static TestInitializer ti;
    static DatabaseAccessor dbac;

    @BeforeClass
    public static void setUpBeforeClass() {
        ti = new TestInitializer();
        try {
            dbac = ti.setupWithoutAddingTuples("genomizer");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ti.removeTuples();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        ti.removeTuplesKeepConnection();
    }

    @Test
    public void addAnExperiment() throws Exception {
        dbac.addExperiment("My First Experiment");
        Experiment e = dbac.getExperiment("My First Experiment");
        assertEquals("My First Experiment", e.getID());
        assertEquals(0, e.getFiles().size());
    }
    
    @Test
    public void searchForEmptyExperiment() throws Exception {
        dbac.addExperiment("Ex1");
        List<Experiment> exs = dbac.search("ex1[expid]");
        assertEquals(1, exs.size());
    }

    @Test (expected = IOException.class)
    public void tryToAddSameExperimentAgain() throws Exception {
        dbac.addExperiment("My First Experiment");
        dbac.addExperiment("my first experiment");
    }

    @Test
    public void addFreeTextAnnotation() throws Exception {
        dbac.addFreeTextAnnotation("Tissue", null, true);

        List<String> annotationLabels = dbac.getAllAnnotationLabels();
        assertTrue(annotationLabels.contains("Tissue"));
    }
    
    @Test (expected = IOException.class)
    public void addDuplicateAnnotation() throws Exception {
        dbac.addFreeTextAnnotation("Tissue", null, true);
        dbac.addFreeTextAnnotation("tissue", null, false);
    }

    @Test
    public void addDropDownAnnotation() throws Exception {

        ArrayList<String> choices = new ArrayList<String>();
        choices.add("male");
        choices.add("female");
        choices.add("unknown");
        
        dbac.addDropDownAnnotation("Sex", choices, 2, false);

        List<String> annotationLabels = dbac.getAllAnnotationLabels();
        assertTrue(annotationLabels.contains("Sex"));
    }
    
    @Test (expected = IOException.class)
    public void addDuplicateDropDownAnnotation() throws Exception {
        ArrayList<String> choices = new ArrayList<String>();
        choices.add("male");
        choices.add("female");
        choices.add("unknown");
        
        dbac.addDropDownAnnotation("Sex", choices, 2, false);
        dbac.addDropDownAnnotation("sex", choices, 0, false);
    }
    
    @Test
    public void annotateExperimentFT() throws Exception {
        dbac.addExperiment("My First Experiment");
        dbac.addFreeTextAnnotation("Tissue", null, true);
        dbac.annotateExperiment("My First Experiment", "Tissue", "Heart");
        Experiment e = dbac.getExperiment("My First Experiment");
        assertEquals("Heart", e.getAnnotations().get("Tissue"));
    }
    
    @Test
    public void annotateExperimentDD() throws Exception {
        if (dbac.getExperiment("My first experiment") == null) {
            dbac.addExperiment("My First Experiment");
        }
        
        ArrayList<String> choices = new ArrayList<String>();
        choices.add("male");
        choices.add("female");
        choices.add("unknown");
        
        dbac.addDropDownAnnotation("Sex", choices, 2, false);
        dbac.annotateExperiment("My First Experiment", "Sex", "Male");
        Experiment e = dbac.getExperiment("My First Experiment");
        assertEquals("male", e.getAnnotations().get("Sex"));
    }
    
    @Test (expected = IOException.class)
    public void annotateExperimentInvalidChoiceDD() throws Exception {
        dbac.addExperiment("My First Experiment");
        
        ArrayList<String> choices = new ArrayList<String>();
        choices.add("male");
        choices.add("female");
        choices.add("unknown");
        
        dbac.addDropDownAnnotation("Sex", choices, 2, false);
        dbac.annotateExperiment("My First Experiment", "Sex", "Alien");
    }
    
    @Test
    public void annotateExperimentFTandDD() throws Exception {
        
        annotateExperimentFT();
        
        annotateExperimentDD();
        
        Experiment e = dbac.getExperiment("My First Experiment");
        assertEquals(2, e.getAnnotations().size());
        assertEquals("male", e.getAnnotations().get("Sex"));
        assertEquals("Heart", e.getAnnotations().get("Tissue"));
    }

}




















