package command.test.processCommands;

import com.google.gson.Gson;
import command.ValidateException;
import command.process.ProcessCommands;
import database.subClasses.UserMethods;
import org.junit.Test;
import server.RequestHandler;

import static org.junit.Assert.*;

public class SmoothingProcessCommandTest {
    private final Gson gson = new RequestHandler().getGson();

    @Test
    public void canCreateWithCorrectInput() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"smoothing\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awe\",\"windowSize\":\"3\"," +
                        "\"meanOrMedian\":\"mean\"," +
                        "\"minSmooth\":\"2\"}]}, {\"type\":\"ratio\", \"infile1\": " +
                        "\"infile1Name\", \"infile2\": \"infile2Name\"}]}";
        ProcessCommands processCommands =
                gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    @Test(expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectInfileName() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"smoothing\"," +
                        "\"files\":[{\"infile\":\"bigte€st1.fastq\"," +
                        "\"outfile\":\"awe\",\"windowSize\":\"3\"," +
                        "\"meanOrMedian\":\"mean\"," +
                        "\"minSmooth\":\"2\"}]}, {\"type\":\"ratio\", \"infile1\": " +
                        "\"infile1Name\", \"infile2\": \"infile2Name\"}]}";
        ProcessCommands processCommands =
                gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
}