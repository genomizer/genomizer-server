package util;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import model.ErrorLogger;

/**
 * Helper class that can check if some details of whether a process
 * is ready to be processed.
 * @author (of comment) c12oor
 *
 */
public final class Process {

    /**
     * Check if the given parameters are okay for processing.
     * The data will be got from the processTab. Method will be called before
     * performing a processing.
     * Data is fed as the Jtext/checkbox fields. (OO)
     * @param smoothWindowSize
     * @param stepPosition
     * @param stepSize
     * @param sgrFormat
     * @param smoothing
     * @param stepBox
     * @return
     */
    public static boolean isCorrectToProcess(JTextField smoothWindowSize,
            JTextField stepPosition, JTextField stepSize, boolean sgrFormat,
            JCheckBox smoothing, JCheckBox stepBox) {

        if (!sgrFormat) {
            return true;
        } else if (!smoothing.isSelected()) {
            return true;
        } else if (aboveZero(smoothWindowSize.getText().trim())
                && zeroOrAbove(stepPosition.getText().trim())) {
            if (!stepBox.isSelected()
                    || (stepBox.isSelected() && aboveZero(stepSize.getText()
                            .trim()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Used to check if the given parameters is acceptable for a
     * ratio calculation.
     * The data will be got from a RatioCalcPopup.
     * Data is fed as the Jtext/checkbox fields. (OO)
     * @param ratioWindowSize
     * @param inputReads
     * @param chromosome
     * @param ratioStepPosition
     * @return true if all values ar valid (correctly nonnegative numbers), else false
     */
    public static boolean isRatioCorrectToProcess(JTextField ratioWindowSize,
            JTextField inputReads, JTextField chromosome,
            JTextField ratioStepPosition) {
        return aboveZero(ratioWindowSize.getText().trim())
                && zeroOrAbove(inputReads.getText().trim())
                && zeroOrAbove(chromosome.getText().trim())
                && zeroOrAbove(ratioStepPosition.getText().trim());
    }

    private static boolean aboveZero(String string) {

        try {
            int value = Integer.parseInt(string);
            return value > 0;
        } catch (NumberFormatException e) {
            ErrorLogger.log(e);
            return false;
        }
    }

    private static boolean zeroOrAbove(String string) {

        try {
            int value = Integer.parseInt(string);
            return value >= 0;
        } catch (NumberFormatException e) {
            ErrorLogger.log(e);
            return false;
        }
    }

}
