package command;

import java.util.HashMap;

/**
 * Class to be used as a reference for the "length" of the request URI. For
 * example the get experiment request URI looks like
 * /experiment/\<experiment-id\> abd hence has the length 2.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class RestfulLengths {
	private static final HashMap<CommandType, Integer> restfulLengths;

	static {
		restfulLengths = new HashMap<>();
		restfulLengths.put(CommandType.LOGIN_COMMAND, 1);
		restfulLengths.put(CommandType.LOGOUT_COMMAND, 1);
		restfulLengths.put(CommandType.GET_EXPERIMENT_COMMAND, 2);
		restfulLengths.put(CommandType.ADD_EXPERIMENT_COMMAND, 1);
		restfulLengths.put(CommandType.UPDATE_EXPERIMENT_COMMAND, 2);
		restfulLengths.put(CommandType.DELETE_EXPERIMENT_COMMAND, 2);
		restfulLengths.put(CommandType.GET_FILE_FROM_EXPERIMENT_COMMAND, 2);
		restfulLengths.put(CommandType.ADD_FILE_TO_EXPERIMENT_COMMAND, 1);
		restfulLengths.put(CommandType.UPDATE_FILE_IN_EXPERIMENT_COMMAND, 2);
		restfulLengths.put(CommandType.DELETE_FILE_FROM_EXPERIMENT_COMMAND, 2);
		restfulLengths.put(CommandType.SEARCH_FOR_EXPERIMENTS_COMMAND, 2);
		restfulLengths.put(CommandType.CREATE_USER_COMMAND, 1);
		restfulLengths.put(CommandType.DELETE_USER_COMMAND, 2);
		restfulLengths.put(CommandType.PROCESS_COMMAND, 2);
		restfulLengths.put(CommandType.GET_PROCESS_STATUS_COMMAND, 1);
		restfulLengths.put(CommandType.GET_ANNOTATION_INFORMATION_COMMAND, 1);
		restfulLengths.put(CommandType.ADD_ANNOTATION_FIELD_COMMAND, 2);
		restfulLengths.put(CommandType.ADD_ANNOTATION_VALUE_COMMAND, 2);
		restfulLengths.put(CommandType.REMOVE_ANNOTATION_FIELD_COMMAND, 3);
		restfulLengths.put(CommandType.GET_ANNOTATION_PRIVILEGES_COMMAND, 2);
		restfulLengths.put(CommandType.UPDATE_ANNOTATION_PRIVILEGES_COMMAND, 3);
		restfulLengths.put(CommandType.ADD_GENOME_RELEASE_COMMAND, 1);
		restfulLengths.put(CommandType.RENAME_ANNOTATION_VALUE_COMMAND, 2);
		restfulLengths.put(CommandType.RENAME_ANNOTATION_FIELD_COMMAND , 2);
		restfulLengths.put(CommandType.DELETE_ANNOTATION_VALUE_COMMAND, 4);
		restfulLengths.put(CommandType.DELETE_GENOME_RELEASE_COMMAND, 3);
		restfulLengths.put(CommandType.GET_ALL_GENOME_RELEASE_COMMAND, 1);
		restfulLengths.put(CommandType.GET_GENOME_RELEASE_SPECIES_COMMAND, 2);
	}

	/**
	 * Returns the "length" that a request URI should have. For example, in the
	 * request URI "/experiment/\<experiment-id\>" the length is 2.
	 * @param cmdt the command type.
	 * @return the "size" of the request URI.
	 */
	public static int getSize(CommandType cmdt) {
		return restfulLengths.get(cmdt);
	}
}
