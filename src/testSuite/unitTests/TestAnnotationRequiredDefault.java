package testSuite.unitTests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;

public class TestAnnotationRequiredDefault {

	private static DatabaseAccessor dbac;

	@BeforeClass
	public static void setUp(){
		try {
			dbac = new DatabaseAccessor("c5dv151_vt14", "shielohh", "postgres", "c5dv151_vt14");
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
			assertEquals("Human",dbac.getDefaultAnnotationValue("Species"));
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
}

