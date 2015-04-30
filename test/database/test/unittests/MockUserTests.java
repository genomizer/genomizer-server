package database.test.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.FilePathGenerator;
import database.constants.ServerDependentValues;
import database.containers.Annotation;
import database.containers.Experiment;
import database.containers.FileTuple;
import database.containers.Genome;
import database.test.TestInitializer;

public class MockUserTests {

    static TestInitializer ti;
    static DatabaseAccessor dbac;

    private static FilePathGenerator fpg;
    private static String testFolderName =
    		"Genomizer Test Folder - Dont be afraid to delete me";
    private static String testFolderPath;

    @BeforeClass
    public static void setUpBeforeClass() {

        ti = new TestInitializer();

        try {
            dbac = ti.setupWithoutAddingTuples();

            testFolderPath = System.getProperty("user.home")
                    + File.separator + testFolderName
                    + File.separator;

            fpg = dbac.getFilePathGenerator();
            fpg.setRootDirectory(testFolderPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ti.removeTuples();
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

        ti.removeTuplesKeepConnection();
        ti.recursiveDelete(new File(testFolderPath));
    }

    @Test
    public void addAnExperiment() throws Exception {

        dbac.addExperiment("My First Experiment");
        Experiment e = dbac.getExperiment("My First Experiment");

        assertEquals("My First Experiment", e.getID());
        assertEquals(0, e.getFiles().size());
    }

    @Test
    public void searchForEmptyExperiment() throws Exception {

        dbac.addExperiment("Ex1");
        List<Experiment> exs = dbac.search("ex1[expid]");
        assertEquals(1, exs.size());
    }

    @Test(expected = IOException.class)
    public void tryToAddSameExperimentAgain() throws Exception {

        dbac.addExperiment("My First Experiment");
        dbac.addExperiment("my first experiment");
    }

    @Test
    public void addFreeTextAnnotation() throws Exception {

        dbac.addFreeTextAnnotation("Tissue", null, true);
        List<String> annotationLabels = dbac.getAllAnnotationLabels();

        assertTrue(annotationLabels.contains("Tissue"));
    }

    @Test(expected = IOException.class)
    public void addDuplicateAnnotation() throws Exception {

        dbac.addFreeTextAnnotation("Tissue", null, true);
        dbac.addFreeTextAnnotation("tissue", null, false);
    }

    @Test
    public void addDropDownAnnotation() throws Exception {

        ArrayList<String> choices = new ArrayList<String>();
        choices.add("male");
        choices.add("female");
        choices.add("unknown");

        dbac.addDropDownAnnotation("Sex", choices, 2, false);
        List<String> annotationLabels = dbac.getAllAnnotationLabels();

        assertTrue(annotationLabels.contains("Sex"));
    }

    @Test
    public void addSpeciesDDAnnotation() throws Exception {

        ArrayList<String> choices = new ArrayList<String>();
        choices.add("Human");
        choices.add("Fly");
        choices.add("Rat");

        dbac.addDropDownAnnotation("Species", choices, 1, true);

        List<String> annotationLabels = dbac.getAllAnnotationLabels();
        assertTrue(annotationLabels.contains("Species"));

        List<String> annotationChoices = dbac.getChoices("Species");
        assertEquals(3, annotationChoices.size());
        assertTrue(annotationChoices.contains("Human"));
    }

    @Test (expected = IOException.class)
    public void attemptToRemoveSpeciesAnnotation() throws Exception {

        addSpeciesDDAnnotation();
        dbac.deleteAnnotation("Species");
    }

    @Test (expected = IOException.class)
    public void speciesRemainsAfterAttemptedDelete() throws Exception {

        attemptToRemoveSpeciesAnnotation();
        Annotation a = dbac.getAnnotationObject("Species");
        assertNotNull(a);
        assertEquals("Species", a.label);
    }

    @Test(expected = IOException.class)
    public void addDuplicateDropDownAnnotation() throws Exception {

        ArrayList<String> choices = new ArrayList<String>();
        choices.add("male");
        choices.add("female");
        choices.add("unknown");

        dbac.addDropDownAnnotation("Sex", choices, 2, false);
        dbac.addDropDownAnnotation("sex", choices, 0, false);
    }

    @Test
    public void annotateExperimentFT() throws Exception {

        dbac.addExperiment("My First Experiment");
        dbac.addFreeTextAnnotation("Tissue", null, true);
        dbac.annotateExperiment("My First Experiment", "Tissue",
                "Heart");
        Experiment e = dbac.getExperiment("My First Experiment");
        assertEquals("Heart", e.getAnnotations().get("Tissue"));
    }

    @Test
    public void annotateExperimentDD() throws Exception {

        if (dbac.getExperiment("My first experiment") == null) {
            dbac.addExperiment("My First Experiment");
        }

        ArrayList<String> choices = new ArrayList<String>();
        choices.add("male");
        choices.add("female");
        choices.add("unknown");

        dbac.addDropDownAnnotation("Sex", choices, 2, false);
        dbac.annotateExperiment("My First Experiment", "Sex", "Male");
        Experiment e = dbac.getExperiment("My First Experiment");
        assertEquals("male", e.getAnnotations().get("Sex"));
    }

    @Test(expected = IOException.class)
    public void annotateExperimentInvalidChoiceDD() throws Exception {

        dbac.addExperiment("My First Experiment");

        ArrayList<String> choices = new ArrayList<String>();
        choices.add("male");
        choices.add("female");
        choices.add("unknown");

        dbac.addDropDownAnnotation("Sex", choices, 2, false);
        dbac.annotateExperiment("My First Experiment", "Sex", "Alien");
    }

    @Test
    public void annotateExperimentFTandDD() throws Exception {

        annotateExperimentFT();
        annotateExperimentDD();
        Experiment e = dbac.getExperiment("My First Experiment");

        assertEquals(2, e.getAnnotations().size());
        assertEquals("male", e.getAnnotations().get("Sex"));
        assertEquals("Heart", e.getAnnotations().get("Tissue"));
    }

    @Test
    public void removeAnnotationChoice() throws Exception {

        addDropDownAnnotation();
        dbac.removeDropDownAnnotationValue("sex", "male");
        Annotation a = dbac.getAnnotationObject("sex");
        assertEquals(2, a.getPossibleValues().size());
    }

    @Test (expected = IOException.class)
    public void attemptToRemoveDefaultAnnotationChoice() throws Exception {

        addDropDownAnnotation();
        dbac.removeDropDownAnnotationValue("sex", "unknown");
        Annotation a = dbac.getAnnotationObject("sex");
        assertEquals(2, a.getPossibleValues().size());
    }

    @Test (expected = IOException.class)
    public void attemptToRemoveAnnotationValueUsedByExperiment()
    		throws Exception {

        annotateExperimentFTandDD();
        dbac.removeDropDownAnnotationValue("sex", "male");
    }

    @Test (expected = IOException.class)
    public void attemptToRemoveAnnotationUsedByExperiment() throws Exception {

        annotateExperimentFTandDD();
        dbac.deleteAnnotation("sex");
    }

    @Test
    public void addRawFiles() throws Exception {

        annotateExperimentFTandDD();

        FileTuple ft = dbac.addNewFile("my first experiment",
                FileTuple.RAW, "rawFile.fastq", "rawInput.fasta",
                null, "Umu", "Ruaridh", false, null, null);

        String expectedRawFileUploadURL = ServerDependentValues.UploadURL
                + testFolderPath
                + "My First Experiment"
                + File.separator
                + "raw"
                + File.separator
                + "rawFile.fastq";

        String expectedInputFileUploadURL = ServerDependentValues.UploadURL
                + testFolderPath
                + "My First Experiment"
                + File.separator
                + "raw"
                + File.separator
                + "rawInput.fasta";

        assertEquals(expectedRawFileUploadURL, ft.getUploadURL());
        assertEquals(expectedInputFileUploadURL,
                ft.getInputFileUploadURL());
    }

    @Test
    public void rawFileUploadDone() throws Exception {

    	annotateExperimentFTandDD();

    	FileTuple ft = dbac.addNewFile("my first experiment",
                FileTuple.RAW, "rawFile.fastq", "rawInput.fasta",
                null, "Umu", "Ruaridh", false, null, null);

    	dbac.fileReadyForDownload(ft.id);
        ft = dbac.getFileTuple(ft.id);

        assertEquals("Done", ft.status);
    }


    @Test
    public void addGenomeReleaseFile() throws Exception {

    	String uploadURL = dbac.addGenomeRelease("hg38", "Human",
                "hg38.fasta");

        String expectedUploadURL = ServerDependentValues.UploadURL
                + testFolderPath + "genome_releases" + File.separator
                + "Human" + File.separator + "hg38" + File.separator
                + "hg38.fasta";

        assertEquals(expectedUploadURL, uploadURL);

        Genome g = dbac.getGenomeRelease("hg38");

        assertEquals(1, g.getFiles().size());
        assertEquals("hg38", g.getFilePrefix());
    }

    @Test
    public void changeSpeciesAnnotationValue() throws Exception {

    	addSpeciesDDAnnotation();
        addGenomeReleaseFile();

        dbac.changeAnnotationValue("Species", "Human", "Homosapien");
        Genome g = dbac.getGenomeRelease("hg38");

        assertEquals("Homosapien", g.species);
    }

    @Test
    public void changeOtherAnnotationValue() throws Exception {

    	annotateExperimentFTandDD();
        addSpeciesDDAnnotation();
        addGenomeReleaseFile();
        dbac.changeAnnotationValue("Sex", "male", "man");
        Genome g = dbac.getGenomeRelease("hg38");

        assertEquals("Human", g.species);
    }

    @Test
    public void getGenomeRelease() throws Exception {

    	addGenomeReleaseFile();
        Genome g = dbac.getGenomeRelease("HG38");
        assertEquals("hg38", g.genomeVersion);

        String expectedFolderPath = testFolderPath
                + "genome_releases" + File.separator + "Human"
                + File.separator + "hg38" + File.separator;

        assertEquals(expectedFolderPath, g.folderPath);

        assertEquals("Human", g.species);

        assertEquals(1, g.getFiles().size());

        assertEquals(1, g.getDownloadURLs().size());

        String expectedDownloadURL = ServerDependentValues.DownloadURL
                + testFolderPath
                + "genome_releases"
                + File.separator
                + "Human"
                + File.separator
                + "hg38"
                + File.separator
                + "hg38.fasta";

        assertEquals(expectedDownloadURL, g.getDownloadURLs().get(0));
    }

    @Test
    public void addTwoGenomeReleaseFiles() throws Exception {

        addGenomeReleaseFile();

        String uploadURL = dbac.addGenomeRelease("hg38", "Human",
                "hg38.2.fasta");

        String expectedUploadURL = ServerDependentValues.UploadURL
                + testFolderPath + "genome_releases" + File.separator
                + "Human" + File.separator + "hg38" + File.separator
                + "hg38.2.fasta";

        assertEquals(expectedUploadURL, uploadURL);
    }

    @Test
    public void getGenomeReleaseFiles() throws Exception {

        addTwoGenomeReleaseFiles();

        Genome g = dbac.getGenomeRelease("hg38");

        assertEquals(2, g.getFiles().size());
        assertEquals(2, g.getDownloadURLs().size());
        assertEquals("hg38", g.getFilePrefix());
    }

    @Test
    public void processRawToProfile() throws Exception {

    	addRawFiles();
        addTwoGenomeReleaseFiles();

        Entry<String, String> folders = dbac
                .processRawToProfile("My First Experiment");

        String expectedRawFolder = testFolderPath
                + "My First Experiment" + File.separator + "raw"
                + File.separator;

        String expectedProfileFolder = testFolderPath
                + "My First Experiment" + File.separator + "profile"
                + File.separator + "0" + File.separator;

        assertEquals(expectedRawFolder, folders.getKey());
        assertEquals(expectedProfileFolder, folders.getValue());
    }

    @Test
    public void processingDone() throws Exception {

    	addRawFiles();
        addTwoGenomeReleaseFiles();

        Entry<String, String> folders = dbac
                .processRawToProfile("My First Experiment");

        addMockFile(folders.getValue(), "Prof1.sam");
        addMockFile(folders.getValue(), "Prof2.sam");

        dbac.addGeneratedProfiles("My First Experiment", folders.getValue(),
        		null, "-n1", "hg38", "Ruaridh", true);

        List<Experiment> exps = dbac
                .search("my first experiment[expid]");
        List<FileTuple> fts = exps.get(0).getFiles();

        assertEquals(3, fts.size());

        FileTuple ft = getFileTuple("Prof1.sam", fts);
        assertNotNull(ft);
        assertEquals("Done", ft.status);
        System.out.println(ft.toString());

        ft = getFileTuple("Prof2.sam", fts);
        assertNotNull(ft);
        assertEquals("Done", ft.status);
        System.out.println(ft.toString());
    }

    private FileTuple getFileTuple(String string, List<FileTuple> fts) {

    	for (FileTuple ft : fts) {
            if (ft.filename.equalsIgnoreCase(string)) {
                return ft;
            }
        }

        return null;
    }

    private void addMockFile(String folderPath, String filename)
            throws IOException {

    	File f = new File(folderPath + filename);
        f.createNewFile();
    }

}