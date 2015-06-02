package database.test.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import database.FileValidator;


public class FileNameValidatorTest {

	@Test
	public void shouldReturnTrue(){
		assertTrue(FileValidator.checkIsValidFileName("hej.txt"));
	}

	@Test
	public void shouldReturnFalse1(){
		assertFalse(FileValidator.checkIsValidFileName("hej./&%&"));
	}

	@Test
	public void shouldReturnFalse2(){
		assertFalse(FileValidator.checkIsValidFileName("hej.*"));
	}

	@Test
	public void shouldReturnFalse3(){
		assertFalse(FileValidator.checkIsValidFileName(".&"));
	}

	@Test
	public void shouldReturnTrueWithLongName(){
		assertTrue(FileValidator.checkIsValidFileName(
						"hej.txtasdfasdfasdfasdfasd.dsfgdgdfg"));

	}
}
