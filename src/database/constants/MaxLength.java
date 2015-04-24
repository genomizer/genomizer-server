package database.constants;

/**
 * The integers in this class represent the maximum number
 * of characters the string attributes in the database can hold.
 */
public class MaxLength {

	public final static int FILE_PATH = 1024;
	public final static int FILE_FILETYPE = 32;
	public final static int FILE_FILENAME = 512;
	public final static int FILE_METADATA = 256;
	public final static int FILE_INTUP_FILE_PATH = 1024;
	public final static int FILE_AUTHOR = 32;
	public final static int FILE_UPLOADER = 32;
	public final static int FILE_EXPID = 256;
	public final static int FILE_GRVERSION = 16;

	public final static int ANNOTATION_LABEL = 32;
	public final static int ANNOTATION_DATATYPE = 16;
	public final static int ANNOTATION_VALUE = 32;
	public final static int	ANNOTATION_DEFAULTVALUE = 32;

	public final static int EXPID = 256;

	public final static int USERNAME = 32;
	public final static int PASSWORD = 32;
	public final static int ROLE = 32;

	public final static int WORKSPACEID = 32;
	public final static int WORKSPACE_NAME = 64;
	public final static int VPATH = 128;

	public final static int GENOME_VERSION = 16;
	public final static int GENOME_SPECIES = 32;
	public final static int GENOME_FILEPATH = 512;

	public final static int CHAIN_FILE_FROMVERSION = 16;
	public final static int CHAIN_FILE_TOVERSION = 16;
	public final static int CHAIN_FILE_FILEPATH = 512;
}
