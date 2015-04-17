package database.test.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import database.FileValidator;


public class FileNameValidatorTest {

	@Test
	public void shouldReturnTrue(){
		assertTrue(FileValidator.fileNameCheck("hej.txt"));
	}

	// TODO: Re-enable. Encoding should probably be changed to utf-8.
	@Ignore
	@Test
	public void sholdReturnTrueWithSpecialSymbols(){
		assertTrue(FileValidator.fileNameCheck("h�j.t�t"));
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
