package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.FilePathGenerator;
import database.FileTuple;

public class TestFilePathGEN {

	@Test

	public void getCatFilePath(){
		String generatedPath = FilePathGenerator.GenerateFilePath("CatTest", "raw", "CatFacts.txt");
		String expectedPath = FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "/CatTest/raw/CatFacts.txt";

        assertEquals(expectedFolderPath, chainFilePath);

    }

    @Test
    public void shouldGenerateRightGenomeReleaseFolderPath() throws Exception {
        String version = "hg13";
        String species = "Human";

        String expectedGenomeReleaseFolderPath = testFolder + File.separator + "genome_releases" + File.separator + species + File.separator + version + File.separator;

        String path = fpg.generateGenomeReleaseFolder(version, species);

        assertEquals(expectedGenomeReleaseFolderPath, path);

    }

    @Test
    public void shouldGenerateChainFileFolder() throws Exception {
        String species = "Human";
        String fromVersion = "v1";
        String toVersion = "v2";

        fpg.generateChainFolderPath(species, fromVersion, toVersion);

        assertNotNull(searchForSubFolder(testFolder, "chain_files"));
    }

    @Test
    public void shouldGenerateSpeciesFolderForChainFiles() throws Exception {
        String species = "Human";
        String fromVersion = "v1";
        String toVersion = "v2";

        fpg.generateChainFolderPath(species, fromVersion, toVersion);

        File chainFileFolder = searchForSubFolder(testFolder, "chain_files");
        assertNotNull(searchForSubFolder(chainFileFolder, species));
    }

    @Test
    public void shouldGenerateFromVersionToVersionFolderForChainFiles()
            throws Exception {
        String species = "Human";
        String fromVersion = "v1";
        String toVersion = "v2";

        fpg.generateChainFolderPath(species, fromVersion, toVersion);

        File chainFileFolder = searchForSubFolder(testFolder, "chain_files");
        File speciesFolder = searchForSubFolder(chainFileFolder, species);
        assertNotNull(searchForSubFolder(speciesFolder, fromVersion + " - "
                + toVersion));
    }

    private File searchForSubFolder(File folder, String name) {
        for (File f : folder.listFiles()) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }


}

