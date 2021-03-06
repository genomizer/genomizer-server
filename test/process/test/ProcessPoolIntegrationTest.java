package process.test;

import com.google.gson.Gson;
import command.process.PutProcessCommands;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import server.ProcessPool;
import server.RequestHandler;
import server.ServerSettings;

/**
 * File:        ProcessPoolIntegrationTest.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-29
 */

public class ProcessPoolIntegrationTest {

    private final Gson gson = new RequestHandler().getGson();
    private final ProcessPool pool = new ProcessPool(1);
    private static final String[] STEP_OUT_FILES = {
            "stepTestInfile.sgr",
            "step10.sgr",
            "step15.sgr",
            "step20.sgr",
            "step25.sgr",
            "step30.sgr",
            "step35.sgr",
            "step40.sgr",
            "step45.sgr"
    };
    private static final int[] STEP_SIZES = {10, 15, 20, 25, 30, 35, 40, 45};

    @Before
    public void setUp() throws Exception {
        ServerSettings.readSettingsFile("niklas_settings.cfg");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    @Ignore
    public void shouldStepOneFile() throws Exception {
        String json = "{\"expId\":\"processpool_test\"," +
                      "\"processCommands\":[{\"type\":\"step\"," +
                      "\"files\":[{\"infile\":\"stepTestInfile.sgr\"," +
                      "\"outfile\":\"stepTestOutfile.sgr\"," +
                      "\"stepSize\":\"30\"}]}]}";

        PutProcessCommands commands = gson.fromJson(json, PutProcessCommands.class);
        commands.setPool(pool);

        commands.doProcesses();
    }

    @Test
    @Ignore
    public void shouldSmoothOneFile() throws Exception {
        String json = "{\"expId\":\"processpool_test\"," +
                      "\"processCommands\":[{\"type\":\"smoothing\"," +
                      "\"files\":[{" +
                      "\"infile\":\"SGR-testdata-2.sgr\"," +
                      "\"outfile\":\"smoothOutfile.sgr\"," +
                      "\"windowSize\":\"10\"," +
                      "\"meanOrMedian\":\"mean\"," +
                      "\"minSmooth\":\"5\"" +
                      "}]}]}";

        System.out.println("json = " + json);

        PutProcessCommands commands = gson.fromJson(json, PutProcessCommands.class);
        commands.setPool(pool);

        commands.doProcesses();
    }

    @Test
    @Ignore
    public void shouldStepAllFilesSequentially() throws Exception {
        StringBuilder jsonBuilder = new StringBuilder(
                "{\"expId\":\"processpool_test\",\"processCommands\": [");
        for (int i = 0; i < STEP_OUT_FILES.length - 1; i++) {
            jsonBuilder.append(
                    stepCommand(
                            STEP_OUT_FILES[i],
                            STEP_OUT_FILES[i + 1],
                            STEP_SIZES[i]));
            if (i < STEP_OUT_FILES.length - 2) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]}");
        String json = jsonBuilder.toString();
        System.out.println("json = " + json);
        PutProcessCommands commands = gson.fromJson(json, PutProcessCommands.class);

        commands.setPool(pool);
        commands.doProcesses();
    }

    private String stepCommand(
            String stepInfile,
            String stepOutfile,
            int stepSize) {
        return "{\"type\":\"step\",\"files\":[" + "{\"infile\":\"" +
               stepInfile + "\"," + "\"outfile\":\"" + stepOutfile + "\"," +
               "\"stepSize\":" + stepSize + "}]}";
    }

    @Test
    @Ignore
    public void shouldRawToProfOneFile() throws Exception {
        String json =
                "{\"expId\":\"processpool_test\"," +
                "\"processCommands\":[{\"type\":\"rawToProfile\"," +
                "\"files\":[{" +
                "\"infile\":\"smalltest1.fastq\"," +
                "\"outfile\":\"smalltest1.wig\"," +
                "\"genomeVersion\":\"GenomV1\"," +
                "\"params\":\"-a -S\"," +
                "\"keepSam\":true" +
                "}]}]}";

        System.out.println("json = " + json);

        PutProcessCommands commands = gson.fromJson(json, PutProcessCommands.class);
        commands.setPool(pool);

        commands.doProcesses();
    }

    @Test
    @Ignore
    public void shouldRatioOneFile() throws Exception {
        String json =
                "{\"expId\":\"processpool_test\"," +
                "\"processCommands\":[{\"type\":\"ratio\"," +
                "\"files\":[{\"preChipFile\":\"stepTestInfile.sgr\",\"postChipFile" +
                "\":\"stepTestInfile.sgr\"," +
                "\"outfile\":\"ratioOutFile.sgr\"," +
                "\"mean\":\"single\"," +
                "\"readsCutoff\":5," +
                "\"chromosomes\":\"0\"" +
                "}]}]}";

        System.out.println("json = " + json);

        PutProcessCommands commands = gson.fromJson(json, PutProcessCommands.class);

        commands.setPool(pool);

        commands.doProcesses();
    }
}
