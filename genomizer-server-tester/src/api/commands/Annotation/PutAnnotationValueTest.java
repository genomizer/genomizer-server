package api.commands.Annotation;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.RenameAnnotationValueRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for updating an annotation value.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class PutAnnotationValueTest extends SuperTestCommand {

    private String annotationName;
    private String oldName;
    private String newName;

    /**
     * Defines the annotation and information to update.
     * @param ident
     * @param annotationName
     * @param oldName
     * @param newName
     * @param expectedResult
     */
    public PutAnnotationValueTest(String ident, String annotationName,
                                  String oldName, String newName, boolean expectedResult) {
        super(ident, expectedResult);
        this.annotationName = annotationName;
        this.oldName = oldName;
        this.newName = newName;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new RenameAnnotationValueRequest(
                            annotationName, oldName, newName),
                    CommandTester.token, Constants.JSON);

            if (CommandTester.conn.getResponseCode() == 200) {
                super.finalResult = true;
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
