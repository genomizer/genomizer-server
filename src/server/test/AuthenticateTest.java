package server.test;

import static org.junit.Assert.*;

import org.junit.Test;

import authentication.Authenticate;

public class AuthenticateTest {

	@Test
	public void testDifferentUUID() {
		System.out.println(Authenticate.createUserID("hej"));
		System.out.println(Authenticate.createUserID("hej"));
		System.out.println(Authenticate.createUserID("hej2"));
		System.out.println(Authenticate.createUserID("litelängre"));

	}

}
