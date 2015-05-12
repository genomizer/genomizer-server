package command.test;

import java.util.*;

import command.*;
import command.Process;
import command.annotation.GetAnnotationPrivilegesCommand;
import command.process.PutProcessCommand;
import database.subClasses.UserMethods.UserType;
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
import server.WorkHandler;
import server.WorkPool;
import server.test.dummies.PutProcessCommandMock;

import static org.junit.Assert.fail;

/**
 * Class used to test the process status command class
 * and that it works properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */

public class GetProcessCommandInformationTest {

	private static WorkHandler workHandler;
	private WorkPool workPool;

	private PutProcessCommand makeCmd(String author, String metadata, String genomeVersion, String expId) {
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

		return new GsonBuilder().create().fromJson(comInfo.toString(), PutProcessCommandMock.class);
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

		//stat = new Process(com);
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

		LinkedList<PutProcessCommand> processesList = workPool.getProcesses();
		LinkedList<Process> getProcessStatuses = new LinkedList<>();

		for (PutProcessCommand proc : processesList) {
			getProcessStatuses.add(workPool.getProcessStatus(proc));
		}


		if (getProcessStatuses.size() > 0) {

			Collections.sort(getProcessStatuses);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			JsonArray arr = new JsonArray();
			for (Process p : getProcessStatuses) {
				JsonElement elem = gson.toJsonTree(p, Process.class);
				arr.add(elem);
			}

			System.out.println(toPrettyFormat(arr.toString()));
		}


//		workHandler.interrupt();
	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		Command c = new GetAnnotationPrivilegesCommand();
		c.setFields("uri", null, UserType.GUEST);
		c.validate();
	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when the user doesn't have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testNotHavingRights() throws ValidateException {

		Command c = new GetAnnotationPrivilegesCommand();
		c.setFields("uri", null, UserType.UNKNOWN);
		c.validate();
		fail();
	}

    private static String toPrettyFormat(String jsonString)
    {
        JsonParser parser = new JsonParser();
        JsonArray json = parser.parse(jsonString).getAsJsonArray();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

		return gson.toJson(json);
    }

}
