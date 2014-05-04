package testSuite.unitTests;

import static org.junit.Assert.*;

import javax.swing.filechooser.FileSystemView;

import org.junit.*;

import database.FilePathGenerator;

public class TestFilePathGEN {

	@Test
	public void getCatFilePath(){
		String generatedPath = FilePathGenerator.GenerateFilePath("CatTest", "raw", "CatFacts.txt");
		String expectedPath = FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "/CatTest/raw/CatFacts.txt";
		assertEquals(expectedPath, generatedPath);
	}
}