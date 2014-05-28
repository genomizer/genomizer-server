package database.testSuite.unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import database.FileValidator;


public class FileNameValidatorTest {

	@Test
	public void shouldReturnTrue(){
		assertTrue(FileValidator.fileNameCheck("hej.txt"));
	}

	@Test
	public void sholdReturnTrueWithÅÄÖ(){
		assertTrue(FileValidator.fileNameCheck("håj.töt"));
	}

	@Test
	public void shouldReturnFalse1(){
		assertFalse(FileValidator.fileNameCheck("hej."));
	}

	@Test
	public void shouldReturnFalse2(){
		assertFalse(FileValidator.fileNameCheck("hej.*"));
	}

	@Test
	public void shouldReturnFalse3(){
		assertFalse(FileValidator.fileNameCheck(".&"));
	}

	@Test
	public void shouldReturnTrueWithLongName(){
		assertTrue(FileValidator.fileNameCheck("hej.txtasdfasdfasdfasdfasd.dsfgdgdfg"));

	}
}
