package api.commands.Annotation;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.RemoveAnnotationFieldRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for deleting the annotation fields.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class DeleteAnnotationFieldTest extends SuperTestCommand {

    private String name;

    /**
     * Defins the annotation filed to delete.
     * @param ident
     * @param name
     * @param expectedResult
     */
    public DeleteAnnotationFieldTest(String ident, String name, boolean expectedResult) {
        super(ident, expectedResult);
        this.name = name;
    }

    @Override
    public void execute() {

        try {
            CommandTester.conn.sendRequest(new RemoveAnnotationFieldRequest(name),
                    CommandTester.token, Constants.TEXT_PLAIN);
            if (CommandTester.conn.getResponseCode() == 200) {
                super.finalResult = true;
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
