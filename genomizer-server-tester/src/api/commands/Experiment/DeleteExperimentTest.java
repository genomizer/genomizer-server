package api.commands.Experiment;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.RemoveExperimentRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for deleting an experiment.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class DeleteExperimentTest extends SuperTestCommand {

    private String key;

    /**
     * Defines which experiment to delete.
     * @param ident
     * @param key
     * @param expectedResult
     */
    public DeleteExperimentTest(String ident, String key, boolean expectedResult) {
        super(ident, expectedResult);
        this.key = key;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new RemoveExperimentRequest(key), CommandTester.token, Constants.TEXT_PLAIN);

            if (CommandTester.conn.getResponseCode() == 200) {
                super.finalResult = true;
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
