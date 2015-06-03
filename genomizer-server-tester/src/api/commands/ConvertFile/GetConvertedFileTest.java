package api.commands.ConvertFile;

import api.commands.CommandTester;
import api.commands.ConvertTests;
import api.commands.SuperTestCommand;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.ErrorLogger;
import requests.DownloadFileRequest;
import util.Constants;
import util.FileData;
import util.RequestException;

/**
 * Command for getting the converted file.
 */
public class GetConvertedFileTest extends SuperTestCommand {

    /**
     *
     * @param ident
     * @param expectedResult
     */
    public GetConvertedFileTest(String ident, boolean expectedResult) {
        super(ident, expectedResult);
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new DownloadFileRequest(ConvertTests.convertedFile.getString(), ""),
                    CommandTester.token, Constants.TEXT_PLAIN);

            if (CommandTester.conn.getResponseCode() == 200) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                FileData data = gson.fromJson(CommandTester.conn.getResponseBody(), FileData.class);

                if (ConvertTests.convertedFile.getString().equals(data.id)) {
                    super.finalResult = true;
                }
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
