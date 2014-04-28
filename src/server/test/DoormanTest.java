package server.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import server.Doorman;

public class DoormanTest {

	private Doorman doorman;
	
	@Before
	public void setUp() throws Exception {
		doorman = new Doorman(new CommandHandlerDummy(), 8080);
	}
	
	

	@Test
	public void shouldConnectOn8080() {
		URL url;
		HttpURLConnection httpcon = null;
		try {
			url = new URL("http://localhost:8000/test");
			httpcon = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			fail();
		}
		try {
			httpcon.connect();
		} catch (IOException e) {
			fail();
		}
		assertNotNull(httpcon);
	}

}
