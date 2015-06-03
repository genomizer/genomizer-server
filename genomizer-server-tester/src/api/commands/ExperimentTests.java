package api.commands;

import api.commands.Experiment.*;
import model.Debug;
import util.AnnotationDataValue;

/**
 * Class containing the experiment tests.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class ExperimentTests extends TestCollection{

    /**
     * Defines and creates the commands used during the testing.
     */
    public ExperimentTests(){
        super();
        AnnotationDataValue[] adv1 = {new AnnotationDataValue(null, "Species", "Fly"),
                new AnnotationDataValue(null, "Sex", "Male"),
                new AnnotationDataValue(null, "Tissue", "Larva")};

        AnnotationDataValue[] adv2 = {new AnnotationDataValue(null, "Sex", "Female")};

        AnnotationDataValue[] adv3 = {new AnnotationDataValue(null, "Species", "Fly"),
                new AnnotationDataValue(null, "Sex", "Male"),
                new AnnotationDataValue(null, "Tissue", "")};

        AnnotationDataValue[] adv4 = {new AnnotationDataValue(null, "Species", "Fly"),
                new AnnotationDataValue(null, "Sexton", "Male"),
                new AnnotationDataValue(null, "Tissue", "Larva")};

        AnnotationDataValue[] adv5 = {new AnnotationDataValue(null, "Species", "Fly"),
                new AnnotationDataValue(null, "Sex", "Shemale"),
                new AnnotationDataValue(null, "Tissue", "Larva")};

        AnnotationDataValue[] adv6 = {new AnnotationDataValue(null, "Species", "Fly"),
                new AnnotationDataValue(null, "Tissue", "Larva")};

        super.commandList.add(new PostExperimentTest("POST EXPERIMENT", CommandTester.EXP_NAME, adv1, true));
        super.commandList.add(new GetExperimentTest("GET POSTED EXPERIMENT", CommandTester.EXP_NAME, CommandTester.EXP_NAME, true));
        super.commandList.add(new GetExperimentTest("GET NONEXISTING EXPERIMENT", CommandTester.EXP_NAME+"1", "", false));

        super.commandList.add(new PutExperimentTest("PUT EXPERIMENT", CommandTester.EXP_NAME, adv2, true));
        super.commandList.add(new GetExperimentTest("GET UPDATED EXPERIMENT", CommandTester.EXP_NAME, "Female", true));

        super.commandList.add(new PutExperimentTest("PUT WRONG ANNOTATION", CommandTester.EXP_NAME, adv4, false));
        super.commandList.add(new GetExperimentTest("GET NONEXISTING EDITED ANNOTATION", CommandTester.EXP_NAME, "sexton", false));

        super.commandList.add(new PutExperimentTest("PUT WRONG ANNOTATION VALUE", CommandTester.EXP_NAME, adv5, false));
        super.commandList.add(new GetExperimentTest("GET NONEXISTING EDITED ANNOTATION VALUE", CommandTester.EXP_NAME, "Shemale", false));
        super.commandList.add(new GetExperimentTest("GET UNEDITED ANNOTATION", CommandTester.EXP_NAME, "Female", true));
        super.commandList.add(new GetExperimentTest("GET OTHER ANNOTATION", CommandTester.EXP_NAME, "Larva", true));

        super.commandList.add(new PostExperimentTest("POST WRONG ANNOTATION", "WrongAnnExp", adv4, false));
        super.commandList.add(new GetExperimentTest("GET FAULTY EXPERIMENT", "WrongAnnExp", "\"name\":\"WrongAnnExp\"", false));
        super.commandList.add(new PostExperimentTest("POST WRONG ANNOTATION VALUE", "WrongAnnExp2", adv5, false));
        super.commandList.add(new GetExperimentTest("GET FAULTY EXPERIMENT VALUE", "WrongAnnExp2", "\"name\":\"WrongAnnExp2\"", false));

        super.commandList.add(new PostExperimentTest("POST EXP TO DELETE", "DeleteTestExp1", adv6, true));
        super.commandList.add(new PostExperimentTest("POST EXISTING EXPERIMENT", "DeleteTestExp1", adv6, false));
        super.commandList.add(new DeleteExperimentTest("DELETE EXPERIMENT", "DeleteTestExp1", true));
        super.commandList.add(new DeleteExperimentTest("DELETE NONEXISTING EXPERIMENT", "DeleteTestExp1", false));

        super.commandList.add(new PostExperimentTest("POST EXP TO EDIT", "DeleteTestExp2", adv6, true));
        super.commandList.add(new PutExperimentTest("PUT EXPERIMENT TO EXTEND", "DeleteTestExp2", adv2, true));
        super.commandList.add(new GetExperimentTest("GET UPDATED EXTENDED EXPERIMENT", "DeleteTestExp2", "Female", true));
        super.commandList.add(new DeleteExperimentTest("CLEANUP", "DeleteTestExp2", true));

        super.commandList.add(new PostExperimentTest("POST NO NAME", "", adv1, false));
        super.commandList.add(new PostExperimentTest("POST FAIL FORCED", "TestExperiment", adv2, false));
        super.commandList.add(new PostExperimentTest("POST EMPTY FORCED", "TestExperiment", adv3, false));
        super.commandList.add(new PutExperimentTest("PUT NONEXISTING EXPERIMENT", CommandTester.EXP_NAME+"2", adv1, false));
    }

    @Override
    public boolean execute() {
        System.out.println("\n--------------------EXPERIMENT-------------------");
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
