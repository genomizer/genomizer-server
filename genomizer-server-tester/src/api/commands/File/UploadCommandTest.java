package api.commands.File;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import communication.HTTPURLUpload;
import model.ErrorLogger;

import java.io.IOException;

/**
 * Test for the uploading command.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class UploadCommandTest extends SuperTestCommand {

    FileTuple ft;

    /**
     * defines the fileTuple to upload.
     * @param ident
     * @param ft
     * @param expectedResult
     */
    public UploadCommandTest(String ident, FileTuple ft, boolean expectedResult) {
        super(ident, expectedResult);
        this.ft = ft;
    }

    @Override
    public void execute() {

        HTTPURLUpload upload = new HTTPURLUpload(ft.getUploadPath(), ft.getName(), ft.getName());

        try {
            upload.sendFile(CommandTester.token);
            if (upload.getResponseCode() == 200) {
                super.finalResult = true;

            }
        } catch (IOException e) {
            if (super.expectedResult) ErrorLogger.log(e);

        }

    }
}
