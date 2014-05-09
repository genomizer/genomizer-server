package unused;

import java.util.List;

/**
 * Class for holding list of annotation choices and data type.
 *
 * @author yhi04jeo
 */
public class Anno {

	private String label;
	private int dataType;
	private List<String> choicesList;
	private int defaultVal;

	/**
	 * Constructor for initiating Annotation object with free text choices.
	 * Parameters: Annotation label and datatype
	 *
	 * @param String label
	 * @param int dataType
	 */
	public Anno(String label, int dataType) {

		this.label = label;
		this.dataType = dataType;
		choicesList = null;
		defaultVal = -1;
	}

	/**
	 * Constructor for initiating Annotation object with alternative choices.
	 * Parameters: Annotation label, datatype, default value, list of available
	 * choices
	 *
	 * @param String label
	 * @param int dataType
	 * @param int defaultVal
	 * @param List<String> choicesList
	 */
	public Anno(String label, int dataType, int defaultVal,
			List<String> choicesList) {

		this.label = label;
		this.dataType = dataType;
		this.choicesList = choicesList;
		this.defaultVal = defaultVal;
	}

	/**
	 * Gets the label name
	 *
	 * @return String
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Gets the data type
	 *
	 * @return int
	 */
	public int getDataType() {
		return dataType;
	}

	/**
	 * Gets the list of choices.
	 *
	 * @return List<String>
	 */
	public List<String> getChoicesList() {
		return choicesList;
	}

	/**
	 * Gets the default choice value, returns -1 if multiple choice
	 *
	 * @return int
	 */
	public int getDefaultVal() {
		return defaultVal;
	}
}