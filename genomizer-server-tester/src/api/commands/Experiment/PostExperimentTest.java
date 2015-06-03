package api.commands.Experiment;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.AddExperimentRequest;
import util.AnnotationDataValue;
import util.RequestException;

/**
 * Test for creating a new experiment.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class PostExperimentTest extends SuperTestCommand {

    private AnnotationDataValue[] adv;
    private String key;

    /**
     * Sets the information for the experiment to create.
     * @param ident
     * @param key
     * @param adv
     * @param expectedResult
     */
    public PostExperimentTest(String ident, String key, AnnotationDataValue[] adv, boolean expectedResult) {
        super(ident, expectedResult);
        this.key = key;
        this.adv = adv;

    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new AddExperimentRequest(key, adv), CommandTester.token, "application/json");

            if (CommandTester.conn.getResponseCode() == 200) {
                super.finalResult = true;
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
