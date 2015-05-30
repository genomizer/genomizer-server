package command.test.process;

import com.google.gson.Gson;
import command.ValidateException;
import command.process.*;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import org.junit.Ignore;
import org.junit.Test;
import server.RequestHandler;

import static org.junit.Assert.*;

/**
 * File:        ProcessCommandsTest.java
 * Author:      Niklas Fries, dv13jen
 */

public class ProcessCommandsTest {

    private final Gson gson = new RequestHandler().getGson();

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
        System.out.println("processCommands = " + processCommands);
    }

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
        System.out.println("processCommands = " + processCommands);

    }

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

    @Test (expected = ValidateException.class)
    @Ignore
    public void shouldThrowValidateExceptionOnIncorrectCommandOrder() throws ValidateException {
        String json = "{ \"expId\": \"anExpId\",\n" +
                "  \"processCommands\": [ \n" +
                "  { \"type\": \"smoothing\",\n" +
                "    \"files\": [ { \"infile\": \"infileName\",\n" +
                "                 \"outfile\": \"outfileName\",\n" +
                "                 \"windowSize\": 10,\n" +
                "                 \"meanOrMedian\": \"mean\",\n" +
                "                 \"minSmooth\": 5 }\n" +
                "             ]\n" +
                "  },\n" +
                "  { \"type\": \"rawToProfile\",\n" +
                "    \"files\": [ { \"infile\": \"in.fastq\",\n" +
                "                 \"outfile\": \"out.fastq\",\n" +
                "                 \"params\": \"-a -m 1\",\n" +
                "                 \"genomeVersion\": \"aGR\",\n" +
                "                 \"keepSam\": true,\n" +
                "                 \"sortSamStringency\": \"STRICT\"\n" +
                "             } ]   \n" +
                "  }\n" +
                "] }";
        ProcessCommands processCommands =
                gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
}
