package api.commands.GenomeRelease;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.RemoveGenomeReleaseRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for deleting a genome.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class DeleteGenomeTest extends SuperTestCommand {

    private String version;
    private String species;

    /**
     * Defines which genome to delete.
     * @param ident
     * @param species
     * @param version
     * @param expectedResult
     */
    public DeleteGenomeTest(String ident, String species, String version, boolean expectedResult) {
        super(ident, expectedResult);
        this.species = species;
        this.version = version;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new RemoveGenomeReleaseRequest(species, version),
                    CommandTester.token, Constants.TEXT_PLAIN);

            if (CommandTester.conn.getResponseCode() == 200) {
                super.finalResult = true;
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
