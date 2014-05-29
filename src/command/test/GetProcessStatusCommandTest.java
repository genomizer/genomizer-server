package command.test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import command.ProcessCommand;
import command.ProcessStatus;
import server.WorkHandler;
import server.test.ProcessCommandMock;

/**
 * Class used to test the process status command class
 * and that it works properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class GetProcessStatusCommandTest {

	private static WorkHandler workHandler = new WorkHandler();
	private ProcessCommand com;
	private ProcessStatus stat;

	private ProcessCommand makeCmd(String author, String metadata, String genomeVersion, String expId) {
		JsonObject comInfo = new JsonObject();
		comInfo.addProperty("expid", expId);

		JsonArray arr = new JsonArray();
		for (int i = 0; i < 8; i++) {
			arr.add(new JsonPrimitive(""));
		}

		comInfo.add("parameters", arr);
		comInfo.addProperty("metadata", metadata);
		comInfo.addProperty("genomeVersion", genomeVersion);
		comInfo.addProperty("author", author);
		//System.out.println(comInfo.toString());
		return new GsonBuilder().create().fromJson(comInfo.toString(), ProcessCommandMock.class);
	}

	@Before
	public void setUp() throws Exception {


		workHandler.addWork(makeCmd("yuri", "meta", "v123", "Exp1"));
		workHandler.addWork(makeCmd("janne", "mea", "v1523", "Exp2"));
		workHandler.addWork(makeCmd("philge", "meta", "v22", "Exp43"));
		workHandler.addWork(makeCmd("per", "meta", "v12", "Exp234"));
		workHandler.addWork(makeCmd("yuri", "meta", "v1", "Exp6"));

		//stat = new ProcessStatus(com);
		//stat.outputFiles = com.getFilePaths();

	}

	@Test
	public void shouldContainStuff() {

		workHandler.start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Collection<ProcessStatus> procStats = workHandler.getProcessStatus();
		List<ProcessStatus> list = new ArrayList<ProcessStatus>( procStats);

		Collections.sort( list );
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		JsonArray arr = new JsonArray();
		for (ProcessStatus p : list) {
			JsonElement elem = gson.toJsonTree(p, ProcessStatus.class);
			arr.add(elem);
		}

		System.out.println(toPrettyFormat(arr.toString()));
//		workHandler.interrupt();
	}

    private static String toPrettyFormat(String jsonString)
    {
        JsonParser parser = new JsonParser();
        JsonArray json = parser.parse(jsonString).getAsJsonArray();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }

}
