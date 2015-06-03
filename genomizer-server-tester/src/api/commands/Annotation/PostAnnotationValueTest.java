package api.commands.Annotation;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.AddNewAnnotationValueRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for creating an annotation value.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class PostAnnotationValueTest extends SuperTestCommand {

    private String annotationLabel;
    private String annotationValue;

    /**
     * Defines the annotation value to create.
     * @param ident
     * @param annotationLabel
     * @param annotationValue
     * @param expectedResult
     */
    public PostAnnotationValueTest(String ident, String annotationLabel, String annotationValue, boolean expectedResult) {
        super(ident, expectedResult);
        this.annotationLabel = annotationLabel;
        this.annotationValue = annotationValue;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new AddNewAnnotationValueRequest(annotationLabel, annotationValue),
                    CommandTester.token, Constants.JSON);

            if (CommandTester.conn.getResponseCode() == 200) {
                super.finalResult = true;
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
