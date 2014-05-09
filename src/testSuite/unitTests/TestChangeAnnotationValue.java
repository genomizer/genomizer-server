package testSuite.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;
import database.Experiment;

public class TestChangeAnnotationValue {

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
    public void shouldChangeAnnotationValueForAnnotatedWith() throws Exception {

    	Experiment exp1 = dbac.getExperiment("Exp1");

    	String label = "Sex";
    	String newValue = "Monkey";
    	String oldValue = exp1.getAnnotations().get(label);


    	dbac.changeAnnotationValue(label, oldValue, newValue);

    	exp1 = dbac.getExperiment("Exp1");
    	assertEquals(exp1.getAnnotations().get(label), newValue);
    	assertFalse(exp1.getAnnotations().get(label).equals(oldValue));

    }

    @Test
    public void shouldChangeAnnotationValueForAnnotationChoices() throws Exception {
    	Experiment exp1 = dbac.getExperiment("Exp1");

    	String label = "Sex";
    	String newValue = "Monkey2";
    	String oldValue = exp1.getAnnotations().get(label);

    	dbac.changeAnnotationValue(label, oldValue, newValue);

    	ArrayList<String> choices = (ArrayList<String>) dbac.getChoices(label);
    	assertTrue(choices.contains(newValue));
    	assertFalse(choices.contains(oldValue));
	}

    @Test
    public void shouldChangeAnnotationValueForAnnotationTable() throws Exception {
    	Experiment exp1 = dbac.getExperiment("Exp1");

    	String label = "Sex";
    	String newValue = "Monkey3";
    	String oldValue = exp1.getAnnotations().get(label);

    	dbac.changeAnnotationValue(label, oldValue, newValue);

    	assertEquals(dbac.getDefaultAnnotationValue(label), newValue);
	}

    @Test
    public void shouldBeAbleToChangeFreeTextValue() throws Exception {
    	Experiment exp1 = dbac.getExperiment("Exp1");

    	String label = "Tissue";
    	String newValue = "Monkey4";
    	String oldValue = exp1.getAnnotations().get(label);

    	dbac.changeAnnotationValue(label, oldValue, newValue);

    	exp1 = dbac.getExperiment("Exp1");
    	assertEquals(exp1.getAnnotations().get(label), newValue);
    	assertFalse(exp1.getAnnotations().get(label).equals(oldValue));
	}

    @Test(expected = SQLException.class)
    public void shouldThrowAnExceptionWhenValueAlreadyExistsInChoices() throws Exception {
    	Experiment exp1 = dbac.getExperiment("Exp1");

    	String label = "Sex";
    	String newValue = "Female";
    	String oldValue = exp1.getAnnotations().get(label);

    	dbac.changeAnnotationValue(label, oldValue, newValue);
	}
}
