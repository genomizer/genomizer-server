package process.test;

import com.google.gson.Gson;
import command.process.ProcessCommands;
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

        ProcessCommands commands = gson.fromJson(json, ProcessCommands.class);
        commands.setPool(pool);

        commands.doProcesses();
    }

    @Test
    @Ignore
    public void shouldSmoothOneFile() throws Exception {
        String json = "{\"expId\":\"processpool_test\"," +
                      "\"processCommands\":[{\"type\":\"smooth\"," +
                      "\"files\":[{\"infile\":\"stepTestInfile.sgr\"," +
                      "\"outfile\":\"stepTestOutfile.sgr\"," +
                      "\"stepSize\":\"30\"}]}]}";

        ProcessCommands commands = gson.fromJson(json, ProcessCommands.class);
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
        ProcessCommands commands = gson.fromJson(json, ProcessCommands.class);

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
                "\"files\":[{\"infile\":\"stepTestInfile.sgr\"," +
                "\"outfile\":\"shtrhtrshts\",\"genomeVersion\":\"hg38\"," +
                "\"params\":\"-a -m 1 --best -p 10 -v 2 -q -S\"," +
                "\"keepSam\":\"on\"}]}]}";

        ProcessCommands commands = gson.fromJson(json, ProcessCommands.class);
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
                "\"readsCutOff\":5," +
                "\"chromosomes\":\"0\"" +
                "}]}]}";

        System.out.println("json = " + json);

        ProcessCommands commands = gson.fromJson(json, ProcessCommands.class);

        commands.setPool(pool);

        commands.doProcesses();
    }
}
