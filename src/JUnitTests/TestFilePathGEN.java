package JUnitTests;

import static org.junit.Assert.*;

import org.junit.*;

import database.FilePathGenerator;

public class TestFilePathGEN {

	@Test
	public void getCatFilePath(){
		System.out.println(FilePathGenerator.GenerateFilePath("CatTest", "raw", "CatFacts.txt"));
		assertEquals("/home/dv12/dv12can/CatTest/raw/CatFacts.txt",FilePathGenerator.GenerateFilePath("CatTest", "raw", "CatFacts.txt"));
	}
}