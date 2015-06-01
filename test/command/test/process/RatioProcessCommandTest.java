package command.test.process;

import com.google.gson.Gson;
import command.ValidateException;
import command.process.ProcessCommands;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import org.junit.Test;
import server.RequestHandler;

import static org.junit.Assert.*;

public class RatioProcessCommandTest {
    private final Gson gson = new RequestHandler().getGson();

    /**
     * Test that command can be created.
     * @throws ValidateException
     */
    @Test
    public void canCreateWithCorrectInput() throws ValidateException {
        String json =
                "{\"expId\":\"not_an_expid\"," +
                        "\"processCommands\":[{\"type\":\"ratio\", \"files\":[{\"preChipFile\": " +
                        "\"infile1Name\", \"postChipFile\": \"infile2Name\", \"outfile\": \"outfile\", " +
                        "\"mean\": \"single\", \"readsCutoff\": \"2\", \"chromosomes\": \"chromosome\"}]}]}";

        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();

        assertNotNull(processCommands);
    }

    /**
     * Test checks that exception can be thrown with an incorrect preChipFile name.
     * @throws ValidateException
     */
    @Test(expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectPreChipFileName() throws ValidateException {
        String json =
                "{\"expId\":\"not_an_expid\"," +
                        "\"processCommands\":[{\"type\":\"ratio\", \"files\":[{\"preChipFile\": " +
                        "\"&infile1Name\", \"postChipFile\": \"infile2Name\", \"outfile\": \"outfile\", " +
                        "\"mean\": \"single\", \"readsCutoff\": \"2\", \"chromosomes\": \"chromosome\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test checks that exception can be thrown with an incorrect preChipFile size.
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void shouldGiveValidateExceptionOnIncorrectPreChipFileLength() throws ValidateException {

        String s = "";
        for(int i = 0; i < MaxLength.FILE_FILENAME + 1; i++) {
            s += "a";
        }
        String json =
                "{\"expId\":\"not_an_expid\"," +
                        "\"processCommands\":[{\"type\":\"ratio\", \"files\":[{\"preChipFile\": " +
                        "\""+s+"\", \"postChipFile\": \"infile2Name\", \"outfile\": \"outfile\", " +
                        "\"mean\": \"single\", \"readsCutoff\": \"2\", \"chromosomes\": \"chromosome\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test checks that exception can be thrown with an incorrect postChipFile name.
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectPostChipFileName() throws ValidateException {
        String json =
                "{\"expId\":\"not_an_expid\"," +
                        "\"processCommands\":[{\"type\":\"ratio\", \"files\":[{\"preChipFile\": " +
                        "\"infile1Name\", \"postChipFile\": \"infile€2Name\", \"outfile\": \"outfile\", " +
                        "\"mean\": \"single\", \"readsCutoff\": \"2\", \"chromosomes\": \"chromosome\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test checks that exception can be thrown with an incorrect postChipFile size.
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void shouldGiveValidateExceptionOnIncorrectPostChipFileLength() throws ValidateException {

        String s = "";
        for(int i = 0; i < MaxLength.FILE_FILENAME + 1; i++) {
            s += "a";
        }
        String json =
                "{\"expId\":\"not_an_expid\"," +
                        "\"processCommands\":[{\"type\":\"ratio\", \"files\":[{\"preChipFile\": " +
                        "\"awwdw\", \"postChipFile\": \""+s+"\", \"outfile\": \"outfile\", " +
                        "\"mean\": \"single\", \"readsCutoff\": \"2\", \"chromosomes\": \"chromosome\"}]}]}";
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
                "{\"expId\":\"not_an_expid\"," +
                        "\"processCommands\":[{\"type\":\"ratio\", \"files\":[{\"preChipFile\": " +
                        "\"infile1Name\", \"postChipFile\": \"infile2Name\", \"outfile\": \"in$\", " +
                        "\"mean\": \"single\", \"readsCutoff\": \"2\", \"chromosomes\": \"chromosome\"}]}]}";
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
                "{\"expId\":\"not_an_expid\"," +
                        "\"processCommands\":[{\"type\":\"ratio\", \"files\":[{\"preChipFile\": " +
                        "\"awwdw\", \"postChipFile\": \"dwqdwqs\", \"outfile\": \""+s+"\", " +
                        "\"mean\": \"single\", \"readsCutoff\": \"2\", \"chromosomes\": \"chromosome\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test checks that exception can be thrown with null chromosomes.
     * @throws ValidateException
     */
    @Test(expected = ValidateException.class)
    public void canGiveValidateExceptionOnChromosomesNull() throws ValidateException {
        String json =
                "{\"expId\":\"not_an_expid\"," +
                        "\"processCommands\":[{\"type\":\"ratio\", \"files\":[{\"preChipFile\": " +
                        "\"infile1Name\", \"postChipFile\": \"infile2Name\", \"outfile\": \"in\", " +
                        "\"mean\": \"single\", \"readsCutoff\": \"2\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test checks that exception can be thrown with an incorrect mean.
     * @throws ValidateException
     */
    @Test(expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectMean() throws ValidateException {
        String json =
                "{\"expId\":\"not_an_expid\"," +
                        "\"processCommands\":[{\"type\":\"ratio\", \"files\":[{\"preChipFile\": " +
                        "\"infile1Name\", \"postChipFile\": \"infile2Name\", \"outfile\": \"in\", " +
                        "\"mean\": \"asd\", \"readsCutoff\": \"2\", \"chromosomes\": \"chromosome\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test checks that exception can be thrown with readsCutoff less than 0.
     * @throws ValidateException
     */
    @Test(expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectReadsCutoff() throws ValidateException {
        String json =
                "{\"expId\":\"not_an_expid\"," +
                        "\"processCommands\":[{\"type\":\"ratio\", \"files\":[{\"preChipFile\": " +
                        "\"infile1Name\", \"postChipFile\": \"infile2Name\", \"outfile\": \"in\", " +
                        "\"mean\": \"double\", \"readsCutoff\": \"-1\", \"chromosomes\": \"chromosome\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test checks that exception can be thrown with null readsCutoff.
     * @throws ValidateException
     */
    @Test(expected = ValidateException.class)
    public void canGiveValidateExceptionOnNullReadsCutoff() throws ValidateException {
        String json =
                "{\"expId\":\"not_an_expid\"," +
                        "\"processCommands\":[{\"type\":\"ratio\", \"files\":[{\"preChipFile\": " +
                        "\"infile1Name\", \"postChipFile\": \"infile2Name\", \"outfile\": \"in\", " +
                        "\"mean\": \"double\", \"chromosomes\": \"chromosome\"}]}]}";
        ProcessCommands processCommands = gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
}