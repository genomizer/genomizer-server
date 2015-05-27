package command.test.processCommands;

import com.google.gson.Gson;
import command.ValidateException;
import command.process.ProcessCommands;
import database.constants.MaxLength;
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

    @Test (expected = ValidateException.class)
    public void shouldGiveValidateExceptionOnIncorrectInfileLength() throws ValidateException {

        String s = "";
        for(int i = 0; i < MaxLength.FILE_FILENAME + 1; i++) {
            s += "a";
        }
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"smoothing\"," +
                        "\"files\":[{\"infile\":\""+s+"\"," +
                        "\"outfile\":\"awe\",\"windowSize\":\"3\"," +
                        "\"meanOrMedian\":\"median\"," +
                        "\"minSmooth\":\"2\"}]}, {\"type\":\"ratio\", \"infile1\": " +
                        "\"infile1Name\", \"infile2\": \"infile2Name\"}]}";
        ProcessCommands processCommands =
                gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    @Test(expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectOutfileName() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"smoothing\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awe¤\",\"windowSize\":\"3\"," +
                        "\"meanOrMedian\":\"mean\"," +
                        "\"minSmooth\":\"2\"}]}, {\"type\":\"ratio\", \"infile1\": " +
                        "\"infile1Name\", \"infile2\": \"infile2Name\"}]}";
        ProcessCommands processCommands =
                gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
    @Test (expected = ValidateException.class)
    public void shouldGiveValidateExceptionOnIncorrectOutfileLength() throws ValidateException {

        String s = "";
        for(int i = 0; i < MaxLength.FILE_FILENAME + 1; i++) {
            s += "a";
        }
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"smoothing\"," +
                        "\"files\":[{\"infile\":\"asdwss\"," +
                        "\"outfile\":\""+s+"\",\"windowSize\":\"3\"," +
                        "\"meanOrMedian\":\"mean\"," +
                        "\"minSmooth\":\"2\"}]}, {\"type\":\"ratio\", \"infile1\": " +
                        "\"infile1Name\", \"infile2\": \"infile2Name\"}]}";
        ProcessCommands processCommands =
                gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
    @Test (expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectMeanOrMedianName() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"smoothing\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awe\",\"windowSize\":\"3\"," +
                        "\"meanOrMedian\":\"meand\"," +
                        "\"minSmooth\":\"2\"}]}, {\"type\":\"ratio\", \"infile1\": " +
                        "\"infile1Name\", \"infile2\": \"infile2Name\"}]}";
        ProcessCommands processCommands =
                gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
    @Test (expected = ValidateException.class)
    public void shouldGiveValidateExceptionOnMinSmoothEqualOrLargerThanWindowSize() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"smoothing\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awe\",\"windowSize\":\"2\"," +
                        "\"meanOrMedian\":\"mean\"," +
                        "\"minSmooth\":\"2\"}]}, {\"type\":\"ratio\", \"infile1\": " +
                        "\"infile1Name\", \"infile2\": \"infile2Name\"}]}";
        ProcessCommands processCommands =
                gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
}