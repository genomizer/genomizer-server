package command.test.process;
/**
 * File:        ProcessCommandAdapterTest.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-22
 */

import com.google.gson.Gson;
import command.process.RawToProfProcessCommand;
import command.process.ProcessCommand;
import org.junit.Test;
import server.RequestHandler;

import static org.junit.Assert.*;

public class ProcessCommandAdapterTest {

    private final Gson gson = new RequestHandler().getGson();

    /**
     * Test that command parses as raw to profile.
     * @throws Exception
     */
    @Test
    public void shouldParseRawToProfCommand() throws Exception {
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

    }

    /**
     * Test that parsing of as raw to profile infile works.
     * @throws Exception
     */
    @Test
    public void shouldParseRawToProfCommandInfile() throws Exception {
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

        RawToProfProcessCommand rawToProfProcessCommand =
                (RawToProfProcessCommand) command;
        RawToProfProcessCommand.RawToProfProcessFile file =
                rawToProfProcessCommand.getFiles().get(0);

        assertEquals("infileName", file.getInfile());
    }

    /**
     * Test that parsing of as raw to profile outfile works.
     * @throws Exception
     */
    @Test
    public void shouldParseRawToProfCommandOutfile() throws Exception {
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

        RawToProfProcessCommand rawToProfProcessCommand =
                (RawToProfProcessCommand) command;
        RawToProfProcessCommand.RawToProfProcessFile file =
                rawToProfProcessCommand.getFiles().get(0);

        assertEquals("outfileName", file.getOutfile());
    }

    /**
     * Test that parsing of as raw to profile genomeVersion works.
     * @throws Exception
     */
    @Test
    public void shouldParseRawToProfCommandGenomeVersion() throws Exception {
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

        RawToProfProcessCommand rawToProfProcessCommand =
                (RawToProfProcessCommand) command;
        RawToProfProcessCommand.RawToProfProcessFile file =
                rawToProfProcessCommand.getFiles().get(0);

        assertEquals("theGR", file.getGenomeVersion());
    }

    /**
     * Test that parsing of as raw to profile params works.
     * @throws Exception
     */
    @Test
    public void shouldParseRawToProfParams() throws Exception {
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

        RawToProfProcessCommand rawToProfProcessCommand =
                (RawToProfProcessCommand) command;
        RawToProfProcessCommand.RawToProfProcessFile file =
                rawToProfProcessCommand.getFiles().get(0);

        assertEquals("theParams", file.getParams());
    }

    /**
     * Test that parsing of as raw to profile keepSam works.
     * @throws Exception
     */
    @Test
    public void shouldParseRawToProfKeepSam() throws Exception {
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

        RawToProfProcessCommand rawToProfProcessCommand =
                (RawToProfProcessCommand) command;
        RawToProfProcessCommand.RawToProfProcessFile file =
                rawToProfProcessCommand.getFiles().get(0);

        assertEquals(true, file.shouldKeepSam());
    }
}
