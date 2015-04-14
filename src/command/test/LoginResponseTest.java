package command.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import response.LoginResponse;


public class LoginResponseTest {

	private LoginResponse loginResponse;

	@Before
	public void setup(){
		HttpExchange httpExchange = new HttpExchange() {

			@Override
			public void setStreams(InputStream i, OutputStream o) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setAttribute(String name, Object value) {
				// TODO Auto-generated method stub

			}

			@Override
			public void sendResponseHeaders(int rCode, long responseLength)
					throws IOException {
				// TODO Auto-generated method stub

			}

			@Override
			public Headers getResponseHeaders() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getResponseCode() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public OutputStream getResponseBody() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URI getRequestURI() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getRequestMethod() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Headers getRequestHeaders() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public InputStream getRequestBody() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public InetSocketAddress getRemoteAddress() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getProtocol() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public HttpPrincipal getPrincipal() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public InetSocketAddress getLocalAddress() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public HttpContext getHttpContext() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object getAttribute(String name) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void close() {
				// TODO Auto-generated method stub

			}
		};
	}


	@Test
	@Ignore
	public void shouldCreateValidHttpHeader() {
		loginResponse = new LoginResponse(200, "324j32k");
		String result = loginResponse.toString();
		assertEquals("HTTP/1.1 200 OK", result.split("\n")[0]);
	}

	@Test
	@Ignore
	public void shouldContainValidJson() {
		loginResponse = new LoginResponse(200, "324j32k");
		String result = loginResponse.toString();
		String expected = "{\"token\":\"324j32k\"}";
		assertEquals(expected, result.substring(result.indexOf('\n')+1));
	}

	@Test
	@Ignore
	public void shouldHaveCorrectFormat() {
		loginResponse = new LoginResponse(200, "uuid");
		String result = loginResponse.toString();
		String expected = "HTTP/1.1 200 OK";
		expected += "\n{\"token\":\"uuid\"}";
		System.out.println(result);
		assertEquals(expected, result);
	}

}
