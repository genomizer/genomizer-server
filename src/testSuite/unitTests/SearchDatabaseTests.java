package testSuite.unitTests;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;
import database.Experiment;

public class SearchDatabaseTests {

    private static DatabaseAccessor dbac;
    private static TestInitializer ti;

    @BeforeClass
    public static void setupBeforeClass() throws Exception {
    	ti = new TestInitializer();
    	dbac = ti.setup();
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException {
    	ti.removeTuples();
    }

    @Test
    public void shouldBeAbleToSearchForExperimentUsingPubMedString()
            throws Exception {
        List<Experiment> experiments = dbac.search("Exp1[ExpID]");
        assertEquals(1, experiments.size());
        assertEquals(2, experiments.get(0).getFiles().size());
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPubMedString()
            throws Exception {
        List<Experiment> experiments = dbac
                .search("/var/www/data/Exp2/raw/file1.fastq[Path]");
        assertEquals(1, experiments.size());
        assertEquals(1, experiments.get(0).getFiles().size());
    }

    @Test
    public void shouldBeAbleToSearchUsingPubMedString() throws Exception {
        List<Experiment> experiments = dbac
                .search("Human[Species] AND Unknown[Sex]");
        assertEquals(1, experiments.size());
        assertEquals("Exp1", experiments.get(0).getID());
    }

    @Test
    public void shouldBeAbleToSearchUsingPubMedString2() throws Exception {
        List<Experiment> experiments = dbac
                .search("Human[Species] AND Does not matter[Sex]");
        assertEquals(1, experiments.size());
        assertEquals("Exp2", experiments.get(0).getID());
    }

    @Test
    public void shouldBeAbleToSearchUsingPubMedString3() throws Exception {
        List<Experiment> experiments = dbac
                .search("Human[Species] OR Rat[Species]");
        assertEquals(3, experiments.size());
    }

    @Test
    public void shouldBeAbleToSearchUsingPubMedString4() throws Exception {
        List<Experiment> experiments = dbac
                .search("Human[Species] AND Child[Development Stage]");
        assertEquals(1, experiments.size());
        assertEquals("Exp2", experiments.get(0).getID());
    }

    @Test
    public void shouldBeAbleToSearchUsingPubMedString5() throws Exception {
        List<Experiment> experiments = dbac
                .search("Human[Species] AND Umeå Uni[Author]");
        assertEquals(1, experiments.size());
        assertEquals(1, experiments.get(0).getFiles().size());
        assertEquals("/var/www/data/Exp1/raw/file1.fastq", experiments.get(0).getFiles()
                .get(0).path);
    }

    @Test
    public void shouldBeAbleToSearchUsingPubMedString6() throws Exception {
        List<Experiment> experiments = dbac
                .search("Human[Species] NOT Child[Development Stage]");
        assertEquals(1, experiments.size());
        assertEquals("Adult", experiments.get(0).getAnnotations().get("Development Stage"));
    }

    @Test
    public void shouldBeAbleToSearchStartingWithNot() throws Exception {
        List<Experiment> experiments = dbac
                .search("NOT Child[Development Stage]");
        assertEquals(2, experiments.size());
    }

    @Test
    public void shouldBeAbleToSearchUsingNOT() throws Exception {
        List<Experiment> experiments = dbac
                .search("Child[Development Stage] NOT Human[Species]");
        assertEquals(1, experiments.size());
        assertEquals("Rat", experiments.get(0).getAnnotations().get("Species"));
    }

    @Test
    public void shouldBeAbleToSearch1() throws Exception {
        List<Experiment> experiments = dbac
                .search("Exp1[ExpID] AND Raw[FileType]");
        for (Experiment e: experiments) {
            System.out.println(e.toString());
        }
    }
}

















