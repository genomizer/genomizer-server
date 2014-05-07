package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

import org.junit.*;

import database.FilePathGenerator;

public class TestFilePathGEN {

	@Test
	public void getCatFilePath() {
		String generatedPath = FilePathGenerator.GenerateFilePath("CatTest",
				"raw", "CatFacts.txt");
		String expectedPath ="/var/www/data/CatTest/raw/CatFacts.txt";

		System.out.println(expectedPath);
		System.out.println(generatedPath);

		assertEquals(expectedPath, generatedPath);
	}

	/*
	 * @Test public void hasDataRoute(){
	 *
	 * File f = new
	 * File(FileSystemView.getFileSystemView().getHomeDirectory().getPath
	 * ()+"/data/hej/vad/g�r/du/");
	 *
	 * System.out.println(FileSystemView.getFileSystemView().getHomeDirectory().
	 * getPath());
	 *
	 * f.mkdirs();
	 *
	 * assertTrue(f.exists()); assertTrue(f.isDirectory());
	 *
	 *
	 * }
	 */

//	@Test
//	public void testExperimentFolders() {
//
//		String expID = "kebab";
//
//		FilePathGenerator.GenerateExperimentFolders(expID);
//
//		File f = new File(FileSystemView.getFileSystemView().getHomeDirectory()
//				.getPath()
//				+ "/data/" + expID + "/raw/");
//
//		assertTrue(f.exists());
//		assertTrue(f.isDirectory());
//
//		f = new File(FileSystemView.getFileSystemView().getHomeDirectory()
//				.getPath()
//				+ "/data/" + expID + "/profile/");
//
//		assertTrue(f.exists());
//		assertTrue(f.isDirectory());
//
//		f = new File(FileSystemView.getFileSystemView().getHomeDirectory()
//				.getPath()
//				+ "/data/" + expID + "/region/");
//
//		assertTrue(f.exists());
//		assertTrue(f.isDirectory());
//	}

	@Test
	public void testGeneratePathForGenomeFiles(){

		String version = "F1.3";
		String specie = "fly";
		String expectedPath = "/var/www/data/genome_releases/fly/F1.3";

		String generatedPath = FilePathGenerator.GeneratePathForGenomeFiles(
											version,specie);

		System.out.println(expectedPath);
		System.out.println(generatedPath);

		assertEquals(expectedPath, generatedPath);
	}

	@Test
	public void testGenerateGenomeReleaseFolders() {

		String specie = "cat";

		FilePathGenerator.GenerateGenomeReleaseFolders(specie);

		File f = new File(FileSystemView.getFileSystemView().getHomeDirectory()
				.getPath()
				+ "/data/genome_releases/" + specie);

		assertTrue(f.exists());
		assertTrue(f.isDirectory());
	}

}