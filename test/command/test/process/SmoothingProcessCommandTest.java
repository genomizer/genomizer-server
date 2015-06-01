package command.test.process;

import com.google.gson.Gson;
import command.ValidateException;
import command.process.ProcessCommands;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import org.junit.Test;
import server.RequestHandler;

import static org.junit.Assert.assertNotNull;


public class SmoothingProcessCommandTest {
    private final Gson gson = new RequestHandler().getGson();

    /**
     * Test that command can be created.
     * @throws ValidateException
     */
    @Test
    public void canCreateWithCorrectInput() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"smoothing\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awe\",\"windowSize\":\"3\"," +
                        "\"meanOrMedian\":\"mean\"," +
                        "\"minSmooth\":\"2\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
        assertNotNull(processCommands);
    }

    /**
     * Test checks that exception can be thrown with an incorrect infile name.
     * @throws ValidateException
     */
    @Test(expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectInfileName() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"smoothing\"," +
                        "\"files\":[{\"infile\":\"bigte€st1.fastq\"," +
                        "\"outfile\":\"awe\",\"windowSize\":\"3\"," +
                        "\"meanOrMedian\":\"mean\"," +
                        "\"minSmooth\":\"2\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test checks that exception can be thrown with an incorrect infile size.
     * @throws ValidateException
     */
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
                        "\"minSmooth\":\"2\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test checks that exception can be thrown with an incorrect outfile name.
     * @throws ValidateException
     */
    @Test(expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectOutfileName() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"smoothing\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awe¤\",\"windowSize\":\"3\"," +
                        "\"meanOrMedian\":\"mean\"," +
                        "\"minSmooth\":\"2\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
    /**
     * Test checks that exception can be thrown with an incorrect outfile size.
     * @throws ValidateException
     */
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
                        "\"minSmooth\":\"2\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
    /**
     * Test checks that exception can be thrown with an incorrect meanOrMedian.
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectMeanOrMedianName() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"smoothing\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awe\",\"windowSize\":\"3\"," +
                        "\"meanOrMedian\":\"meand\"," +
                        "\"minSmooth\":\"2\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
    /**
     * Test checks that exception can be thrown with an incorrect minSmooth vs windowSize.
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void shouldGiveValidateExceptionOnMinSmoothEqualOrLargerThanWindowSize() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"smoothing\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awe\",\"windowSize\":\"2\"," +
                        "\"meanOrMedian\":\"mean\"," +
                        "\"minSmooth\":\"2\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
    /**
     * Test checks that exception can be thrown with a minSmooth being less than 0.
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void shouldGiveValidateExceptionOnMinSmoothSmallerThan0() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"smoothing\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awe\",\"windowSize\":\"2\"," +
                        "\"meanOrMedian\":\"mean\"," +
                        "\"minSmooth\":\"-1\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
    /**
     * Test checks that exception can be thrown with an incorrect windowSize being less than 0.
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void shouldGiveValidateExceptionOnWindowSizeSmallerThan0() throws ValidateException {
        String json =
                "{\"expId\":\"asd\"," +
                        "\"processCommands\":[{\"type\":\"smoothing\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awe\",\"windowSize\":\"-1\"," +
                        "\"meanOrMedian\":\"mean\"," +
                        "\"minSmooth\":\"-2\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
}