package api.commands.Annotation;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.RemoveAnnotationValueRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for deleting an annotation value.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class DeleteAnnotationValueTest extends SuperTestCommand {

    private String name;
    private String value;

    /**
     * Defins the annotation value to delete.
     * @param ident
     * @param name
     * @param value
     * @param expectedResult
     */
    public DeleteAnnotationValueTest(String ident, String name, String value, boolean expectedResult) {
        super(ident, expectedResult);
        this.name = name;
        this.value = value;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new RemoveAnnotationValueRequest(
                            name, value),
                    CommandTester.token, Constants.TEXT_PLAIN);
            if (CommandTester.conn.getResponseCode() == 200) {
                super.finalResult = true;
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
