package process.test;


import static org.junit.Assert.*;

import java.io.IOException;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import process.classes.Executor;
import process.classes.RawToProfileConverter;
import process.classes.SmoothingParameterChecker;
public class SmoothingParameterCheckerTest {
	private SmoothingParameterChecker spc;
	private String parameters;

	@After
	public void tearDown() {
		spc = null;
	}

	@Test
	public void shouldFailIfParametersAreNull() {
		spc = SmoothingParameterChecker.SmoothingParameterCheckerFactory(null);
		assertEquals(false, spc.checkSmoothParams());
	}

	@Test
	public void shouldFailIfParameterContainsTooFewValues() {
		spc = SmoothingParameterChecker.SmoothingParameterCheckerFactory("1");
		assertEquals(false, spc.checkSmoothParams());
	}


	@Test
	public void shouldFailIfParameterContainsTooManyValues() {
		spc = SmoothingParameterChecker.SmoothingParameterCheckerFactory("1 1 1 1 1 1");
		assertEquals(false, spc.checkSmoothParams());
	}

	@Test
	public void shouldFailIfParameterContainsUndefinedTypeOfSmoothing() {
		spc = SmoothingParameterChecker.SmoothingParameterCheckerFactory("1 2 1 1 1");
		assertEquals(false, spc.checkSmoothParams());
	}

	@Test
	public void shouldSucceedWithDefaultParameters() {
		spc = SmoothingParameterChecker.SmoothingParameterCheckerFactory("10 1 5 0 0");
		assertEquals(true, spc.checkSmoothParams());
	}

	@Test
	public void shouldReturnCorrectWindowSize() {
		spc = SmoothingParameterChecker.SmoothingParameterCheckerFactory("10 1 5 0 0");
		assertEquals("10", spc.getWindowSize());
	}

	@Test
	public void shouldReturnCorrectMinProbe() {
		spc = SmoothingParameterChecker.SmoothingParameterCheckerFactory("10 1 5 0 0");
		assertEquals("5", spc.getMinProbe());
	}


}
