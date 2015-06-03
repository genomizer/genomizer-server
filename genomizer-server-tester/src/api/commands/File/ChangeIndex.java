package api.commands.File;

import api.commands.*;
import util.FileIndices;
import util.StringContainer;

/**
 * Class which is used to update the reference to the files in the experiments.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class ChangeIndex extends SuperTestCommand {

    private String key;
    private int ind;
    private int checkSize;

    /**
     * Creates the information about the index to move to.
     * @param ident The identity of the command.
     * @param key The experiment to find the file in.
     * @param ind The index for the file to get.
     * @param checkIndex The expected number of files to find in the experiment.
     *                    Set to -1 to skip the checking.
     * @param expectedResult The expected result for the command.
     */
    public ChangeIndex(String ident, String key, int ind, int checkIndex, boolean expectedResult) {
        super(ident, expectedResult);
        this.key = key;
        this.ind = ind;
        this.checkSize = checkIndex;
    }

    @Override
    public void execute() {

        super.finalResult = true;
        FileIndices fi = new FileIndices(key);

        if (checkSize >= 0){
            super.finalResult = checkSize == FileIndices.getSize();
            if (!super.finalResult)
                System.out.println("Index SIZE: " + FileIndices.getSize());
        }

        if (ind < FileIndices.getSize()){
            CommandTester.fileID = new StringContainer(fi.getFileID(ind));
        }
    }
}
