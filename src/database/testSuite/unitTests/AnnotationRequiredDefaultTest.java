package database.testSuite.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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

public class AnnotationRequiredDefaultTest {

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
	public void testIsRequierdTrue(){
		try {
			assertTrue(dbac.isAnnotationRequiered("Species"));
			assertTrue(dbac.isAnnotationRequiered("Tissue"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testIsRequierdFalse(){
		try {
			assertFalse(dbac.isAnnotationRequiered("Sex"));
			assertFalse(dbac.isAnnotationRequiered("Development Stage"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetDefaultValue(){

		try {
			assertEquals("Unknown",dbac.getDefaultAnnotationValue("Sex"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testShouldGetNull(){

		try {
			assertNull(dbac.getDefaultAnnotationValue("Tissue"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void shouldHaveRightRequiredFiledAfterFreeTextAddition() throws Exception {
        dbac.addFreeTextAnnotation("Colour", "Green", true);
        
        assertTrue(dbac.isAnnotationRequiered("Colour"));
        
        Annotation a = dbac.getAnnotationObject("Colour");
        assertTrue(a.isRequired);
        

        dbac.addFreeTextAnnotation("Viscosity", "Thick", false);
        
        assertFalse(dbac.isAnnotationRequiered("Viscosity"));
        
        a = dbac.getAnnotationObject("Viscosity");
        assertFalse(a.isRequired);

    }

	@Test
    public void shouldHaveRightRequiredFiledAfterDropDownAddition() throws Exception {

	    List<String> choices = new ArrayList<String>();
	    choices.add("10cm");
	    choices.add("20cm");

        dbac.addDropDownAnnotation("Height", choices, 0, true);
        
        assertTrue(dbac.isAnnotationRequiered("Height"));
        
        Annotation a = dbac.getAnnotationObject("Height");
        assertTrue(a.isRequired);

        dbac.addDropDownAnnotation("Width", choices, 0, false);
        
        assertFalse(dbac.isAnnotationRequiered("Width"));
        
        a = dbac.getAnnotationObject("Width");
        assertFalse(a.isRequired);
        
        List<String> labels = new ArrayList<String>();
        labels.add("Height");
        labels.add("Width");
        
        List<Annotation> as = dbac.getAnnotationObjects(labels);
        assertEquals(2, as.size());
        
        a = as.get(0);
        
        if (a.label.equals("Height")) {
            assertTrue(a.isRequired);
        } else {
            assertFalse(a.isRequired);
        }
        
        a = as.get(1);
        
        if (a.label.equals("Height")) {
            assertTrue(a.isRequired);
        } else {
            assertFalse(a.isRequired);
        }
    }


}

