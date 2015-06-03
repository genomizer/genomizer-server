package api.commands.Annotation;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.RenameAnnotationFieldRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for updating the annotation field.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class PutAnnotationFieldTest extends SuperTestCommand {

    private String oldName;
    private String newName;

    /**
     * Defines the annotation and information to update.
     * @param ident
     * @param oldName
     * @param newName
     * @param expectedResult
     */
    public PutAnnotationFieldTest(String ident, String oldName, String newName, boolean expectedResult) {
        super(ident, expectedResult);
        this.oldName = oldName;
        this.newName = newName;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new RenameAnnotationFieldRequest(
                            oldName, newName),
                    CommandTester.token, Constants.JSON);

            if (CommandTester.conn.getResponseCode() == 200) {
                super.finalResult = true;
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
