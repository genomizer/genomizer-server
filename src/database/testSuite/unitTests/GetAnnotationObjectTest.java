package database.testSuite.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import database.Annotation;
import database.DatabaseAccessor;
import database.testSuite.TestInitializer;

public class GetAnnotationObjectTest {

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
    public void shouldBeAbleToGetAllChoicesForDropDownAnnotations()
    		throws Exception {

    	String label = "Sex";
    	Annotation annotation = dbac.getAnnotationObject(label);
    	ArrayList<String> choices = (ArrayList<String>) annotation.getPossibleValues();

    	assertEquals(4, choices.size());
    	assertTrue(choices.contains("Female"));
    	assertTrue(choices.contains("Male"));
    	assertTrue(choices.contains("Unknown"));
    	assertTrue(choices.contains("Does not matter"));
    	
	}

    @Test
    public void shouldReturnNullWhenRequestingListWithChoicesForFreeTextAnnotations()
    		throws Exception {

    	String label = "Tissue";
    	Annotation annotation = dbac.getAnnotationObject(label);

    	assertTrue(annotation.getPossibleValues() == null);
	}

    @Test
    public void shouldStoreCorrectLabel() throws Exception {
    	String label = "Sex";
    	Annotation annotation = dbac.getAnnotationObject(label);

    	assertEquals(label, annotation.label);
	}

    @Test
    public void shouldStoreCorrectDataType() throws Exception {
    	String label = "Sex";
    	Annotation annotation = dbac.getAnnotationObject(label);

    	assertTrue(Annotation.DROPDOWN == annotation.dataType);
	}

    @Test
    public void shouldStoreCorrectRequiredFlag() throws Exception {
    	String label = "Sex";
    	Annotation annotation = dbac.getAnnotationObject(label);

    	assertTrue(annotation.isRequired == false);
	}

    @Test
    public void shouldReturnNullIfLabelDoesNotExist() throws Exception {
    	String label = "r23f9hyf293y";
    	Annotation annotation = dbac.getAnnotationObject(label);

    	assertTrue(annotation == null);
	}

    @Test
    public void shouldBeAbleToGetMultipleAnnotations() throws Exception {
    	List<String> labels = new ArrayList<String>();
    	labels.add("Sex");
    	labels.add("Tissue");
    	List<Annotation> annotations = dbac.getAnnotationObjects(labels);

    	assertEquals("Sex", annotations.get(0).label);
    	assertEquals("Tissue", annotations.get(1).label);
	}

    @Test
    public void shouldGetNullWhenSendingInEmptyList() throws Exception {
    	List<String> labels = new ArrayList<String>();
    	List<Annotation> annotations = dbac.getAnnotationObjects(labels);

    	assertEquals(null, annotations);
	}

    @Test
    public void shouldGetAnnotationsWhenMixingValidWithInvalidLabels() throws Exception {
    	List<String> labels = new ArrayList<String>();
    	labels.add("invlaidLabel");
    	labels.add("Sex");
    	labels.add("anotherInvalidLabel");
    	labels.add("Tissue");
    	List<Annotation> annotations = dbac.getAnnotationObjects(labels);

    	assertTrue(annotations.size() == 2);
    	assertEquals("Sex", annotations.get(0).label);
    	assertEquals("Tissue", annotations.get(1).label);
	}
}
