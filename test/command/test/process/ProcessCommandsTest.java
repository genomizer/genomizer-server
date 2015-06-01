package command.test.process;

import com.google.gson.Gson;
import command.ValidateException;
import command.process.*;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import org.junit.Test;
import server.RequestHandler;

import static org.junit.Assert.*;

/**
 * File:        ProcessCommandsTest.java
 * Author:      Niklas Fries, dv13jen
 */

public class ProcessCommandsTest {

    private final Gson gson = new RequestHandler().getGson();

    /**
     * Test that the list of processCommands can be created and returned with size 1
     * @throws Exception
     */
    @Test
    public void shouldReturnListWithOneCommand() throws Exception {

        String json =
                "{\"expId\":\"not_an_expid\"," +
                "\"processCommands\":[{\"type\":\"rawToProfile\"," +
                "\"files\":[{\"infile\":" +
                "\"bigtest1.fastq\",\"outfile\":\"awsd\"," +
                "\"genomeVersion\":\"theGR\",\"params\":\"" +
                "-a -m 1 --best -p 10 -v 2 -q -S\",\"keepSam\":\"on\"}]}]}";

        ProcessCommands processCommands =
                gson.fromJson(json, ProcessCommands.class);

        assertEquals(1, processCommands.getProcessCommands().size());
    }

    /**
     * Test that the list of processCommands can be created and returned with size 2. One ratio and one rawToProfile
     * @throws Exception
     */
    @Test
    public void shouldContainOneRawToProfileAndOneRatio() throws Exception {
        String json =
                "{\"expId\":\"not_an_expid\"," +
                        "\"processCommands\":[{\"type\":\"rawToProfile\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awsd\",\"genomeVersion\":\"theGR\"," +
                        "\"params\":\"-a -m 1 --best -p 10 -v 2 -q -S\"," +
                        "\"keepSam\":\"on\"}]}, {\"type\":\"ratio\", \"files\":[{\"preChipFile\": " +
                        "\"infile1Name\", \"postChipFile\": \"infile2Name\", \"outfile\": \"outfile\", " +
                        "\"mean\": \"single\", \"readsCutoff\": \"2\", \"chromosomes\": \"chromosome\"}]}]}";
        ProcessCommands processCommands =
                gson.fromJson(json, ProcessCommands.class);
        assertEquals(
                RawToProfProcessCommand.class,
                processCommands.getProcessCommands().get(0).getClass());
        assertEquals(
                RatioProcessCommand.class,
                processCommands.getProcessCommands().get(1).getClass());

    }

    /**
     * Test that creation works with correct input.
     * @throws ValidateException
     */
    @Test
    public void shouldNotGiveValidateException() throws ValidateException {
        String json =
                "{\"expId\":\"not_an_expid\"," +
                        "\"processCommands\":[{\"type\":\"rawToProfile\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awsd\",\"genomeVersion\":\"theGR\"," +
                        "\"params\":\"-a -m 1 --best -p 10 -v 2 -q -S\"," +
                        "\"keepSam\":\"on\"}]}, {\"type\":\"ratio\", \"files\":[{\"preChipFile\": " +
                        "\"infile1Name\", \"postChipFile\": \"infile2Name\", \"outfile\": \"outfile\", " +
                        "\"mean\": \"single\", \"readsCutoff\": \"2\", \"chromosomes\": \"chromosome\"}]}]}";
        ProcessCommands processCommands =
                gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test that validateException is thrown when user rights is set incorrectly.
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void shouldGiveValidateExceptionOnIncorrectUserRights() throws ValidateException {
        String json =
                "{\"expId\":\"not_an_expid\"," +
                        "\"processCommands\":[{\"type\":\"rawToProfile\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awsd\",\"genomeVersion\":\"theGR\"," +
                        "\"params\":\"-a -m 1 --best -p 10 -v 2 -q -S\"," +
                        "\"keepSam\":\"on\"}]}]}";
        ProcessCommands processCommands =
                gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.GUEST);
        processCommands.validate();
    }

    /**
     * Test that validateException is thrown with incorrect ExpId name.
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void canGiveValidateExceptionOnIncorrectExpIdName() throws ValidateException {
        String json =
                "{\"expId\":\"asd$\"," +
                        "\"processCommands\":[{\"type\":\"rawToProfile\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awsd\",\"genomeVersion\":\"theGR\"," +
                        "\"params\":\"-a -m 1 --best -p 10 -v 2 -q -S\"," +
                        "\"keepSam\":\"on\"}]}]}";
        ProcessCommands processCommands =
                gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }

    /**
     * Test that validateException is thrown with incorrect ExpId size.
     * @throws ValidateException
     */
    @Test (expected = ValidateException.class)
    public void shouldGiveValidateExceptionOnIncorrectExpIdLength() throws ValidateException {

        String s = "";
        for(int i = 0; i < MaxLength.EXPID + 1; i++) {
            s += "a";
        }
        String json =
                "{\"expId\":\""+s+"\"," +
                        "\"processCommands\":[{\"type\":\"rawToProfile\"," +
                        "\"files\":[{\"infile\":\"bigtest1.fastq\"," +
                        "\"outfile\":\"awsd\",\"genomeVersion\":\"theGR\"," +
                        "\"params\":\"-a -m 1 --best -p 10 -v 2 -q -S\"," +
                        "\"keepSam\":\"on\"}]}]}";
        ProcessCommands processCommands =
                gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
}
