package api.commands;

import api.commands.Annotation.*;
import model.Debug;

/**
 * Class which contains the tests for the annotation commands.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class AnnotationTests extends TestCollection{

    /**
     * Creates the annotation commands to tests.
     */
    public AnnotationTests(){
        super();
        super.commandList.add(new PostAnnotationFieldTest("POST FORCED DROPDOWN", "Things", new String[]{"One", "Two", "Three", "Four"}, true, true));
        super.commandList.add(new PostAnnotationFieldTest("POST NON-FORCED DROPDOWN", "MoreThings", new String[]{"Cat", "Dog", "Horse", "Giraffe"}, false, true));
        super.commandList.add(new PostAnnotationFieldTest("POST FORCED FREETEXT", "FreeThings", new String[]{"freetext"}, true, true));
        super.commandList.add(new PostAnnotationFieldTest("POST NON-FORCED FREETEXT", "MoreFreeThings", new String[]{"freetext"}, false, true));
        super.commandList.add(new PostAnnotationFieldTest("POST DROPDOWN GARBAGE", "GARBAGE", new String[]{garbage, garbage, garbage}, false, false));
        super.commandList.add(new PostAnnotationFieldTest("TO BE DELETED", "Delete", new String[]{"freetext"}, true, true));

        super.commandList.add(new DeleteAnnotationFieldTest("DELETE ANNOTATION NOT IN USE", "Delete", true));
        super.commandList.add(new DeleteAnnotationFieldTest("DELETE ANNOTATION IN USE", "Sex", false));

        super.commandList.add(new PutAnnotationFieldTest("PUT FORCED DROPDOWN FIELD", "Things", "Stuff", true));
        super.commandList.add(new PutAnnotationFieldTest("PUT NON-FORCED DROPDOWN FIELD", "MoreThings", "MoreStuff", true));
        super.commandList.add(new PutAnnotationFieldTest("PUT FORCED FREETEXT FIELD", "FreeThings", "FreeStuff", true));
        super.commandList.add(new PutAnnotationFieldTest("PUT NON-FORCED FREETEXT FIELD", "MoreFreeThings", "MoreFreeStuff", true));

        super.commandList.add(new PutAnnotationValueTest("PUT FORCED DROPDOWN VALUE", "Stuff", "One", "Hi", true));
        super.commandList.add(new PutAnnotationValueTest("PUT NON-FORCED DROPDOWN VALUE", "MoreStuff", "Cat", "Nothing", true));
        super.commandList.add(new PutAnnotationValueTest("PUT FORCED FREETEXT VALUE", "FreeStuff", "freetext", "cant", false));
        super.commandList.add(new PutAnnotationValueTest("PUT NON-FORCED FREETEXT VALUE", "MoreFreeStuff", "freetext", "stillcant", false));
        super.commandList.add(new PutAnnotationValueTest("PUT DROPDOWN TO FREETEXT", "Stuff", "Two", "freetext", false));
        super.commandList.add(new PutAnnotationValueTest("PUT DROPDOWN TO EXISTING VALUE", "Stuff", "Three", "Two", false));

        super.commandList.add(new PostAnnotationValueTest("POST DROPDOWN WITH NEW VALUE", "Stuff", "Five", true));
        super.commandList.add(new PostAnnotationValueTest("POST DROPDOWN WITH FREETEXT", "Stuff", "freetext", false));
        super.commandList.add(new PostAnnotationValueTest("POST DROPDOWN WITH EXISTING VALUE", "Stuff", "Five", false));

        super.commandList.add(new DeleteAnnotationValueTest("DELETE DROPDOWN VALUE", "Stuff", "Four", true));
        super.commandList.add(new DeleteAnnotationValueTest("DELETE FREETEXT VALUE", "FreeStuff", "freetext", false));

        super.commandList.add(new DeleteAnnotationFieldTest("CLEANUP", "Stuff", true));
        super.commandList.add(new DeleteAnnotationFieldTest("CLEANUP", "MoreStuff", true));
        super.commandList.add(new DeleteAnnotationFieldTest("CLEANUP", "FreeStuff", true));
        super.commandList.add(new DeleteAnnotationFieldTest("CLEANUP", "MoreFreeStuff", true));
    }

    @Override
    public boolean execute() {
        System.out.println("\n-------------------ANNOTATIONS-------------------");
        boolean bigResult = true;
        for (SuperTestCommand stc: super.commandList) {
            stc.execute();
            runTests++;

            boolean succeeded = stc.finalResult == stc.expectedResult;

            if (succeeded){
                succeededTests++;
            } else {
                failedTests++;
                nameOfFailedTests.add(stc.ident);
                bigResult = false;
            }

            Debug.log(stc.toString(), succeeded);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return bigResult;
    }
}
