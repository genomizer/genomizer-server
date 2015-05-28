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
    }

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

}