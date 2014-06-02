package database.testSuite.unitTests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.containers.Experiment;
import database.testSuite.TestInitializer;

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

        List<Experiment> experiments = dbac.search("Exp1[EXpid]");
        assertEquals(1, experiments.size());
        assertEquals(2, experiments.get(0).getFiles().size());
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPubMedString()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("/var/www/data/Exp2/raw/file1.fastq[PaTH]");

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
                .search("HumaN[Species] and Does not matter[SEx]");

        assertEquals(1, experiments.size());
        assertEquals("Exp2", experiments.get(0).getID());
    }

    @Test
    public void shouldBeAbleToSearchUsingPubMedString3() throws Exception {

        List<Experiment> experiments = dbac
                .search("Human[SpeciEs] or Rat[SpeciEs]");

        assertEquals(3, experiments.size());
    }

    @Test
    public void shouldBeAbleToSearchUsingPubMedString4() throws Exception {

        List<Experiment> experiments = dbac
                .search("Human[Species] AND Child[DeveLopmeNt Stage]");

        assertEquals(1, experiments.size());
        assertEquals("Exp2", experiments.get(0).getID());
    }

    @Test
    public void shouldBeAbleToSearchUsingPubMedString5() throws Exception {

        List<Experiment> experiments = dbac
                .search("Human[SpeCies] AnD Umeå uni[author]");

        assertEquals(1, experiments.size());
        assertEquals(1, experiments.get(0).getFiles().size());
        assertEquals("/var/www/data/Exp1/raw/file1.fastq", experiments.get(0)
                .getFiles().get(0).path);
    }

    @Test
    public void shouldBeAbleToSearchUsingPubMedString6() throws Exception {
        List<Experiment> experiments = dbac
                .search("Human[SpEcies] NoT ChiLd[DeveLopment Stage]");

        assertEquals(1, experiments.size());
        assertEquals("Adult",
                experiments.get(0).getAnnotations().get("Development Stage"));
    }

    @Test
    public void shouldBeAbleToSearchStartingWithNot() throws Exception {

        List<Experiment> experiments = dbac
                .search("not ChiLd[Development Stage]");

        assertEquals(2, experiments.size());
    }

    @Test
    public void shouldBeAbleToSearchUsingNOT() throws Exception {

        List<Experiment> experiments = dbac
                .search("CHild[Development Stage] NOT HumAn[Species]");

        assertEquals(1, experiments.size());
        assertEquals("Rat", experiments.get(0).getAnnotations().get("Species"));
    }

    @Test
    public void shouldBeAbleToSearch1() throws Exception {

        List<Experiment> experiments = dbac
                .search("Exp1[ExpID] And Raw[FileType]");

        for (Experiment e : experiments) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void shouldBeAbleToSearchCaseInsensitive() throws IOException,
            SQLException, ParseException {

        List<Experiment> experiments = dbac
                .search("EXp1[ExPiD] AND RaW[FileTYPE]");

        for (Experiment e : experiments) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void shouldBeAbleToSearchMoreCaseInsensitive() throws IOException,
            SQLException, ParseException {

        List<Experiment> experiments = dbac.search("ExP1[ExpID] and "
                + "RAw[FileType] aND /var/www/data/Exp1/raw/"
                + "file1_input.fastq[FilePath]");

        for (Experiment e : experiments) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void shouldBeAbleToSearchForExperimentUsingDate() throws Exception {

        List<Experiment> elist = dbac.search("exp2[expid]");
        Date date = elist.get(0).getFiles().get(0).date;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String query = df.format(date) + "[date]";
        List<Experiment> experiments = dbac.search(query);

        assertEquals(experiments.get(0).getFiles().get(0).date.getTime(),
                date.getTime());
    }

    @Test
    public void shouldBeAbleToSearchForExperimentUsingFileID() throws Exception {

        List<Experiment> elist = dbac.search("exp2[expid] ANd UCSC[author]");

        int id = elist.get(0).getFiles().get(0).id;

        String query = Integer.toString(id) + "[fileid]";
        List<Experiment> experiments = dbac.search(query);

        assertEquals(experiments.get(0).getFiles().get(0).author, "UCSC");
        assertEquals(elist.get(0).getFiles().get(0).expId, "Exp2");
    }

    @Test
    public void shouldBeAbleToSearchOnPath() throws Exception {

        List<Experiment> elist = dbac.search("/var/www/data/Exp1/raw/"
                + "file1.fastq[path]");

        assertEquals(1, elist.size());
    }

    @Test
    public void shouldGetAllExperimentsWhenSearchingAnEmptySring()
            throws Exception {

        List<Experiment> exps = dbac.search("");
        assertEquals(4, exps.size());

        Experiment e = getExp("Exp1", exps);

        assertEquals(4, e.getAnnotations().size());
        assertEquals(2, e.getFiles().size());
    }

    @Test
    public void searchPrintTest() throws Exception {

        String pms = "exp[expid]";

        List<Experiment> exps = dbac.search(pms);
        for (Experiment e : exps) {
            System.out.println(e.toString());
        }
    }

    private Experiment getExp(String string, List<Experiment> exps) {

        for (Experiment e : exps) {
            if (e.getID().equalsIgnoreCase(string)) {
                return e;
            }
        }
        return null;
    }
}