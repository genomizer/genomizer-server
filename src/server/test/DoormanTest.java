package server.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import server.Doorman;

public class DoormanTest {

	private Doorman doorman;

	@Before
	public void setUp() throws Exception {
		doorman = new Doorman(new CommandHandlerDummy(), 8080);
		doorman.start();
	}


	@Test
	public void shouldAcceptRequests() throws Exception {
		HttpURLConnection httpcon = request("");
		httpcon.getResponseCode();
	}

	private HttpURLConnection request(String path) throws Exception {
		URL url = new URL("http://localhost:8080/" + path);
		return (HttpURLConnection) url.openConnection();
	}
}
