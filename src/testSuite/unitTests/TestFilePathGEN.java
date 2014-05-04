package testSuite.unitTests;

import static org.junit.Assert.*;

import javax.swing.filechooser.FileSystemView;

import org.junit.*;

import database.FilePathGenerator;

public class TestFilePathGEN {

	@Test
	public void getCatFilePath(){
		System.out.println(FilePathGenerator.GenerateFilePath("CatTest", "raw", "CatFacts.txt"));
		assertEquals(FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "/CatTest/raw/CatFacts.txt",FilePathGenerator.GenerateFilePath("CatTest", "raw", "CatFacts.txt"));
	}
}