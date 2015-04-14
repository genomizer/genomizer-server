package process.classes;
/**
 * Class used to validate and calculate the string representation of smoothing parameters.
 * @author c11oan
 *
 */
public class SmoothingParameterChecker {
	private String windowSize;
	private String minProbe;
	private String smoothType;
	private boolean parametersCorrect;

	private SmoothingParameterChecker(String smoothingParameters) {
		calculateSmoothingParameterValues(smoothingParameters);
	}

	public static SmoothingParameterChecker SmoothingParameterCheckerFactory(
			String smoothingParameters) {
		return new SmoothingParameterChecker(smoothingParameters);
	}

	private void calculateSmoothingParameterValues(String smoothingParameters) {
		String[] parameters;
		parametersCorrect = true;
		if (smoothingParameters != null) {
			parameters = smoothingParameters.split(" ");
			if (parameters.length == 5) {
				if (parameters[0] != null) {
					windowSize = parameters[0];

				} else {
					parametersCorrect = false;
				}
				if (parameters[1] != null) {
					smoothType = determineSmoothType(parameters[1]);
				} else {
					parametersCorrect = false;
				}
				if (parameters[2] != null) {
					minProbe = parameters[2];
				} else {
					parametersCorrect = false;
				}
			} else {
				parametersCorrect = false;
			}
		} else {
			parametersCorrect = false;
		}
	}

	private String determineSmoothType(String smoothType) {
		String smooth = null;
		if (smoothType.equals("1")) {
			smooth = "median";
		} else if (smoothType.equals("0")) {
			smooth = "mean";
		} else {
			parametersCorrect = false;
		}
		return smooth;
	}
	/**
	 * A method that returns the string representation of window size.
	 * @return
	 */
	public String getWindowSize() {
		return windowSize;
	}
	/**
	 * A method that returns the string representation of minimum probe.
	 * @return
	 */
	public String getMinProbe() {
		return minProbe;
	}
	/**
	 * A method that returns the string representation of smooth type.
	 * @return
	 */
	public String getSmoothType() {
		return smoothType;
	}
	/**
	 * A method that is used to check if the smoothing/stepping parameters were in a correct format.
	 * @return
	 */
	public boolean checkSmoothParams() {
		return parametersCorrect;
	}

}
