package database.test.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.containers.Annotation;
import database.test.TestInitializer;

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
    	ArrayList<String> choices = (ArrayList<String>)
    			annotation.getPossibleValues();

    	assertEquals(5, choices.size());
		assertTrue(choices.contains(""));
		assertTrue(choices.contains("Female"));
    	assertTrue(choices.contains("Male"));
    	assertTrue(choices.contains("Unknown"));
    	assertTrue(choices.contains("Does_not_matter"));
	}

    @Test
    public void shouldReturnNullWhenRequestingFreeTextAnnotationsChoices()
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
    public void shouldGetAnnotationsWhenMixingValidWithInvalidLabels()
    		throws Exception {

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

	@Test
	public void shouldGetCorrectAmountOfForcedAnnotationLabels() throws Exception {
		ArrayList<String> labels = dbac.getAllForcedAnnotationLabels();
		assertEquals(labels.size(),2);
	}

	@Test
	public void shouldGetCorrectForcedAnnotationLabels() throws Exception {
		ArrayList<String> labels = dbac.getAllForcedAnnotationLabels();
		assertTrue(labels.contains("Species"));
		assertTrue(labels.contains("Tissue"));
	}
}