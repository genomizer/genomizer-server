package command.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
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
import server.ErrorLogger;
import server.WorkHandler;
import server.WorkPool;
import server.test.dummies.ProcessCommandMock;

/**
 * Class used to test the process status command class
 * and that it works properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
@Ignore
public class GetProcessStatusCommandTest {

	private static WorkHandler workHandler;
	private WorkPool workPool;

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

		workPool = new WorkPool();
		workHandler = new WorkHandler(workPool);

		workPool.addWork(makeCmd("yuri", "meta", "v123", "Exp1"));
		workPool.addWork(makeCmd("janne", "mea", "v1523", "Exp2"));
		workPool.addWork(makeCmd("philge", "meta", "v22", "Exp43"));
		workPool.addWork(makeCmd("per", "meta", "v12", "Exp234"));
		workPool.addWork(makeCmd("yuri", "meta", "v1", "Exp6"));

		//stat = new ProcessStatus(com);
		//stat.outputFiles = com.getFilePaths();

	}

	@Test
	@Ignore
	public void shouldContainStuff() {

		new Thread(workHandler).start();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Collection<ProcessStatus> procStats = null;

		try {
			procStats = workPool.getProcesses().values();

		} catch (InterruptedException e) {
			ErrorLogger.log("SYSTEM", "Error acquiring processes: " +
					e.getMessage());
		}

		if (procStats != null) {
			List<ProcessStatus> list = new ArrayList<ProcessStatus>( procStats);

			Collections.sort( list );
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			JsonArray arr = new JsonArray();
			for (ProcessStatus p : list) {
				JsonElement elem = gson.toJsonTree(p, ProcessStatus.class);
				arr.add(elem);
			}

			System.out.println(toPrettyFormat(arr.toString()));
		}


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
