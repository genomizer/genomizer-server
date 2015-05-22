package command.test.processCommands;
/**
 * File:        ProcessCommandAdapterTest.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-22
 */

import com.google.gson.Gson;
import command.process.BowtieProcessCommand;
import command.process.ProcessCommand;
import command.process.ProcessCommandAdapter;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProcessCommandAdapterTest {

    @Test
    public void shouldParseBowtieCommand() throws Exception {
        String json = "{\"type\": \"bowtie\", " +
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

        Gson gson = ProcessCommandAdapter.getProcessCommandGson();
        ProcessCommand command = gson.fromJson(json, ProcessCommand.class);
        assertEquals(BowtieProcessCommand.class, command.getClass());

        BowtieProcessCommand bowtieProcessCommand =
                (BowtieProcessCommand) command;
        BowtieProcessCommand.BowtieProcessFile file =
                bowtieProcessCommand.getFiles().get(0);

        assertEquals("infileName", file.getInfile());
        assertEquals("outfileName", file.getOutfile());
        assertEquals("theGR", file.getGenomeVersion());
        assertEquals("theParams", file.getParams());
        assertEquals(true, file.shouldKeepSam());

        System.out.println("bowtieProcessCommand = " + bowtieProcessCommand);
    }
}
