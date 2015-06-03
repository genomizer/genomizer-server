package api.commands.File;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import model.ErrorLogger;
import requests.AddFileToExperiment;
import util.Constants;
import util.RequestException;

/**
 * Test for adding a new file to an experiment.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class PostFileTest extends SuperTestCommand{

    private FileTuple ft;
    private Gson gson;

    /**
     * Defines the fileTuple to add to the experiment.
     * @param ident
     * @param ft
     * @param expectedResult
     */
    public PostFileTest(String ident, FileTuple ft, boolean expectedResult) {
        super(ident, expectedResult);
        this.ft = ft;
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        gson = builder.create();
    }

    @Override
    public void execute() {

        try {
            CommandTester.conn.sendRequest(new AddFileToExperiment(ft.getId(), ft.getName(),
                    ft.getType(),ft.getMetaData(),ft.getAuthor(), "", false, ft.getGrVersion()),
                    CommandTester.token, Constants.JSON);


            if (CommandTester.conn.getResponseCode() == 200) {
                ft.setUploadPath(parseURL());
                UploadCommandTest ut = new UploadCommandTest("POST FILE UPLOAD", ft, true);
                ut.execute();
                super.finalResult = (ut.finalResult == ut.expectedResult);
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }

    private String parseURL(){
        PostFileResponse pf;
        pf =  gson.fromJson(CommandTester.conn.getResponseBody(), PostFileResponse.class);

        return pf.URLupload;
    }

    private class PostFileResponse {

        @Expose
        public String URLupload;

        public PostFileResponse(String URLupload) {
            this.URLupload = URLupload;
        }


    }
}
