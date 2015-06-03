package api.commands.Experiment;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.ChangeExperimentRequest;
import util.AnnotationDataValue;
import util.RequestException;

/**
 * Test for updating an experiment.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class PutExperimentTest extends SuperTestCommand {

    String key;
    AnnotationDataValue[] adv;

    /**
     * Defines the experiment and information to update.
     * @param ident
     * @param key
     * @param advIN
     * @param expectedResult
     */
    public PutExperimentTest(String ident, String key, AnnotationDataValue[] advIN, boolean expectedResult) {
        super(ident, expectedResult);
        this.key = key;
        this.adv = advIN;
    }

    @Override
    public void execute() {
            try {
                CommandTester.conn.sendRequest(new ChangeExperimentRequest(key, this.adv), CommandTester.token, "application/json" );

                if (CommandTester.conn.getResponseCode() == 200) {
                    super.finalResult = true;
                }
            } catch (RequestException e) {
                if (super.expectedResult) ErrorLogger.log(e);
            }
    }
}
