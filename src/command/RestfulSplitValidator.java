package command;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class used to validate and split a restful header.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class RestfulSplitValidator {

	/**
	 * Method used to split restful headers.
	 *
	 * @param restful to split.
	 * @param expectedLength of the header.
	 * @return null if wrongly formatted, else arraylist.
	 */
	public ArrayList<String> addRestful(String restful, CommandType cmdType) {

		ArrayList<String> rest = null;

		if(restful != null) {

			rest = new ArrayList<String>(Arrays.asList(restful.split("/")));;
			rest.remove(0);

			if(rest.size() != getExpectedLength(cmdType)) {

				rest = null;

			}

		}

		return rest;
	}

	/**
	 * Used to get the expected length of the header.
	 *
	 * @param Command type.
	 * @return integer with expected header length.
	 */
	private int getExpectedLength(CommandType cmdt) {

		int expectedLength = 0;

		if(cmdt == CommandType.LOGIN_COMMAND) {
			expectedLength = 1;
		} else if (cmdt == CommandType.LOGOUT_COMMAND) {
			expectedLength = 1;
		} else if (cmdt == CommandType.RETRIEVE_EXPERIMENT_COMMAND) {
			expectedLength = 2;
		} else if (cmdt == CommandType.ADD_EXPERIMENT_COMMAND) {
			expectedLength = 1;
		} else if (cmdt == CommandType.UPDATE_EXPERIMENT_COMMAND) {
			expectedLength = 2;
		} else if (cmdt == CommandType.REMOVE_EXPERIMENT_COMMAND) {
			expectedLength = 2;
		} else if (cmdt == CommandType.GET_FILE_FROM_EXPERIMENT_COMMAND) {
			expectedLength = 2;
		} else if (cmdt == CommandType.ADD_FILE_TO_EXPERIMENT_COMMAND) {
			expectedLength = 1;
		} else if (cmdt == CommandType.UPDATE_FILE_IN_EXPERIMENT_COMMAND) {
			expectedLength = 2;
		} else if (cmdt == CommandType.DELETE_FILE_FROM_EXPERIMENT_COMMAND) {
			expectedLength = 2;
		} else if (cmdt == CommandType.SEARCH_FOR_EXPERIMENTS_COMMAND) {
			expectedLength = 2;
		} else if (cmdt == CommandType.UPDATE_USER_COMMAND) {
			expectedLength = 1;
		} else if (cmdt == CommandType.DELETE_USER_COMMAND) {
			expectedLength = 1;
		} else if (cmdt == CommandType.PROCESS_COMMAND) {
			expectedLength = 1;
		} else if (cmdt == CommandType.GET_ANNOTATION_INFORMATION_COMMAND) {
			expectedLength = 1;
		} else if (cmdt == CommandType.ADD_ANNOTATION_FIELD_COMMAND) {
			expectedLength = 2;
		} else if (cmdt == CommandType.ADD_ANNOTATION_VALUE_COMMAND) {
			expectedLength = 2;
		} else if (cmdt == CommandType.RENAME_ANNOTATION_VALUE_COMMAND) {
			expectedLength = 2;
		} else if (cmdt == CommandType.RENAME_ANNOTATION_FIELD_COMMAND) {
			expectedLength = 2;
		} else if (cmdt == CommandType.REMOVE_ANNOTATION_FIELD_COMMAND) {
			expectedLength = 3;
		} else if (cmdt == CommandType.GET_ANNOTATION_PRIVILEGES_COMMAND) {
			expectedLength = 2;
		} else if (cmdt == CommandType.UPDATE_ANNOTATION_PRIVILEGES_COMMAND) {
			expectedLength = 2;
		} else if (cmdt == CommandType.ADD_GENOME_RELEASE_COMMAND) {
			expectedLength = 1;
		} else if (cmdt == CommandType.DELETE_GENOME_RELEASE_COMMAND) {
			expectedLength = 3;
		}

		return expectedLength;

	}

}
