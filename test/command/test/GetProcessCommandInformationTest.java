package command.test;

import java.util.*;
import java.util.concurrent.Callable;

import command.*;
import command.Process;
import command.annotation.*;
import command.process.*;
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
import response.HttpStatusCode;
import response.ProcessResponse;
import response.Response;
import server.ProcessPool;

import static org.junit.Assert.fail;

/**
 * Class used to test the process status command class
 * and that it works properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */

@SuppressWarnings("deprecation")
public class GetProcessCommandInformationTest {

	private ProcessPool processPool;

	private Process makeProcess(String author, String expId) {
		Process proc = new Process();
		proc.author = author;
		proc.experimentName = expId;
		return proc;
	}

	@Before
	public void setUp() throws Exception {

		processPool = new ProcessPool(5);
		Callable<Response> callable = new Callable<Response>() {
			@Override
			public Response call() throws Exception {
				return new ProcessResponse(HttpStatusCode.OK);
			}
		};

		processPool.addProcess(makeProcess("yuri", "Exp1"), callable);
		processPool.addProcess(makeProcess("janne", "Exp2"), callable);
		processPool.addProcess(makeProcess("philge", "Exp43"), callable);
		processPool.addProcess(makeProcess("per", "Exp234"), callable);
		processPool.addProcess(makeProcess("yuri", "Exp6"), callable);
	}

	@Test
	@Ignore
	public void shouldContainStuff() {

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Process> processesList = processPool.getProcesses();


		if (processesList.size() > 0) {

			Collections.sort(processesList);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			JsonArray arr = new JsonArray();
			for (Process p : processesList) {
				JsonElement elem = gson.toJsonTree(p, Process.class);
				arr.add(elem);
			}

			System.out.println(toPrettyFormat(arr.toString()));
		}
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
		c.setFields("uri", null, null, UserType.GUEST);
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
		c.setFields("uri", null, null, UserType.UNKNOWN);
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
