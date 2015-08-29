package database.test.unittests;

import database.DatabaseAccessor;
import database.containers.Experiment;
import database.test.TestInitializer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;

import static org.junit.Assert.assertEquals;

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
    public void shouldBeAbleToSearchForExperimentUsingPartialPubMedString()
            throws Exception {

        List<Experiment> experiments = dbac.search("Exp[EXpid]");
        assertEquals(6, experiments.size());

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
    public void shouldBeAbleToSearchForFilesUsingPartialPathPubMedString()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("/var/www/data/Exp2/raw/file1.fast[PaTH]");

        assertEquals(1, experiments.size());
        assertEquals(1, experiments.get(0).getFiles().size());
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPartialPathPubMedString2()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("raw/file1.fastq[PaTH]");

        assertEquals(3, experiments.size());
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPartialPathPubMedString3()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("/data/Exp2[PaTH]");

        assertEquals(1, experiments.size());
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPartialPathPubMedString4()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("file1[PaTH]");

        assertEquals(3, experiments.size());
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPartialNamePubMedString()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("file1[fiLeNaMe]");

        assertEquals(5, getNumberOfFiles(experiments));
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPartialNamePubMedString2()
            throws Exception {

        List<Experiment> experiments = dbac
                .search(".sam[fiLeNaMe]");

        assertEquals(3, getNumberOfFiles(experiments));
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPartialTypePubMedString()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("Prof[filetype]");

        assertEquals(12, getNumberOfFiles(experiments));
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPartialTypePubMedString2()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("egion[filetype]");

        assertEquals(0, getNumberOfFiles(experiments));
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPartialInputPathPubMedString()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("file1_input[inputfilepath]");

        assertEquals(6, getNumberOfFiles(experiments));
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPartialInputPathPubMedString2()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("www[inputfilepath]");

        assertEquals(6, getNumberOfFiles(experiments));
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPartialMetaDataPathPubMedString()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("--best[metadata]");

        assertEquals(12, getNumberOfFiles(experiments));
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPartialMetaDataPubMedString2()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("2[metadata]");

        assertEquals(1, getNumberOfFiles(experiments));
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPartialAuthorPubMedString()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("UCS[author]");

        assertEquals(6, getNumberOfFiles(experiments));
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPartialUploaderPubMedString()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("ser1[uploader]");

        assertEquals(19, getNumberOfFiles(experiments));
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPartialGRVersionPubMedString2()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("hg[grversion]");

        assertEquals(1, getNumberOfFiles(experiments));
    }

    @Test
    public void shouldBeAbleToSearchForFilesUsingPartialStatusPubMedString2()
            throws Exception {

        List<Experiment> experiments = dbac
                .search("Do[status]");

        assertEquals(19, getNumberOfFiles(experiments));
    }

    private int getNumberOfFiles(List<Experiment> experiments) {
        int nrOfFiles = 0;
        for (Experiment exp : experiments) {
            nrOfFiles += exp.getFiles().size();
        }
        return nrOfFiles;
    }

    @Test
    public void shouldBeAbleToSearchForFileEndingUsingPartialPathPubMedString()
            throws Exception {

        List<Experiment> experiments = dbac
                .search(".fastq[PaTH]");

        assertEquals(5, experiments.size());
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
                .search("HumaN[Species] and Does_not_matter[SEx]");

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
                .search("Human[Species] AND Child[Development_Stage]");

        assertEquals(1, experiments.size());
        assertEquals("Exp2", experiments.get(0).getID());
    }

    @Test
    @Ignore // We stick to ascii...
    public void shouldBeAbleToSearchUsingPubMedString5() throws Exception {

        List<Experiment> experiments = dbac
                .search("Human[SpeCies] AnD Umea uni[author]");

        assertEquals(1, experiments.size());
        assertEquals(1, experiments.get(0).getFiles().size());
        assertEquals("/var/www/data/Exp1/raw/file1.fastq", experiments.get(0)
                .getFiles().get(0).path);
    }

    @Test
    public void shouldBeAbleToSearchUsingPubMedString6() throws Exception {
        List<Experiment> experiments = dbac
                .search("Human[SpEcies] NoT ChiLd[Development_Stage]");

        assertEquals(1, experiments.size());
        assertEquals("Adult",
                experiments.get(0).getAnnotations().get("Development_Stage"));
    }

    @Test
    public void shouldBeAbleToSearchStartingWithNot() throws Exception {

        List<Experiment> experiments = dbac
                .search("not ChiLd[Development_Stage]");

        assertEquals(4, experiments.size());
    }

    @Test
    public void shouldBeAbleToSearchUsingNOT() throws Exception {

        List<Experiment> experiments = dbac
                .search("CHild[Development_Stage] NOT HumAn[Species]");

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
    public void shouldGetAllExperimentsWhenSearchingAnEmptyString()
            throws Exception {

        List<Experiment> exps = dbac.search("");
        assertEquals(6, exps.size());

        Experiment e = getExp("Exp1", exps);

        assertEquals(4, e.getAnnotations().size());
        assertEquals(2, e.getFiles().size());
    }

    @Test
    public void shouldGetAllExperimentsWhenSearchingANullString()
            throws Exception {

        List<Experiment> exps = dbac.search(null);
        assertEquals(6, exps.size());

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