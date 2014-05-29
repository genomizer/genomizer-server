package server.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import authentication.Authenticate;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import server.ErrorLogger;

import command.CommandFactory;
import command.ProcessCommand;

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
		Authenticate.addUser(username);
		Response r1 = new MinimalResponse(StatusCode.BAD_REQUEST);
		Response r2 = new MinimalResponse(StatusCode.OK);
//
//		if(ErrorLogger.log(username, r1) && ErrorLogger.log(username, r2)){
//			assertEquals(StatusCode.BAD_REQUEST, ErrorLogger.getUserLog(username).get(0).getCode());
//			assertEquals(StatusCode.OK, ErrorLogger.getUserLog(username).get(1).getCode());
//			ErrorLogger.printUserLog("splutt");
//		}else{
//			fail();
//		}
	}



	@Test
	public void shouldLogBADREQUESTError() {
		//		ResponseLogger logger = new ResponseLogger();
		String username = "splutt";

		Response r = new MinimalResponse(StatusCode.BAD_REQUEST);

//		if(ErrorLogger.log(username, r)){
//			assertEquals(StatusCode.BAD_REQUEST, ErrorLogger.getUserLog(username).get(0).getCode());
//		}else{
//			fail();
//		}

		ErrorLogger.printUserLog("splutt");

	}
	@Test
	public void shouldLog2BADREQUESTErrors(){
		//		ResponseLogger logger = new ResponseLogger();

		String username = "splutt";
		Response r1 = new MinimalResponse(StatusCode.BAD_REQUEST);
		Response r2 = new MinimalResponse(StatusCode.BAD_REQUEST);
//
//		if(ErrorLogger.log(username, r1) && ErrorLogger.log(username, r2)){
//			assertEquals(StatusCode.BAD_REQUEST, ErrorLogger.getUserLog(username).get(0).getCode());
//			assertEquals(StatusCode.BAD_REQUEST, ErrorLogger.getUserLog(username).get(1).getCode());
//		}else{
//			fail();
//		}
		ErrorLogger.printUserLog("splutt");

	}

}
