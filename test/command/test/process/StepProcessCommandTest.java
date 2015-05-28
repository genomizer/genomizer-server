package command.test.process;

import com.google.gson.Gson;
import command.ValidateException;
import command.process.ProcessCommands;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import org.junit.Test;
import server.RequestHandler;


public class StepProcessCommandTest {
    private final Gson gson = new RequestHandler().getGson();

    @Test
    public void canCreateWithCorrectInput() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"step\"," +
                        "\"files\":[{\"infile\":\"bigte1.wig\"," +
                        "\"outfile\":\"awe\",\"stepSize\":\"3\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    @Test(expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectInfileName() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"step\"," +
                        "\"files\":[{\"in%file\":\"bigte1.wig\"," +
                        "\"outfile\":\"awe\",\"stepSize\":\"3\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
    @Test (expected = ValidateException.class)
    public void shouldGiveValidateExceptionOnIncorrectInfileLength() throws ValidateException {

        String s = "";
        for(int i = 0; i < MaxLength.FILE_FILENAME + 1; i++) {
            s += "a";
        }
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"step\"," +
                        "\"files\":[{\""+s+"\":\"bigte1.wig\"," +
                        "\"outfile\":\"awe\",\"stepSize\":\"3\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
    @Test(expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectOutfileName() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"step\"," +
                        "\"files\":[{\"infile\":\"bigte1.wig\"," +
                        "\"outfile\":\"a&we\",\"stepSize\":\"3\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    @Test(expected = ValidateException.class)
    public void shouldGiveValidateExceptionOnIncorrectOutfileLength() throws ValidateException {

        String s = "";
        for(int i = 0; i < MaxLength.FILE_FILENAME + 1; i++) {
            s += "a";
        }
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"step\"," +
                        "\"files\":[{\"infile\":\"bigte1.wig\"," +
                        "\"outfile\":\""+s+"\",\"stepSize\":\"3\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    @Test(expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectStepSize() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"step\"," +
                        "\"files\":[{\"infile\":\"bigte1.wig\"," +
                        "\"outfile\":\"awe\",\"stepSize\":\"-1\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
    @Test(expected = ValidateException.class)
    public void canGiveValidateExceptionOnStepSizeNull() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"step\"," +
                        "\"files\":[{\"infile\":\"bigte1.wig\"," +
                        "\"outfile\":\"awe\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
}