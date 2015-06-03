package api.commands.File;

import api.commands.CommandTester;
import util.StringContainer;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.RemoveFileFromExperimentRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for deleting a file in an experiment.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class DeleteFileTest extends SuperTestCommand {

    private StringContainer file;

    /**
     * Defines the file to delete.
     * @param ident
     * @param file
     * @param expectedResult
     */
    public DeleteFileTest(String ident, StringContainer file, boolean expectedResult) {
        super(ident, expectedResult);
        this.file = file;
    }

    @Override
    public void execute() {
        try {
            if (file == null)
            CommandTester.conn.sendRequest(new RemoveFileFromExperimentRequest(CommandTester.fileID.getString()), CommandTester.token, Constants.TEXT_PLAIN);
            else
                CommandTester.conn.sendRequest(new RemoveFileFromExperimentRequest(file.getString()), CommandTester.token, Constants.TEXT_PLAIN);

            if (CommandTester.conn.getResponseCode() == 200) {
                super.finalResult = true;
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
