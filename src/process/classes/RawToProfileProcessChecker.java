package process.classes;

public class RawToProfileProcessChecker {
	private boolean shouldRunBowTie;
	private boolean shouldRunSamToGff;
	private boolean shouldRunGffToAllnusgr;
	private boolean shouldRunSmoothing;
	private boolean shouldRunStep10;
	private boolean shouldRunRatioCalculation;
	private boolean isCorrect;

	private RawToProfileProcessChecker() {

	}

	public static RawToProfileProcessChecker rawToProfileCheckerFactory() {
		return new RawToProfileProcessChecker();
	}

	public void calculateWhichProcessesToRun(String[] parameters) {
		if ((!parameters[0].equals("")) && (!parameters[1].equals(""))) {
			shouldRunBowTie = true;
		}

		if (!parameters[2].equals("") && shouldRunBowTie) {
			shouldRunSamToGff = true;
		}

		if ((!parameters[3].equals("")) && (shouldRunBowTie)
				&& (shouldRunSamToGff)) {
			shouldRunGffToAllnusgr = true;
		}

		if ((!parameters[4].equals("")) && (shouldRunGffToAllnusgr)) {
			shouldRunSmoothing = true;
		}

		if ((!parameters[5].equals("")) && (shouldRunSmoothing)) {
			shouldRunStep10 = true;
		}

		if (!parameters[6].equals("") && (shouldRunStep10)) {
			shouldRunRatioCalculation = true;
		}

	}

	public boolean shouldRunBowTie() {
		return shouldRunBowTie;
	}

	public boolean shouldRunSamToGff() {
		return shouldRunSamToGff;
	}

	public boolean shouldRunGffToAllnusgr() {
		return shouldRunGffToAllnusgr;
	}

	public boolean shouldRunSmoothing() {
		return shouldRunSmoothing;
	}

	public boolean shouldRunStep() {
		return shouldRunStep10;
	}

	public boolean shouldRunRatioCalc() {
		return shouldRunRatioCalculation;
	}
}