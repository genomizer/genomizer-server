package process.tests;

import static org.junit.Assert.*;

import java.io.IOException;


import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import process.classes.Executor;
import process.classes.RawToProfileConverter;

public class RawToProfileTest {
	RawToProfileConverter rtp = null;

	@Before
	public void setup() {
		rtp = new RawToProfileConverter();
	}

	@After
	public void tearDown() {
		rtp = null;
	}

//	@Test(expected = IOException.class)
//	public void ExceptedIOException() {
//		rtp.execute(new String[]{"HEJ"});
//	}

	@Test
	public void StandardShouldGetString() {
		rtp.standardParamProcedure(new String[]{""});
	}

	@Test
	public void SpecificShouldGetString() {
		rtp.specificParamProcedure(new String[]{""});
	}
}