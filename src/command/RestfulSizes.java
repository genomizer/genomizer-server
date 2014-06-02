package command;

/**
 * Class used to specify the RESTful-header information pieces.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class RestfulSizes {

	public static final int LOGIN_COMMAND = 1;
	public static final int LOGOUT_COMMAND = 1;
	public static final int GET_EXPERIMENT_COMMAND = 2;
	public static final int ADD_EXPERIMENT_COMMAND = 1;
	public static final int UPDATE_EXPERIMENT_COMMAND = 2;
	public static final int DELETE_EXPERIMENT_COMMAND = 2;
	public static final int GET_FILE_FROM_EXPERIMENT_COMMAND = 2;
	public static final int ADD_FILE_TO_EXPERIMENT_COMMAND = 1;
	public static final int UPDATE_FILE_IN_EXPERIMENT_COMMAND = 2;
	public static final int DELETE_FILE_FROM_EXPERIMENT_COMMAND = 2;
	public static final int SEARCH_FOR_EXPERIMENTS_COMMAND = 2;
	public static final int CREATE_USER_COMMAND = 1;
	public static final int UPDATE_USER_COMMAND = 1;
	public static final int DELETE_USER_COMMAND = 2;
	public static final int PROCESS_COMMAND	= 2;
	public static final int GET_PROCESS_STATUS_COMMAND = 1;
	public static final int GET_ANNOTATION_INFORMATION_COMMAND = 1;
	public static final int ADD_ANNOTATION_FIELD_COMMAND = 2;
	public static final int ADD_ANNOTATION_VALUE_COMMAND = 2;
	public static final int REMOVE_ANNOTATION_FIELD_COMMAND = 3;
	public static final int GET_ANNOTATION_PRIVILEGES_COMMAND = 2;
	public static final int UPDATE_ANNOTATION_PRIVILEGES_COMMAND = 3;
	public static final int UPDATE_USER_PRIVILEGES_COMMAND = 3;
	public static final int ADD_GENOME_RELEASE_COMMAND = 1;
	public static final int RENAME_ANNOTATION_VALUE_COMMAND = 2;
	public static final int RENAME_ANNOTATION_FIELD_COMMAND = 2;
	public static final int DELETE_ANNOTATION_VALUE_COMMAND = 4;
	public static final int DELETE_GENOME_RELEASE_COMMAND = 3;
	public static final int GET_ALL_GENOME_RELEASE_COMMAND = 1;
	public static final int GET_GENOME_RELEASE_SPECIES_COMMAND = 2;

}
