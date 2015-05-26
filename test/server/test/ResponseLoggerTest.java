package server.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import authentication.Authenticate;

import response.HttpStatusCode;
import response.MinimalResponse;
import server.ErrorLogger;

public class ResponseLoggerTest {


	@Before
	public void setUp() throws Exception {
		ErrorLogger.reset();

	}

	@After
	public void tearDown() throws Exception {
		ErrorLogger.reset();
	}

	@Test
	public void shouldLogBADREQUESTandOKResponse(){
		String username = "splutt";
		Authenticate.updateActiveUser(username);
		new MinimalResponse(HttpStatusCode.BAD_REQUEST);
		new MinimalResponse(HttpStatusCode.OK);
//
//		if(ErrorLogger.log(username, r1) && ErrorLogger.log(username, r2)){
//			assertEquals(HttpStatusCode.BAD_REQUEST, ErrorLogger.getUserLog(username).get(0).getCode());
//			assertEquals(HttpStatusCode.OK, ErrorLogger.getUserLog(username).get(1).getCode());
//			ErrorLogger.printUserLog("splutt");
//		}else{
//			fail();
//		}
	}



	@Test
	public void shouldLogBADREQUESTError() {
		//		ResponseLogger logger = new ResponseLogger();
		//String username = "splutt";

		new MinimalResponse(HttpStatusCode.BAD_REQUEST);

//		if(ErrorLogger.log(username, r)){
//			assertEquals(HttpStatusCode.BAD_REQUEST, ErrorLogger.getUserLog(username).get(0).getCode());
//		}else{
//			fail();
//		}

		ErrorLogger.printUserLog("splutt");

	}
	@Test
	public void shouldLog2BADREQUESTErrors(){
		//		ResponseLogger logger = new ResponseLogger();

		//String username = "splutt";
		new MinimalResponse(HttpStatusCode.BAD_REQUEST);
		new MinimalResponse(HttpStatusCode.BAD_REQUEST);
//
//		if(ErrorLogger.log(username, r1) && ErrorLogger.log(username, r2)){
//			assertEquals(HttpStatusCode.BAD_REQUEST, ErrorLogger.getUserLog(username).get(0).getCode());
//			assertEquals(HttpStatusCode.BAD_REQUEST, ErrorLogger.getUserLog(username).get(1).getCode());
//		}else{
//			fail();
//		}
		ErrorLogger.printUserLog("splutt");

	}

}
