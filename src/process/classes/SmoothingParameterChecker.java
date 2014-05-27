package process.classes;

public class SmoothingParameterChecker {
	private String windowSize;
	private String minProbe;
	private String smoothType;
	private boolean parametersCorrect;


	private SmoothingParameterChecker(String smoothingParameters) {
		calculateSmoothingParameterValues(smoothingParameters);
	}

	public static SmoothingParameterChecker SmoothingParameterCheckerFactory(String smoothingParameters) {
		return new SmoothingParameterChecker(smoothingParameters);
	}

	private void calculateSmoothingParameterValues(String smoothingParameters) {
		String[] parameters;
		parametersCorrect = true;
		if(smoothingParameters != null) {
			parameters = smoothingParameters.split(" ");
			if(parameters.length == 5) {
				if(parameters[0] != null) {
					windowSize = parameters[0];

				} else {
					parametersCorrect = false;
				}
				if(parameters[1] != null) {
					smoothType = determineSmoothType(parameters[1]);
				} else {
					parametersCorrect = false;
				}
				if(parameters[2] != null) {
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
		if(smoothType.equals("1")) {
			smooth = "median";
		} else if(smoothType.equals("0")) {
			smooth = "mean";
		} else {
			parametersCorrect = false;
		}
		return smooth;
	}

	public String getWindowSize() {
		return windowSize;
	}

	public String getMinProbe() {
		return minProbe;
	}

	public String getSmoothType() {
		return smoothType;
	}

	public boolean checkSmoothParams() {
		return parametersCorrect;
	}




}
