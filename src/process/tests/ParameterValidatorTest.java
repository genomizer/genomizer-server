package process.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import process.classes.ParameterValidator;
import process.classes.ProcessException;

public class ParameterValidatorTest {

	ParameterValidator pv;
	String correctSmoothingParams;
	
	@Before
	public void setup() {
		pv = new ParameterValidator();
		correctSmoothingParams = "1 1 1 1 1";
	}

	@After
	public void tearDown() {
		pv = null;
	}

	// --- Tests for validateSmoothing ---
	@Test
	public void ShouldReturnTrue() throws ProcessException {
		assertTrue(pv.validateSmoothing(("10 1 5 0 1")));
	}

	@Test (expected = ProcessException.class)
	public void ShouldReturnFalseWhenFirstParameterIsDecimal() throws ProcessException {
		pv.validateSmoothing("1,3 1 1 1 1");
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailFalseWhenSecondParameterIsDecimal() throws ProcessException {
		pv.validateSmoothing("1 1.3 1 1 1");
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailFalseWhenThirdParameterIsDecimal() throws ProcessException {
		pv.validateSmoothing("1 1 1,3 1 1");
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenFourthParameterIsDecimal() throws ProcessException {
		pv.validateSmoothing("1 1 1 1,4 1");
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenFifthParameterIsDecimal() throws ProcessException {
		assertTrue(pv.validateSmoothing("1 1 1 1 1,2"));
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenFirstParameterIsNotANumber() throws ProcessException {
		assertTrue(pv.validateSmoothing("A 1 1 1 1"));
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenSecondParameterIsNotANumber() throws ProcessException {
		assertTrue(pv.validateSmoothing("1 A 1 1 1"));
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenThirdParameterIsNotANumber() throws ProcessException {
		assertTrue(pv.validateSmoothing("1 1 A 1 1"));
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenFourthParameterIsNotANumber() throws ProcessException {
		assertTrue(pv.validateSmoothing("1 1 1 A 1"));
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenFifthParameterIsNotANumber() throws ProcessException {
		assertTrue(pv.validateSmoothing("1 1 1 1 A"));
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenFirstParameterIsBelowZero() throws ProcessException {
		pv.validateSmoothing("-1 1 1 1 1");
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenSecondParameterIsBelowZero() throws ProcessException {
		pv.validateSmoothing("1 -1 1 1 1");
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenThirdParameterIsBelowZero() throws ProcessException {
		pv.validateSmoothing("1 1 -1 1 1");
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenFourthParameterIsBelowZero() throws ProcessException {
		pv.validateSmoothing("1 1 1 -1 1");
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenFifthParameterIsBelowZero() throws ProcessException {
		pv.validateSmoothing("1 1 1 1 -1");
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenSecondParameterIsNotOneOrZero() throws ProcessException {
		pv.validateSmoothing("1 2 1 1 1");
	}

	@Test
	public void ShouldSucceedWhenSecondParameterIsOne() throws ProcessException {
		assertTrue(pv.validateSmoothing("1 1 1 1 1"));
	}

	@Test
	public void ShouldSucceedWhenFourthParameterIsOne() throws ProcessException {
		assertTrue(pv.validateSmoothing("1 1 1 1 1"));
	}

	@Test
	public void ShouldSucceedWhenFifthParameterIsOne() throws ProcessException {
		assertTrue(pv.validateSmoothing("1 1 1 1 1"));
	}

	@Test
	public void ShouldSucceedWhenSecondParameterIsZero() throws ProcessException {
		assertTrue(pv.validateSmoothing("1 0 1 1 1"));
	}

	@Test
	public void ShouldSucceedWhenFourthParameterIsZero() throws ProcessException {
		assertTrue(pv.validateSmoothing("1 1 1 0 1"));
	}

	@Test
	public void ShouldSucceedWhenFifthParameterIsZero() throws ProcessException {
		assertTrue(pv.validateSmoothing("1 1 1 1 0"));
	}

	// --- Tests for validateStep ---
	@Test (expected = ProcessException.class)
	public void ShouldFailWhenParameterIsNotANumber() throws ProcessException {
		assertTrue(pv.validateStep("y test"));
	}

	@Test
	public void ShouldSucceedWhenParameterIsNumber() throws ProcessException {
		assertTrue(pv.validateStep("y 10"));
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenParameterIsFloat() throws ProcessException {
		assertTrue(pv.validateSmoothing("y 10,5"));
	}

	// --- Tests validateRatioCalculation ---
	@Test
	public void ShouldSucceedWhenFirstParameterIsSingle() throws ProcessException {
		assertTrue(pv.validateRatioCalculation("single 5 6",correctSmoothingParams));
	}

	@Test
	public void ShouldSucceedWhenFirstParameterIsDouble() throws ProcessException {
		assertTrue(pv.validateRatioCalculation("double 5 6", correctSmoothingParams));
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenFirstParameterIsNotSingle() throws ProcessException {
		assertTrue(pv.validateRatioCalculation("hej 5 6", correctSmoothingParams));
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenSecondParameterIsFloat() throws ProcessException {
		assertTrue(pv.validateRatioCalculation("single 5,6 6", correctSmoothingParams));
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenThirdParameterIsFloat() throws ProcessException {
		assertTrue(pv.validateRatioCalculation("single 5 6,6", correctSmoothingParams));
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenSecondRatioParameterIsBelowZero() throws ProcessException {
		assertTrue(pv.validateRatioCalculation("single -5 6", correctSmoothingParams));
	}

	@Test (expected = ProcessException.class)
	public void ShouldFailWhenThirdRatioParameterIsBelowZero() throws ProcessException {
		assertTrue(pv.validateRatioCalculation("single 5 -6", correctSmoothingParams));
	}
}