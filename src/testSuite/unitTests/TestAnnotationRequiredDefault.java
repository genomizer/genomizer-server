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
			assertTrue(dbac.isRequierd("Species"));
			assertTrue(dbac.isRequierd("Tissue"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testIsRequierdFalse(){
		try {
			assertFalse(dbac.isRequierd("Sex"));
			assertFalse(dbac.isRequierd("Development Stage"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetDefaultValue(){

		try {
			assertEquals("Human",dbac.getDefaultValue("Species"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testShouldGetNull(){

		try {
			assertNull(dbac.getDefaultValue("Tissue"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

