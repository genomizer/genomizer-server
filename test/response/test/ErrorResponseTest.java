package response.test;

import static org.junit.Assert.*;

import org.junit.Test;

import response.ErrorResponse;
import response.Response;
import response.StatusCode;

public class ErrorResponseTest {

	@Test
	public void shouldHaveCorrectCode() {
		assertEquals(404, new ErrorResponse(StatusCode.NOT_FOUND, "").getCode());
	}
	
	@Test
	public void shouldGenerateJsonMessage() {
		Response resp = new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, "Error text");
		String expected = "{\"message\":\"Error text\"}\n";
		assertEquals(expected, resp.getBody());
	}

}
