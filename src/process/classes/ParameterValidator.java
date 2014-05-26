package process.classes;

public class ParameterValidator extends Executor {

	//validate bowtie params

	//validate bowtie genome file


	public boolean validateSmoothing(String smoothing) throws ProcessException {

		//validate smoothing
			String[] smooth = parse(smoothing);
			int[] smoothParams = new int[smooth.length];
			float[] floatSmooth = new float[smooth.length];

			for(int i = 0; i < smooth.length ;i++) {
				try {
					floatSmooth[i] = Float.parseFloat(smooth[i]);
				} catch (NumberFormatException exc) {
					throw new ProcessException("Parameter nr "+(i+1)+" is not a number");
				}
			}

			for(int i = 0; i < floatSmooth.length; i++) {
				if(floatSmooth[i] % 1 != 0) {
					throw new ProcessException("Parameter "+(i+1)+" is not a integer");
				} else {
					smoothParams[i] = (int) floatSmooth[i];
				}
			}

			for(int i = 0; i < smoothParams.length; i++) {
				if(smoothParams[i] < 0) {
					throw new ProcessException("smoothing parameters must be bigger" +
							" than zero");
				}
			}

			if(smoothParams[1] > 1 || smoothParams[1] < 0) {
				 throw new ProcessException("Parameter 2 needs to be either 1 or 0");
			}

			if(smoothParams[3] > 1 || smoothParams[3] < 0) {
				throw new ProcessException("Parameter 4 needs to be either 1 or 0");
			}

			if(smoothParams[4] > 1 || smoothParams[4] < 0) {
				throw new ProcessException("Parameter 5 needs to be either 1 or 0");
			}

		return true;
	}

	public boolean validateStep(String string) throws ProcessException {

		String[] step = parse(string);
		float stepFloat;
		try {
			stepFloat = Float.parseFloat(step[1]);
		} catch (NumberFormatException e) {
			throw new ProcessException("Stepsize parameters is not a number");
		}

		if(stepFloat % 1 != 0) {
			throw new ProcessException("Stepsize parameter is not an integer");
		}

		return true;
	}

	public boolean validateRatioCalculation(String string) throws ProcessException {
		String[] ratio = parse(string);
		String firstParam = ratio[0];

		float[] ratioFloat = new float[2];
		int[] ratioInt = new int[2];

		for(int i = 0; i < ratio.length-1; i++) {
			try {
				ratioFloat[i] = Float.parseFloat(ratio[i+1]);
			} catch (NumberFormatException e) {
				throw new ProcessException("Ratio calculation parameter "+(i+1)
						+" is not a number");
			}
		}

		for(int i = 0; i < ratioFloat.length; i++) {
			if(ratioFloat[i] % 1 == 0) {
				ratioInt[i] = (int) ratioFloat[i];

			} else {
				throw new ProcessException("Ratio calculation parameter "+(i+1)+" is not a integer");
			}

			if(ratioFloat[i] < 0) {
				throw new ProcessException("Ratio calculation parameter "+i+1+" mus be a positive value");
			}

		}


		if((!firstParam.equals("single")) && (!firstParam.equals("double"))) {
			throw new ProcessException("Ratio calculation first parameter invalid, must be 'single' or 'double'");
		}

		return true;
	}
}



