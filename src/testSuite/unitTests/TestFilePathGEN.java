package testSuite.unitTests;

import static org.junit.Assert.*;

import javax.swing.filechooser.FileSystemView;

import org.junit.*;

import database.FilePathGenerator;

public class TestFilePathGEN {

	@Test
<<<<<<< HEAD
	public void getCatFilePath(){
		String generatedPath = FilePathGenerator.GenerateFilePath("CatTest", "raw", "CatFacts.txt");
		String expectedPath = FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "/CatTest/raw/CatFacts.txt";
=======
	public void getCatFilePath() {
		String generatedPath = FilePathGenerator.GenerateFilePath("CatTest",
				"raw", "CatFacts.txt");
		String expectedPath ="/var/www/data/CatTest/raw/CatFacts.txt";

		System.out.println(expectedPath);
		System.out.println(generatedPath);

>>>>>>> refs/remotes/origin/development
		assertEquals(expectedPath, generatedPath);
	}
<<<<<<< HEAD
=======

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

	@Test
	public void testExperimentFolders() {

		String expID = "kebab";

		FilePathGenerator.GenerateExperimentFolders(expID);

		File f = new File(FileSystemView.getFileSystemView().getHomeDirectory()
				.getPath()
				+ "/data/" + expID + "/raw/");

		assertTrue(f.exists());
		assertTrue(f.isDirectory());

		f = new File(FileSystemView.getFileSystemView().getHomeDirectory()
				.getPath()
				+ "/data/" + expID + "/profile/");

		assertTrue(f.exists());
		assertTrue(f.isDirectory());

		f = new File(FileSystemView.getFileSystemView().getHomeDirectory()
				.getPath()
				+ "/data/" + expID + "/region/");

		assertTrue(f.exists());
		assertTrue(f.isDirectory());
	}

>>>>>>> refs/remotes/origin/development
}
