package command.test.processCommands;

import com.google.gson.Gson;
import command.ValidateException;
import command.process.*;
import database.subClasses.UserMethods;
import org.junit.Test;
import server.RequestHandler;

import static org.junit.Assert.*;

/**
 * File:        ProcessCommandsTest.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-22
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
                "\"keepSam\":\"on\"}]}, {\"type\":\"ratio\", \"infile1\": " +
                "\"infile1Name\", \"infile2\": \"infile2Name\"}]}";

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
                        "\"keepSam\":\"on\"}]}, {\"type\":\"ratio\", \"infile1\": " +
                        "\"infile1Name\", \"infile2\": \"infile2Name\"}]}";
        ProcessCommands processCommands =
                gson.fromJson(json, ProcessCommands.class);
        processCommands.setFields(null, null, null, UserMethods.UserType.USER);
        processCommands.validate();
    }
}
