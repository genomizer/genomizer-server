package process;

/**
 * Class used to validate incoming parameters to RawToProfile so all parameters
 * are correct format. 
 * v 1.0
 */
public class ParameterValidator extends Executor {

	/**
	 * Validated smoothing parameters
	 * 
	 * @param smoothing
	 * @return
	 * @throws ProcessException
	 */
	public boolean validateSmoothing(String smoothing) throws ProcessException {

		// validate smoothing
		String[] smooth = parse(smoothing);

		if (smooth.length != 5) {
			throw new ProcessException(
					"Not correct number of parameters for smoothing, should be 5");
		}

		int[] smoothParams = new int[smooth.length];
		
		// Parse all parameters to float first to check that all are integers.
		float[] floatSmooth = new float[smooth.length];
		for (int i = 0; i < floatSmooth.length; i++) {
			try {
				floatSmooth[i] = Float.parseFloat(smooth[i]);
			} catch (NumberFormatException exc) {
				throw new ProcessException("Parameter nr " + (i + 1)
						+ " is not a number");
			}
		}

		// Checks that all parameters are integers
		for (int i = 0; i < floatSmooth.length; i++) {
			if (floatSmooth[i] % 1 != 0) {
				throw new ProcessException("Parameter " + (i + 1)
						+ " is not a integer");
			} else {
				smoothParams[i] = (int) floatSmooth[i];
			}
		}

	
		for (int i = 0; i < smoothParams.length; i++) {
			if (smoothParams[i] < 0) {
				throw new ProcessException(
						"smoothing parameters must be bigger" + " than zero");
			}
		}

		if (smoothParams[1] > 1 || smoothParams[1] < 0) {
			throw new ProcessException("Parameter 2 needs to be either 1 or 0");
		}

		if (smoothParams[3] > 1 || smoothParams[3] < 0) {
			throw new ProcessException("Parameter 4 needs to be either 1 or 0");
		}

		if (smoothParams[4] > 1 || smoothParams[4] < 0) {
			throw new ProcessException("Parameter 5 needs to be either 1 or 0");
		}

		return true;
	}

	/**
	 * Validates step parameters
	 * 
	 * @param string
	 * @return
	 * @throws ProcessException
	 */
	public boolean validateStep(String string) throws ProcessException {

		String[] step = parse(string);

		if (step.length != 2) {
			throw new ProcessException(
					"Not correct number of parameters for step size, should be 2");
		}
		
		// Parse to float to check that all parameters are integers.
		float stepFloat;
		try {
			stepFloat = Float.parseFloat(step[1]);
		} catch (NumberFormatException e) {
			throw new ProcessException("Stepsize parameters is not a number");
		}

		// Checks that parameters are integers.
		if (stepFloat % 1 != 0) {
			throw new ProcessException("Stepsize parameter is not an integer");
		}

		return true;
	}

	/**
	 * Validates Ratio Calculation parameters, Also validates the smoothing 
	 * parameters that is used on the Ratio calculated files cause they should
	 * always be done together.
	 * 
	 * @param RatioParam
	 * @param smoothParam
	 * @return
	 * @throws ProcessException
	 */
	public boolean validateRatioCalculation(String RatioParam,
			String smoothParam) throws ProcessException {
		System.out.println("RATIO CHECKER");
		String[] ratio = parse(RatioParam);

		if (ratio.length != 3) {
			throw new ProcessException(
					"Not correct number of parameters for ratio calculation, should be 3");
		}

		System.out.println("ratioLength = " + ratio.length);
		String firstParam = ratio[0];

		float[] ratioFloat = new float[2];
		int[] ratioInt = new int[2];

		for (int i = 0; i < ratio.length - 1; i++) {
			try {
				ratioFloat[i] = Float.parseFloat(ratio[i + 1]);
			} catch (NumberFormatException e) {
				throw new ProcessException("Ratio calculation parameter "
						+ (i + 1) + " is not a number");
			}
		}

		for (int i = 0; i < ratioFloat.length; i++) {
			if (ratioFloat[i] % 1 == 0) {
				ratioInt[i] = (int) ratioFloat[i];

			} else {
				throw new ProcessException("Ratio calculation parameter "
						+ (i + 1) + " is not a integer");
			}

			if (ratioFloat[i] < 0) {
				throw new ProcessException("Ratio calculation parameter " + i
						+ 1 + " mus be a positive value");
			}

		}

		if ((!firstParam.equals("single")) && (!firstParam.equals("double"))) {
			throw new ProcessException(
					"Ratio calculation first parameter invalid, must be 'single' or 'double'");
		}

		validateSmoothing(smoothParam);

		return true;
	}
}
