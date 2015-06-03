package api.commands.ConvertFile;

import api.commands.CommandTester;
import api.commands.ConvertTests;
import api.commands.SuperTestCommand;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.ErrorLogger;
import requests.ConvertFileRequest;
import util.Constants;
import util.FileData;
import util.RequestException;

/**
 * Command for converting a file.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class ConvertFileTest extends SuperTestCommand {
    private String format;

    /**
     * Creates the command for converting a file.
     * @param ident The identity of the command.
     * @param format The format to convert to.
     * @param expectedResult The expected result of the command.
     */
    public ConvertFileTest(String ident, String format, boolean expectedResult) {
        super(ident, expectedResult);
        this.format = format;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new ConvertFileRequest(CommandTester.fileID.getString(), format),
                    CommandTester.token, Constants.JSON);

            if (CommandTester.conn.getResponseCode() == 200){
                super.finalResult = true;
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                FileData data = gson.fromJson(CommandTester.conn.getResponseBody(), FileData.class);
                ConvertTests.convertedFile.setString(data.id);
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
