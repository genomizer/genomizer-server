package database;

public class MaxSize {

	public final static int FILE_PATH = 128;
	public final static int FILE_FILETYPE = 32;
	public final static int FILE_FILENAME = 32;
	public final static int FILE_METADATA = 256;
	public final static int FILE_AUTHOR = 32;
	public final static int FILE_UPLOADER = 32;
	public final static int FILE_EXPID = 64;
	public final static int FILE_GRVERSION = 16;

	public final static int ANNOTATION_LABEL = 32;
	public final static int ANNOTATION_DATATYPE = 16;
	public final static int ANNOTATION_VALUE = 32;
	public final static int	ANNOTATION_DEFAULTVALUE = 32;

	public final static int EXPID = 64;

	public final static int USERNAME = 32;
	public final static int PASSWORD = 32;
	public final static int ROLE = 32;

	public final static int WORKSPACEID = 32;
	public final static int WORKSPACE_NAME = 64;
	public final static int VPATH = 128;

	public final static int GENOME_VERSION = 16;
	public final static int GENOME_SPECIES = 32;
	public final static int GENOME_FILEPATH = 128;

	public final static int CHAIN_FILE_FROMVERSION = 16;
	public final static int CHAIN_FILE_TOVERSION = 16;
	public final static int CHAIN_FILE_FILEPATH = 128;
}
