package api.commands;

import api.commands.GenomeRelease.DeleteGenomeTest;
import api.commands.GenomeRelease.GetAllGenomeTest;
import api.commands.GenomeRelease.GetSpecificGenomeTest;
import api.commands.GenomeRelease.PostGenomeTest;
import model.Debug;

/**
 * Class which contains the tests for the genome releases.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class GenomeReleaseTests extends TestCollection{

    /**
     * Creates the genome release commands to test.
     */
    public GenomeReleaseTests(){
        super();

        super.commandList.add(new PostGenomeTest("POST GENOME SINGLE", new String[]{"File.whatever"}, "Fly", "SingleFileTest", true));
        super.commandList.add(new GetSpecificGenomeTest("GET GENOME SINGLE", "Fly",
                new String [] {
                        "{\"genomeVersion\":\"SingleFileTest\",\"species\":\"Fly\",\"folderPath\":\"",
                        "/genome_releases/Fly/SingleFileTest/\",\"files\":[\"File.whatever\"]}" },
                true));
        super.commandList.add(new PostGenomeTest("POST GENOME MULTI", new String[]{"File1.wha", "File2.wha", "File3.wha"}, "Human", "MultiFileTest", true));
        super.commandList.add(new GetSpecificGenomeTest("GET GENOME MULTI", "Human",
                new String [] {
                        "{\"genomeVersion\":\"MultiFileTest\",\"species\":\"Human\",\"folderPath\":\"",
                        "/genome_releases/Human/MultiFileTest/\",\"files\":",
                        "\"File1.wha\"",
                        "\"File2.wha\"",
                        "\"File3.wha\"" },
                true));
        super.commandList.add(new PostGenomeTest("POST GENOME NAME IN USE", new String[]{"File.whatever"}, "Fly", "SingleFileTest", false));
        super.commandList.add(new PostGenomeTest("POST GENOME NO SPECIE", new String[]{"File.whatever"}, "", "NoSpecieTest", false));
        super.commandList.add(new DeleteGenomeTest("DELETE UNUSED GENOME", "Fly", "SingleFileTest", true));
        super.commandList.add(new DeleteGenomeTest("DELETE USED GENOME", "Human", "hg38", false));
        super.commandList.add(new GetAllGenomeTest("GET ALL GENOMES",
                "{\"genomeVersion\": \"SingleFileTest\",\"species\": \"Fly\",\"folderPath\": \"/var/www/data/genome_releases/Fly/SingleFileTest/\",\"files\": [\"File.whatever\"]}",
                false));
    }

    @Override
    public boolean execute() {
        System.out.println("\n----------------------GENOME---------------------");
        boolean bigResult = true;
        for (SuperTestCommand stc: super.commandList) {
            stc.execute();
            runTests++;

            boolean succeeded = stc.finalResult == stc.expectedResult;

            if (succeeded){
                succeededTests++;
            } else {
                failedTests++;
                nameOfFailedTests.add(stc.ident);
                bigResult = false;
            }

            Debug.log(stc.toString(), succeeded);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return bigResult;
    }
}
