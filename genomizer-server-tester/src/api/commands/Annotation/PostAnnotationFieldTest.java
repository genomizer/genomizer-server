package api.commands.Annotation;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.AddAnnotationRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for the create annotation command.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class PostAnnotationFieldTest extends SuperTestCommand {

    private String[] categories;
    private String name;
    private Boolean forced;

    /**
     * Defines the information for the annotation.
     * @param ident
     * @param name
     * @param categories
     * @param forced
     * @param expectedResult
     */
    public PostAnnotationFieldTest(String ident, String name,
                                   String[] categories, boolean forced, boolean expectedResult) {
        super(ident, expectedResult);
        this.name = name;
        this.forced = forced;
        this.categories = categories;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new AddAnnotationRequest(
                            name, categories, forced),
                    CommandTester.token, Constants.JSON);

            if (CommandTester.conn.getResponseCode() == 200) {
                super.finalResult = true;
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
