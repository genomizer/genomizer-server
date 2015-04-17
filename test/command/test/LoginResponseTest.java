package command.test;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import response.LoginResponse;

@Ignore
public class LoginResponseTest {

	private LoginResponse loginResponse;

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
