package command.test.processCommands;
/**
 * File:        ProcessCommandAdapterTest.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-22
 */

import com.google.gson.Gson;
import command.process.RawToProfProcessCommand;
import command.process.ProcessCommand;
import command.process.ProcessCommandAdapter;
import org.junit.Test;
import server.RequestHandler;

import static org.junit.Assert.*;

public class ProcessCommandAdapterTest {

    private final Gson gson = new RequestHandler().getGson();

    @Test
    public void shouldParseBowtieCommand() throws Exception {
        String json = "{\"type\": \"rawToProfile\", " +
                      "\"files\": [" +
                      "{" +
                      "\"infile\": \"infileName\"," +
                      "\"outfile\": \"outfileName\"," +
                      "\"genomeVersion\": \"theGR\"," +
                      "\"params\": \"theParams\"," +
                      "\"keepSam\": true" +
                      "}" +
                      "]" +
                      "}";

        ProcessCommand command = gson.fromJson(json, ProcessCommand.class);
        assertEquals(RawToProfProcessCommand.class, command.getClass());

        RawToProfProcessCommand rawToProfProcessCommand =
                (RawToProfProcessCommand) command;
        RawToProfProcessCommand.RawToProfProcessFile file =
                rawToProfProcessCommand.getFiles().get(0);

        assertEquals("infileName", file.getInfile());
        assertEquals("outfileName", file.getOutfile());
        assertEquals("theGR", file.getGenomeVersion());
        assertEquals("theParams", file.getParams());
        assertEquals(true, file.shouldKeepSam());

        System.out.println("bowtieProcessCommand = " + rawToProfProcessCommand);
    }
}
